fun DependencyHandlerScope.kapt(dependencyProvider : Provider<MinimalExternalModuleDependency>){
    add("kapt", dependencyProvider.get())
}

plugins {
    kotlin("kapt")
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.samadtch.yassirchallenge.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.samadtch.yassirchallenge.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.shared)

    //Compose
    implementation(libs.compose.ui)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.icons)
    implementation(libs.compose.lifecycle)
    implementation(libs.compose.navigation)

    //Androidx
    implementation(libs.androidx.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.viewmodel)

    //Dependency Injection
    implementation(libs.koin.android)
    implementation(libs.hilt)
    implementation(libs.hilt.compose.navigation)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)

    //Paging
    implementation(libs.paging.compose)
    implementation(libs.paging.common)

    //Others
    implementation(libs.ktor.client.core)
    implementation(libs.coil.compose)
}