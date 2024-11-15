plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.sonarqube") version "5.1.0.4882"
}

android {
    namespace = "com.mendoza.servicio_web"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mendoza.servicio_web"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

sonar {
    properties {
        property("sonar.projectKey", "rmm-01_app-inventariado")
        property("sonar.organization", "rmm-01")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
git add .github/workflows/sonarcloud-analysis.yml
dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    //Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.room:room-common:2.6.1")

    testImplementation("junit:junit:4.13.2")//Pruebas con JUnit
    testImplementation ("org.jetbrains.kotlin:kotlin-test-junit:1.8.0") // Pruebas con Kotlin
    testImplementation ("org.mockito:mockito-core:4.0.0") // Mockito para simulaciones (mocks)
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0") // Mockito adaptado a Kotlin
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}