import subprocess
import re
import sys
from datetime import datetime

addon = sys.argv[1]

def run(cmd):
    return subprocess.check_output(cmd, shell=True).decode().strip()

# 1. GET TAG RANGE
tags = run(f"git tag --list '{addon}-v*'").split("\n")
tags = [t for t in tags if t]

if tags:
    from_tag = sorted(tags)[-1]
else:
    from_tag = run("git rev-list --max-parents=0 HEAD")

# 2. GET COMMITS
log = run(f"git log {from_tag}..HEAD --pretty=format:'%s'")

commits = log.split("\n")

# 3. PARSE
pattern = re.compile(r"^(feat|fix|docs|refactor|chore|api|perf|removed)(\(([^)]+)\))?:\s*(.+)$")

seen = set()
output_commits = []

for c in commits:
    m = pattern.match(c)
    if not m:
        continue

    type_ = m.group(1)
    scope = m.group(3)
    title = m.group(4).strip()

    # dedupe
    key = f"{type_}:{scope}:{title}"
    if key in seen:
        continue
    seen.add(key)

    # FILTER LOGIC (IMPORTANT FIX)
    if scope is None or scope == addon or scope.startswith(addon):
        output_commits.append(f"- {type_}: {title}")

# 4. VERSION
version = run(f"grep '^mod_version=' addon-{addon}/gradle.properties | cut -d= -f2").split("-")[0]

date = datetime.now().strftime("%d-%m-%Y")

# 5. FINAL OUTPUT (NO GLOBAL/SPEC SPLIT)
changelog = f"""
## [{version}] - {date}

### Changes
{chr(10).join(output_commits) if output_commits else "- No relevant changes"}
"""

with open(f"addon-{addon}/CHANGELOG.md", "w") as f:
    f.write(changelog)

print(changelog)