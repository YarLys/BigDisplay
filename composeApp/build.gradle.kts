import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization")
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // Библиотека для работы с iCalendar
            implementation("org.mnode.ical4j:ical4j:4.0.0-alpha11")
            // Для работы с датами
            implementation("joda-time:joda-time:2.12.5")
            // Дата/время для KMP
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

            implementation(compose.materialIconsExtended) // For icons
            // Material3
            implementation("org.jetbrains.compose.material3:material3-desktop:1.4.0")
            // Animations
            implementation(compose.animation)
            implementation(compose.animationGraphics)
            // QR Code generator
            implementation("com.google.zxing:core:3.5.1")
            implementation("com.google.zxing:javase:3.5.1")

            // Video player
            //implementation("uk.co.caprica:vlcj:4.7.0")
            //implementation(libs.alert.kmp)

            // Navigation
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
            // Coil
            implementation(libs.coil.compose.core)
            implementation(libs.coil.mp)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.compose)
            // MVIKotlin
            implementation("com.arkivanov.mvikotlin:mvikotlin:4.3.0")
            implementation("com.arkivanov.mvikotlin:mvikotlin-main:4.3.0")
            implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:4.3.0")
            // Ktor
            implementation("io.ktor:ktor-client-core:2.3.7")
            implementation("io.ktor:ktor-client-cio:2.3.7")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
            implementation("io.ktor:ktor-client-logging:2.3.7")
            implementation("io.ktor:ktor-client-okhttp:2.3.7")
            // Koin (DI)
            implementation("io.insert-koin:koin-core:3.5.6")
            implementation("io.insert-koin:koin-compose:1.1.0")

            /*implementation("org.openjfx:javafx-controls:17.0.16")
            implementation("org.openjfx:javafx-graphics:17.0.16")
            implementation("org.openjfx:javafx-fxml:17.0.16")*/
            /*val fxSuffix = "win"
            implementation("org.openjfx:javafx-base:17:${fxSuffix}")
            implementation("org.openjfx:javafx-graphics:17:${fxSuffix}")
            implementation("org.openjfx:javafx-controls:17:${fxSuffix}")
            implementation("org.openjfx:javafx-swing:17:${fxSuffix}")
            implementation("org.openjfx:javafx-web:17:${fxSuffix}")
            implementation("org.openjfx:javafx-media:17:${fxSuffix}")*/
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            // VideoPlayer
            implementation("uk.co.caprica:vlcj:4.7.0")
            implementation("org.jetbrains.compose.ui:ui-graphics:1.1.1")
            implementation("uk.co.caprica:vlcj:4.8.2")

            //implementation("io.github.khubaibkhan4:mediaplayer-kmp:2.0.9")
        }
    }
}


compose.desktop {
    application {
        mainClass = "org.example.bigdisplayproject.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.bigdisplayproject"
            packageVersion = "1.0.0"
        }
    }
}
