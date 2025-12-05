#!/usr/bin/env bash
set -e

# Output file (repo root)
OUTPUT_FILE="release-notes.txt"

# GitHub repo URL (adjust if not auto-detectable)
# You can try to auto-detect with `git remote get-url origin` and convert to https
GITHUB_URL=$(git remote get-url origin | sed -E 's/(git@|https:\/\/)([^:/]+)[/:](.+)\.git/\1https:\/\/\2\/\3/')

# Previous tag
LAST_TAG=$(git describe --tags --abbrev=0 HEAD^ 2>/dev/null || echo "")

# Header
echo "# Changes since last release" > "$OUTPUT_FILE"
echo "" >> "$OUTPUT_FILE"

# Generate release notes
if [ -z "$LAST_TAG" ]; then
    # No previous tag, include all commits
    COMMITS=$(git log --pretty=format:"%h %s")
else
    COMMITS=$(git log "$LAST_TAG"..HEAD --pretty=format:"%h %s")
fi

# Write commits as markdown links
while IFS= read -r line; do
    HASH=$(echo "$line" | awk '{print $$1}')
    MSG=$(echo "$line" | cut -d' ' -f2-)
    # Markdown link: [message](https://github.com/user/repo/commit/hash)
    echo "- [$MSG]($GITHUB_URL/commit/$HASH)" >> "$OUTPUT_FILE"
done <<< "$COMMITS"

echo "Release notes written to $OUTPUT_FILE"
