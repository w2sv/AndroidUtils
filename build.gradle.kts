import nl.littlerobots.vcu.plugin.resolver.VersionSelectors

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.androidJUnit5) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.versionCatalogUpdate)
}

versionCatalogUpdate {
    versionSelector(VersionSelectors.PREFER_STABLE)
}

// Set version from $ROOT/version.txt, which is created or updated when running the make publish routine
rootProject.layout.projectDirectory.file("version.txt").asFile.let { versionFile ->
    if (versionFile.exists()) {
        version = versionFile.readText().trim()
    }
}
