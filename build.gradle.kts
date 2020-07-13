plugins {
    id("org.jlleitschuh.gradle.ktlint") version "6.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.10.0"
}

buildscript {

    repositories {
        google()
        jcenter() {
            content {
                includeGroup("org.jetbrains.kotlinx")
            }
        }
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Classpath.gradle)
        classpath(Classpath.kotlinGradlePlugin)
        classpath(Classpath.navigationSafeArgs)
        classpath(Classpath.ktlinGradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
