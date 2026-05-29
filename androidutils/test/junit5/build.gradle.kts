plugins {
    id("w2sv.android-library")
    alias(libs.plugins.mannodermaus.android.junit5)
}

dependencies {
    api(libs.junit.jupiter.api)
    implementation(libs.androidx.arch.core.runtime)
}
