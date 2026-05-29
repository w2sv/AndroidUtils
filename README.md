# AndroidUtils

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.w2sv/androidutils-core)
![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/w2sv/AndroidUtils?include_prereleases)
[![Build](https://github.com/w2sv/AndroidUtils/actions/workflows/workflow.yaml/badge.svg)](https://github.com/w2sv/AndroidUtils/actions/workflows/workflow.yaml)
![GitHub](https://img.shields.io/github/license/w2sv/AndroidUtils)

Small Android utility libraries published as separate artifacts.

## 📚 Documentation

API reference: [w2sv.github.io/AndroidUtils](https://w2sv.github.io/AndroidUtils/)

## 📦 Modules

| Artifact | Description |
| --- | --- |
| `androidutils-backpress` | Back press handling utilities |
| `androidutils-core` | Core Android utilities |
| `androidutils-view` | View utilities |
| `androidutils-lifecycle` | Lifecycle utilities |
| `androidutils-junit5` | JUnit 5 test utilities |

## 🚀 Installation

### Inline

```kotlin
dependencies {
    implementation("io.github.w2sv:androidutils-backpress:<version>")
    implementation("io.github.w2sv:androidutils-core:<version>")
    implementation("io.github.w2sv:androidutils-view:<version>")
    implementation("io.github.w2sv:androidutils-lifecycle:<version>")
    testImplementation("io.github.w2sv:androidutils-junit5:<version>")
}
```

### Version Catalog (`libs.versions.toml`)

```toml
[versions]
w2sv-androidutils = "<version>"

[libraries]
w2sv-androidutils-backpress = { module = "io.github.w2sv:androidutils-backpress", version.ref = "w2sv-androidutils" }
w2sv-androidutils-core = { module = "io.github.w2sv:androidutils-core", version.ref = "w2sv-androidutils" }
w2sv-androidutils-view = { module = "io.github.w2sv:androidutils-view", version.ref = "w2sv-androidutils" }
w2sv-androidutils-lifecycle = { module = "io.github.w2sv:androidutils-lifecycle", version.ref = "w2sv-androidutils" }
w2sv-androidutils-junit5 = { module = "io.github.w2sv:androidutils-junit5", version.ref = "w2sv-androidutils" }
```

Then add the aliases you need:

```kotlin
dependencies {
    implementation(libs.w2sv.androidutils.backpress)
    implementation(libs.w2sv.androidutils.core)
    implementation(libs.w2sv.androidutils.view)
    implementation(libs.w2sv.androidutils.lifecycle)
    testImplementation(libs.w2sv.androidutils.junit5)
}
```

## 📄 License

Licensed under the Apache License 2.0.
