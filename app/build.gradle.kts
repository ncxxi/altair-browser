plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ncxxi.altair"
    compileSdk = 36

    defaultConfig {
        applicationId = "ncxxi.altair"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/*.kotlin_module",
            )
        }
    }
}

configurations.configureEach {
    resolutionStrategy.capabilitiesResolution.withCapability("org.mozilla.telemetry:glean-native") {
        val gv = candidates.firstOrNull { c ->
            (c.id as? org.gradle.api.artifacts.result.ModuleComponentIdentifier)?.module?.contains("geckoview") == true
        }
        if (gv != null) {
            select(gv)
            because("use GeckoView Glean instead of standalone Glean")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    debugImplementation(libs.androidx.ui.tooling)

    implementation(libs.mozilla.browser.engine.gecko)
    implementation(libs.mozilla.browser.state)
    implementation(libs.mozilla.compose.browser.toolbar)
    implementation(libs.mozilla.feature.session)
    implementation(libs.mozilla.concept.engine)
    implementation(libs.mozilla.support.ktx)
}
