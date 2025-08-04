SHELL=/bin/bash

VERSION := $(shell grep -Po '^version=\K.*' gradle.properties)

update-dependencies:
	@./gradlew versionCatalogUpdate

update-gradle:
	@./gradlew wrapper --gradle-version latest

format:
	@./gradlew ktlintFormat

publish:
	@echo "###### Assembling & running checks ######"
	@./gradlew build
	@echo "###### Pushing latest changes ######"
	@git status;git add .;git commit -m '$(VERSION)';git push
	@echo "###### Creating release ######"
	@gh release create $(VERSION) --generate-notes
