SHELL=/bin/bash

update-dependencies:
	@./gradlew versionCatalogUpdate

update-gradle:
	@./gradlew wrapper --gradle-version latest

format:
	@./gradlew ktlintFormat

publish:
	@echo "###### Checking for uncommitted changes ######"; \
		if ! git diff --quiet || ! git diff --cached --quiet; then \
			echo "There are uncommitted changes! Commit or stash them before publishing."; \
			exit 1; \
		fi

	@echo "###### Bump version ######"
	@./scripts/bump-version.sh $(type)

	@echo "###### Build & test ######"
	@./gradlew build

	@VERSION=$$(cat version.txt); \
	echo "###### Create git tag $$VERSION ######"; \
	git tag "$$VERSION"; \
	git push origin "$$VERSION"

	@echo "###### Generate release notes ######"; \
	@./scripts/generate-release-notes.sh

	@VERSION=$$(cat version.txt); \
	echo "###### Create GitHub release ######"; \
	gh release create "$$VERSION" -F release-notes.txt
