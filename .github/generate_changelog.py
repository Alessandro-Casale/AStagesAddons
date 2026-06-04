import subprocess
import re
import sys
from datetime import datetime

addon = sys.argv[1]

def run(cmd):
    return subprocess.check_output(cmd, shell=True).decode().strip()

tags = run(f"git tag --list '{addon}-v*'").split("\n")
tags = [t for t in tags if t]

if tags:
    from_tag = sorted(tags)[-1]
else:
    from_tag = run("git rev-list --max-parents=0 HEAD")

log = run(f"git log {from_tag}..HEAD --pretty=format:'%s'")

commits = log.split("\n")

global_commits = []
addon_commits = []

pattern = re.compile(r"^(feat|fix|docs|refactor|chore)(\((.+)\))?: (.+)$")

for c in commits:
    m = pattern.match(c)
    if not m:
        continue

    scope = m.group(3)
    title = m.group(4)

    if scope is None:
        global_commits.append(title)
    elif scope == addon:
        addon_commits.append(title)

def section(title, items):
    if not items:
        return ""
    out = f"### {title}\n"
    for i in items:
        out += f"- {i}\n"
    return out + "\n"

version = run(f"grep '^mod_version=' addon-{addon}/gradle.properties | cut -d= -f2").split("-")[0]
date = datetime.now().strftime("%d-%m-%Y")

output = f"## [{version}] - {date}\n\n"
output += section("General changes", global_commits)
output += section(f"{addon} changes", addon_commits)

with open(f"addon-{addon}/CHANGELOG.md", "w") as f:
    f.write(output)

print(output)