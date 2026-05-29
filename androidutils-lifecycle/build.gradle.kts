import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("w2sv.android-library")
    alias(libs.plugins.mannodermaus.android.junit5)
}

// Run unit tests (which respect the java toolchain, as they run on the JVM) on Java 17, which is required by JUnit5
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

// Compile unit test code with JVM 11 to fix inconsistency
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    if (name.contains("UnitTest", ignoreCase = true)) {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
}

android {
    // THIS forces main code to target JVM 11
    kotlin.compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.lifecycle.livedata.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(projects.androidutilsJunit5)
}
