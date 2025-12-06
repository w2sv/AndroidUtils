plugins {
    id("w2sv.android-library")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.w2sv.androidutils"
            artifactId = "core"
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
                description.set("Utilities for Android development.")
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
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.resources)
    implementation(libs.w2sv.kotlinutils)

    testImplementation(libs.bundles.unitTest)
}
