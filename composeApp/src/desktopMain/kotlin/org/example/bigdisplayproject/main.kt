package org.example.bigdisplayproject

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.bigdisplayproject.ui.App
import java.awt.Cursor
import java.awt.Point
import java.awt.Toolkit
import java.io.File
import javax.imageio.ImageIO

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "BigDisplayProject",
        state = rememberWindowState().apply {
            placement = WindowPlacement.Fullscreen
        },
        undecorated = true
    ) {
        Box {
            App()
        }
    }
}

private fun setCustomCursor(): Cursor {
    return Toolkit.getDefaultToolkit().createCustomCursor(
        ImageIO.read(File("D:\\Projects\\Kotlin\\Android\\KMP\\BigDisplayProject\\composeApp\\src\\commonMain\\composeResources\\drawable\\customCursorMove.png")),
        Point(0, 0),
        "custom_cursor"
    )
}

// TODO: Если будете использовать KMP WebView, необходимо настроить KCEF
/*
private fun TryKCEFInit() {
    var kcefInitialized by remember { mutableStateOf(false) }
    var kcefError by remember { mutableStateOf<String?>(null) }
    var showKCEFUI by remember { mutableStateOf(false) }

    val bundleLocation = System.getProperty("compose.application.resources.dir")?.let { File(it) } ?: File(".")
    // Инициализация KCEF после создания окна
    LaunchedEffect(Unit) {
        try {
            withContext(Dispatchers.IO) {
                KCEF.init(
                    builder = {
                        installDir(File(bundleLocation, "kcef-bundle")) // recommended, but not necessary

                        progress {
                            onDownloading {
                                // use this if you want to display a download progress for example
                                println("KCEF INIT: $it%")
                            }
                            onInitialized {
                                println("KCEF INIT: DONE")
                                /*kcefInitialized = true
                                showKCEFUI = true*/
                                println("KCEF INIT: SUCCESS")
                            }
                        }
                    },
                    onRestartRequired = {
                        // all required CEF packages downloaded but the application needs a restart to load them (unlikely to happen)
                        println("KCEF INIT: Restart is required.")
                    },
                    onError = { error ->
                        //kcefError = "KCEF ERROR: ${error?.message}"
                        println("KCEF ERROR: ${error?.message}")
                    }
                )
            }
        } catch (e: Exception) {
            //kcefError = "KCEF EXCEPTION: ${e.message}"
            //showKCEFUI = true // Все равно показываем UI
            println("KCEF EXCEPTION: ${e.message}")
        }
        println("DEBUG 1")
    }
    println("DEBUG 2")
    LaunchedEffect (kcefInitialized) {
        println("KCEF INITIALIZED")
        withContext(Dispatchers.IO) {
            KCEF.newClient()
        }
    }


    Window(
        onCloseRequest = ::exitApplication,
        title = "BigDisplayProject",
        state = rememberWindowState().apply {
            placement = WindowPlacement.Fullscreen
        },
        undecorated = true
    ) {
        if (showKCEFUI && kcefInitialized) {
            // Показываем ваш основной UI только когда KCEF готов
            Box(
                modifier = Modifier
                    .pointerHoverIcon(
                        PointerIcon(setCustomCursor())
                    )
            ) {
                App()
            }
        } else {
            // Показываем загрузочный экран
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (kcefError != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Ошибка KCEF:", color = Color.Red)
                        Text(kcefError!!, color = Color.Red)
                        Button(onClick = { showKCEFUI = true }) {
                            Text("Продолжить без KCEF")
                        }
                    }
                } else if (!kcefInitialized) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Инициализация KCEF...")
                        CircularProgressIndicator()
                        Button(
                            onClick = {
                                kcefError = "KCEF пропущен"
                                showKCEFUI = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.LightGray
                            )
                        ) {
                            Text("Пропустить KCEF")
                        }
                    }
                }
            }
        }

 }
 */