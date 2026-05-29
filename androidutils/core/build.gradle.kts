plugins {
    id("w2sv.android-library")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat.resources)
    implementation(libs.w2sv.kotlinutils.coroutines)

    testImplementation(libs.bundles.unitTest)
}
