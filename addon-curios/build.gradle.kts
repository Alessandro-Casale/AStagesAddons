import me.modmuss50.mpp.ReleaseType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Suppress("PropertyName") val mod_id: String by project
@Suppress("PropertyName") val mod_group_id: String by project
@Suppress("PropertyName") val mod_version: String by project
@Suppress("PropertyName") val minecraft_version: String by project

plugins {
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
}

neoForge {
    mods {
        create(mod_id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

group = mod_group_id

dependencies {
    implementation("curse.maven:curios-309927:6529130")
}

publishMods {
    file.set(tasks.jar.flatMap { it.archiveFile })
    modLoaders.add("neoforge")
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val formattedDate: String = today.format(formatter)
    val changelogFile = layout.projectDirectory.file("CHANGELOG.md")
    val formattedVersion = mod_version.substringBeforeLast("-")
    val addonModId = mod_id.replace("astages_", "") // .capitalized()
    val formattedAddonModId = "Curios"

    when {
        mod_version.contains("alpha", true) -> {
            type.set(ALPHA)
            changelog.set(
                """
                        ## [$formattedVersion] - $formattedDate
                        This is an alpha version meant to be used only by developers!   
                        Changelog can be found in Discord server.
                    """.trimIndent()
            )
        }
        mod_version.contains("beta", true) -> {
            type.set(BETA)
            changelog.set(
                """
                        ## [$formattedVersion] - $formattedDate
                        This is a beta version meant to be used only by developers!   
                        Changelog can be found in Discord server.
                    """.trimIndent()
            )
        }
        else -> {
            type.set(STABLE)
            changelog.set(providers.fileContents(changelogFile).asText.orElse("No changelog provided."))
        }
    }

    github {
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
        repository.set("Alessandro-Casale/AStagesAddons")
        val version = mod_version.substringBeforeLast("-")
        val branch = mod_version.substringAfterLast("-")
        commitish.set(branch.toMcRange())
        tagName.set("$addonModId-v$mod_version")

        displayName.set("AStages $formattedAddonModId $mod_version")

        announcementTitle.set("Download from GitHub")
    }

    curseforge {
        accessToken.set(providers.environmentVariable("CURSEFORGE_API_KEY"))
        projectId.set("1303904")
        minecraftVersions.add(minecraft_version)
        changelogType.set("markdown")
        requires("astages", "curios")

        displayName.set("$mod_id-$mod_version")

        projectSlug.set("astages-curios") // For discord setup
        announcementTitle.set("Download from CurseForge") // For discord setup
    }

    modrinth {
        accessToken.set(providers.environmentVariable("MODRINTH_API_KEY"))
        projectId.set("lBYd97ML")
        minecraftVersions.add(minecraft_version)
        requires("astages", "curios")

        displayName.set("$mod_id-$mod_version")

        if (type.get() == ReleaseType.STABLE) {
            changelog.set(
                providers.fileContents(changelogFile)
                    .asText
                    .map { it.lineSequence().drop(3).joinToString("\n") }
            )
        } else {
            changelog.set(changelog.get().dropFirstLine())
        }

        announcementTitle.set("Download from Modrinth")
    }
}

fun String.toMcRange(): String {
    return this.substringBeforeLast(".") + ".X"
}

fun String.dropFirstLine(): String {
    return lines().drop(1).joinToString("\n")
}