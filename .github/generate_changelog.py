import subprocess
import re
import sys
from datetime import datetime
from collections import defaultdict

addon = sys.argv[1]

def run(cmd):
    return subprocess.check_output(cmd, shell=True).decode().strip()

# -------------------------
# 1. TAG RANGE
# -------------------------
tags = run(f"git tag --list '{addon}-v*-1.20.1'").split("\n")
tags = [t for t in tags if t]

if tags:
    from_tag = sorted(tags)[-1]
else:
    from_tag = run("git rev-list --max-parents=0 HEAD")

# -------------------------
# 2. COMMITS
# -------------------------
log = run(f"git log {from_tag}..HEAD --pretty=format:'%s'")
commits = log.split("\n")

# -------------------------
# 3. CATEGORY MAP
# -------------------------
categories = {
    "feat": "New Features",
    "fix": "Bug Fixes",
    "api": "API Changes",
    "perf": "Performance Improvements",
    "removed": "Removed Features"
}

pattern = re.compile(
    r"^(feat|fix|api|perf|removed)(\([^)]+\))?:\s*(.+)$"
)

grouped = defaultdict(list)
seen = set()

# -------------------------
# 4. PARSE + GROUP
# -------------------------
for c in commits:
    m = pattern.match(c)
    if not m:
        continue

    type_ = m.group(1)
    raw_scope = m.group(2)
    scope = raw_scope.strip("()") if raw_scope else None
    title = m.group(3).strip()

    if scope is None or scope == addon:
        grouped[type_].append(title)

# -------------------------
# 5. VERSION
# -------------------------
version = run(
    f"grep '^mod_version=' addon-{addon}/gradle.properties | cut -d= -f2"
).split("-")[0]

date = datetime.now().strftime("%d-%m-%Y")

# -------------------------
# 6. BUILD OUTPUT
# -------------------------
output = []
output.append(f"## [{version}] - {date}  ")
output.append("")

for key in ["feat", "fix", "api", "perf", "removed"]:
    items = grouped.get(key, [])
    if not items:
        continue

    output.append(f"{categories[key]}  ")
    for i in items:
        output.append(f"- {i}  ")
    output.append("")

final_output = "\n".join(output).strip() + "\n"

# -------------------------
# 7. WRITE FILE
# -------------------------
with open(f"addon-{addon}/CHANGELOG.md", "w") as f:
    f.write(final_output)

print(final_output)