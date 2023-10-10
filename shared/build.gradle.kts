import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.example.moveeapp_compose_kmm.Deps
import com.example.moveeapp_compose_kmm.util.requireStringProperty

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("kotlin-parcelize")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.codingfeline.buildkonfig")
}

buildkonfig {
    packageName = "com.example.moveeapp_compose_kmm"

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING, "API_KEY_TMDB", System.getenv("API_KEY_TMDB"),
            const = true
        )

        buildConfigField(
            FieldSpec.Type.STRING,
            "BASE_URL",
            rootProject.requireStringProperty("BASE_URL"),
            const = true
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "IMAGE_BASE_URL",
            rootProject.requireStringProperty("IMAGE_BASE_URL"),
            const = true
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "REGISTER_URL",
            rootProject.requireStringProperty("REGISTER_URL"),
            const = true
        )

        buildConfigField(
            FieldSpec.Type.STRING,
            "RESET_PASSWORD_URL",
            rootProject.requireStringProperty("RESET_PASSWORD_URL"),
            const = true
        )
    }
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
        extraSpecAttributes["resources"] =
            "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {

                // Compose
                with(compose) {
                    api(runtime)
                    api(foundation)
                    api(material)
                    api(material3)
                    api(materialIconsExtended)
                    api(animation)
                }

                // Coroutines
                api(Deps.Org.JetBrains.Kotlinx.coroutinesCore)

                // KotlinX Serialization Json
                api(Deps.Org.JetBrains.Kotlinx.kotlinxSerializationJson)

                // Ktor
                with(Deps.Io.Ktor) {
                    api(ktorClientCore)
                    api(ktorSerializationKotlinxJson)
                    api(ktorClientContentNegotiation)
                    api(ktorClientLogging)
                }

                // Koin
                with(Deps.Koin) {
                    api(core)
                    api(test)
                    api(compose)
                }

                //Navigation
                with(Deps.Navigation.Voyager) {
                    api(navigation)
                    api(viewModel)
                    api(koin)
                    api(tabNavigator)
                    api(transition)
                }

                // Logback for ktor logging
                api(Deps.Logback.logbackClassic)

                //Image loader
                api(Deps.Resources.sekio)

                //KVault
                api(Deps.Kvault.Kvault)

                //Moko
                with(Deps.Resources.Moko) {
                    api(moko)
                    api(compose)
                }
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                // Ktor
                api(Deps.Io.Ktor.ktorClientAndroid)

                // Koin
                api(Deps.Koin.android)

                api(Deps.Org.JetBrains.Kotlinx.kotlinxSerializationJson)

                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                implementation("androidx.compose.ui:ui-util:1.5.0")
                implementation("androidx.compose.ui:ui-tooling:1.5.0")
                implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")

            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.2")
                implementation("io.ktor:ktor-client-ios:2.3.1")
            }
        }
    }
}

android {
    namespace = "com.example.moveeapp_compose_kmm"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
}

multiplatformResources {
    multiplatformResourcesPackage = "com.example.moveeapp_compose_kmm"
}