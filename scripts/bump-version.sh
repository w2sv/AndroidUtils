#!/usr/bin/env bash
set -e

BUMP_TYPE="$1"
VERSION_FILE="version.txt"

if [ -z "$BUMP_TYPE" ]; then
  echo "Usage: $0 major|minor|patch"
  exit 1
fi

echo "Fetching tags..."
git fetch --tags

LATEST=$(git tag --sort=-v:refname | head -n 1)

if [ -z "$LATEST" ]; then
  LATEST="0.0.0"
fi

echo "Latest tag: $LATEST"

IFS='.' read -r MAJOR MINOR PATCH <<< "$LATEST"

case "$BUMP_TYPE" in
  major)
    NEW="$((MAJOR + 1)).0.0"
    ;;
  minor)
    NEW="$MAJOR.$((MINOR + 1)).0"
    ;;
  patch)
    NEW="$MAJOR.$MINOR.$((PATCH + 1))"
    ;;
  *)
    echo "Invalid type: must be major|minor|patch"
    exit 1
    ;;
esac

echo "New version: $NEW"
echo "$NEW" > "$VERSION_FILE"
echo "Wrote $NEW to $VERSION_FILE"
