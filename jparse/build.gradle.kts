plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kspGradle)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kspApi)
    implementation(libs.kotlinPoet)
    implementation(libs.kotlinPoetKsp)
    implementation(libs.json)
}