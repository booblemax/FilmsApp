plugins {
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktLint
    id("io.gitlab.arturbosch.detekt") version Versions.detekt
}

buildscript {

    val kotlinVersion by extra("1.4.0")
    repositories {
        google()
        jcenter {
            content {
                includeGroup("org.jetbrains.kotlinx")
            }
        }
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Classpath.gradle)
        classpath(Classpath.kotlinGradlePlugin)
        classpath(Classpath.ktlinGradlePlugin)
    }
}

allprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    repositories {
        google()
        jcenter()
    }

    ktlint {
        version.set("0.35.0")
        debug.set(false)
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(false)
        disabledRules.set(listOf("import-ordering"))
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    detekt {
        config = rootProject.files("config/detekt/detekt.yml")
        reports {
            html {
                enabled = true
                destination = file("build/reports/detekt.html")
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
