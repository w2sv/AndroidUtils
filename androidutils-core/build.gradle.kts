plugins {
    id("w2sv.android-library")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat.resources)

    testImplementation(libs.bundles.unitTest)
}
