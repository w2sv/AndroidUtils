plugins {
    id("w2sv.android-library")
    alias(libs.plugins.androidJUnit5)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.w2sv.androidutils.test"
            artifactId = "junit5"
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
    api(libs.junit.jupiter.api)
    implementation(libs.androidx.arch.core.runtime)
}
