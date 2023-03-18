import tw.idv.louislee.Version

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("com.google.devtools.ksp")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("io.insert-koin:koin-core:${Version.KOIN}")
    implementation("io.insert-koin:koin-annotations:${Version.KOIN_KSP}")
    ksp("io.insert-koin:koin-ksp-compiler:${Version.KOIN_KSP}")
}
