import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.jetbrainsCompose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenLocal()
        google()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("com.guardsquare:proguard-gradle:7.2.0")
    }
}

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("kapt") version "1.7.10"
    id("org.jetbrains.compose") version "1.2.0"
    id("java")
    id("idea")
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
}

val versionCode = "1.0.0"
group = "de.mobanisto"
version = versionCode

allprojects {
    repositories {
        mavenCentral()
        jetbrainsCompose()
        google()
        mavenLocal()
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.2")
    implementation("net.java.dev.jna:jna:5.12.1")
    implementation("net.java.dev.jna:jna-platform:5.12.1")
    implementation("ch.qos.logback:logback-classic:1.4.4")
    implementation("de.mobanisto:wintoast:0.1.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.register<proguard.gradle.ProGuardTask>("minify") {
    val packageUberJarForCurrentOS by tasks.getting
    dependsOn(packageUberJarForCurrentOS)
    val files = packageUberJarForCurrentOS.outputs.files
    injars(files)
    outjars(files.map { file -> File(file.parentFile, "${file.nameWithoutExtension}.min.jar") })

    val library = if (System.getProperty("java.version").startsWith("1.")) "lib/rt.jar" else "jmods"
    libraryjars("${System.getProperty("java.home")}/$library")

    configuration("proguard-rules.pro")
}

compose.desktop {
    application {
        mainClass = "de.mobanisto.test.MainWindowsToastsKt"
        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.Msi)
            packageName = "Test Notifications"
            description = "Test Notifications using Compose"
            vendor = "Mobanisto"
            copyright = "2022 Mobanisto"
            licenseFile.set(project.file("LICENSE.txt"))
            linux {
                packageName = "test-project"
                debPackageVersion = versionCode
                appCategory = "comm"
                menuGroup = "Network;Chat;InstantMessaging;"
            }
            windows {
                iconFile.set(project.file("src/main/resources/icon.ico"))
                upgradeUuid = "CBF591AA-28D0-49DC-B812-43F6583AF06C"
            }
        }
    }
}
