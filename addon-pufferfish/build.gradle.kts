import me.modmuss50.mpp.ReleaseType
import net.minecraftforge.gradle.userdev.tasks.JarJar
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

@Suppress("PropertyName") val mod_group_id: String by project
@Suppress("PropertyName") val mod_id: String by project
@Suppress("PropertyName") val mapping_channel: String by project
@Suppress("PropertyName") val mapping_version: String by project
@Suppress("PropertyName") val minecraft_version: String by project
@Suppress("PropertyName") val forge_version: String by project
@Suppress("PropertyName") val mod_version: String by project

plugins {
    id("net.minecraftforge.gradle")
    id("org.spongepowered.mixin")
    eclipse
    idea
    id("org.parchmentmc.librarian.forgegradle")
    id("me.modmuss50.mod-publish-plugin")
}

version = mod_version
group = mod_group_id

base {
    archivesName.set(mod_id)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

minecraft {
    mappings(mapping_channel, mapping_version)
    copyIdeResources.set(true)

    runs {
        configureEach {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")

            mods {
                create(mod_id) {
                    source(sourceSets.main.get())
                }
            }
        }

        create("client") {
            property("forge.enabledGameTestNamespaces", mod_id)
        }

        create("server") {
            property("forge.enabledGameTestNamespaces", mod_id)
            args("--nogui")
        }

        create("gameTestServer") {
            property("forge.enabledGameTestNamespaces", mod_id)
        }

        create("data") {
            workingDirectory(project.file("run-data"))
            args("--mod", mod_id, "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/"))
        }
    }
}

mixin {
    add(sourceSets.main.get(), "$mod_id.refmap.json")
    config("$mod_id.mixins.json")
}

sourceSets.main.configure {
    resources.srcDir("src/generated/resources")
}

jarJar.enable()

dependencies {
    "minecraft"("net.minecraftforge:forge:$minecraft_version-$forge_version")

    implementation(fg.deobf("dev.latvian.mods:kubejs-forge:2001.6.5-build.14"))
    implementation(fg.deobf("dev.latvian.mods:rhino-forge:2001.2.2-build.18"))
    implementation(fg.deobf("dev.architectury:architectury-forge:9.1.13"))

    implementation(fg.deobf("curse.maven:puffish-skills-835091:8181003"))

    implementation(fg.deobf("curse.maven:astages-1120180:8387881"))

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

tasks.named<ProcessResources>("processResources") {
    val props = mapOf(
        "minecraft_version" to project.property("minecraft_version"),
        "minecraft_version_range" to project.property("minecraft_version_range"),
        "forge_version" to project.property("forge_version"),
        "forge_version_range" to project.property("forge_version_range"),
        "loader_version_range" to project.property("loader_version_range"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.property("mod_version"),
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description")
    )

    inputs.properties(props)

    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
        expand(props + mapOf("project" to project))
    }
}

tasks.named("jarJar").configure {
    finalizedBy("reobfJarJar")
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            "Specification-Title" to mod_id,
            "Specification-Vendor" to project.property("mod_authors"),
            "Specification-Version" to "1",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to project.property("mod_authors"),
            "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
            "MixinConfigs" to "${mod_id}.mixins.json"
        )
    }

    dependsOn(tasks.named("jarJar"))
    finalizedBy("reobfJar")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

publishMods {
    file.set(tasks.named("jarJar", JarJar::class).flatMap { it.archiveFile })
    modLoaders.add("forge")
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val formattedDate: String = today.format(formatter)
    val changelogFile = layout.projectDirectory.file("CHANGELOG.md")
    val formattedVersion = mod_version.substringBeforeLast("-")
    val addonModId = mod_id.replace("astages_", "") // .capitalized()
    val formattedAddonModId = "Pufferfish's Skills"

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
        projectId.set("1303921")
        minecraftVersions.add(minecraft_version)
        changelogType.set("markdown")
        requires("astages", "puffish-skills")

        displayName.set("$mod_id-$mod_version")

        projectSlug.set("astages-pufferfishs-skills") // For discord setup
        announcementTitle.set("Download from CurseForge") // For discord setup
    }

    modrinth {
        accessToken.set(providers.environmentVariable("MODRINTH_API_KEY"))
        projectId.set("tG1ZzCp5")
        minecraftVersions.add(minecraft_version)
        requires("astages", "skills")

        displayName.set("$mod_id-$mod_version")

        if (type.get() == ReleaseType.STABLE) {
            changelog.set(
                providers.fileContents(changelogFile)
                    .asText
                    .map { it.lineSequence().drop(2).joinToString("\n") }
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