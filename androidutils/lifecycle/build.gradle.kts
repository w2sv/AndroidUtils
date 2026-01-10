import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("w2sv.android-library")
    alias(libs.plugins.androidJUnit5)
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

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.w2sv.androidutils"
            artifactId = "lifecycle"
            version = version.toString()
            afterEvaluate {
                from(components["release"])
            }
            pom {
                developers {
                    developer {
                        id.set("w2sv")
                        name.set("Janek Zangenberg")
                    }
                }
                description.set("Lifecycle utilities for Android development.")
                url.set("https://github.com/w2sv/AndroidUtils")
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.localbroadcastmanager)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(project(":androidutils:test:junit5"))
}
