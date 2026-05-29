plugins {
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

// Include every module applying this convention plugin in the root Dokka site.
rootProject.dependencies.add("dokka", project)

android {
    namespace = "com.w2sv.${project.name.replace('-', '.')}"
    compileSdk = 37

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes { getByName("release") { isMinifyEnabled = false } }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures { buildConfig = false }

    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all { test -> test.failOnNoDiscoveredTests = false }
        }
    }
}

mavenPublishing {
    coordinates(
        artifactId = project.name,
        version = rootProject.version.toString()
    )
}
