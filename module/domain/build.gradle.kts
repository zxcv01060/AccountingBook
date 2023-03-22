import tw.idv.louislee.Version

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("com.google.devtools.ksp")
    id("com.squareup.sqldelight")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

sqldelight {
    // Database name
    database("AccountingBookDatabase") {
        // Package name used for the generated MyDatabase.kt
        packageName = "tw.idv.louislee.accountingbook.domain.entity"

        // The directory where to store '.db' schema files relative to the root
        // of the project. These files are used to verify that migrations yield
        // a database with the latest schema. Defaults to null so the verification
        // tasks will not be created.
        schemaOutputDirectory = File("src/main/sqldelight/databases")

        // The dialect version you would like to target
        // Defaults to "sqlite:3.18"
        dialect = "sqlite:3.24"

        // If set to true, migration files will fail during compilation if there are errors in them.
        // Defaults to false
        verifyMigrations = true
    }
}

dependencies {
    implementation("io.insert-koin:koin-core:${Version.KOIN}")
    implementation("io.insert-koin:koin-annotations:${Version.KOIN_KSP}")
    ksp("io.insert-koin:koin-ksp-compiler:${Version.KOIN_KSP}")
    implementation("com.squareup.sqldelight:runtime:${Version.SQL_DELIGHT}")
    implementation("com.squareup.sqldelight:coroutines-extensions-jvm:${Version.SQL_DELIGHT}")
}
