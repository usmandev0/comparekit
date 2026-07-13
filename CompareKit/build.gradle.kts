import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.usmandev0"
version = libs.versions.compareKit.get()

kotlin {
    android {
        namespace = "com.usmandev.comparekit"
        //noinspection GradleDependency
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "CompareKit"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.animation)
            api(libs.compose.ui)
        }

        androidMain.dependencies {
            implementation(libs.coil.compose)
            implementation(libs.coil.network.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.ktor.client.darwin)
        }
    }
}

private fun String.normalizedSigningKeyId(): String {
    val hex = trim()
        .removePrefix("0x")
        .removePrefix("0X")
        .filter { it.isDigit() || it in 'A'..'F' || it in 'a'..'f' }

    require(hex.isNotEmpty()) {
        "signingInMemoryKeyId is empty. Set SIGNING_KEY_ID to your GPG key ID " +
            "(run: gpg --list-secret-keys --keyid-format=long)."
    }

    val shortKeyId = when (hex.length) {
        8 -> hex
        16, 40 -> hex.takeLast(8)
        else -> error(
            "signingInMemoryKeyId must be 8, 16, or 40 hexadecimal characters, but got " +
                "'$this' (${hex.length} hex chars after cleanup). " +
                "Run: gpg --list-secret-keys --keyid-format=long",
        )
    }

    return shortKeyId.uppercase()
}

plugins.withId("signing") {
    extensions.configure<SigningExtension>("signing") {
        val keyIdProperty = providers.gradleProperty("signingInMemoryKeyId")
        val keyProperty = providers.gradleProperty("signingInMemoryKey")
        val passwordProperty = providers.gradleProperty("signingInMemoryKeyPassword")

        if (keyIdProperty.isPresent && keyProperty.isPresent) {
            useInMemoryPgpKeys(
                keyIdProperty.get().normalizedSigningKeyId(),
                keyProperty.get().trim(),
                passwordProperty.orNull?.trim().orEmpty(),
            )
        }
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(
        "io.github.usmandev0",
        "comparekit",
        version.toString()
    )

    pom {
        name.set("CompareKit")
        description.set("A Kotlin Multiplatform Compose library for interactive before/after image comparison.")
        inceptionYear.set("2026")
        url.set("https://github.com/usmandev0/comparekit")

        licenses {
            license {
                name.set("Apache License 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("usmandev0")
                name.set("Usman")
                url.set("https://github.com/usmandev0")
            }
        }

        scm {
            url.set("https://github.com/usmandev0/comparekit")
            connection.set("scm:git:git://github.com/usmandev0/comparekit.git")
            developerConnection.set("scm:git:ssh://git@github.com/usmandev0/comparekit.git")
        }
    }
}
