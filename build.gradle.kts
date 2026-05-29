import nl.littlerobots.vcu.plugin.resolver.VersionSelectors

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.mannodermaus.android.junit5) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.versionCatalogUpdate)
}

versionCatalogUpdate {
    versionSelector(VersionSelectors.PREFER_STABLE)
}

// Set version from $ROOT/version.txt, which is created or updated when running the make publish routine
rootProject.file("version.txt")
    .takeIf { it.exists() }
    ?.readText()
    ?.trim()
    ?.let { version = it }
