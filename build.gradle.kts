// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false // Use a recent stable version
    id("com.android.library") version "8.2.0" apply false // If you have library modules
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false // Use a recent stable Kotlin version
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
