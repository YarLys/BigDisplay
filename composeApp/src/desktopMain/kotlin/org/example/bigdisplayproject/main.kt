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
    /*System.setProperty("compose.interop.blending", "true")
    System.setProperty("compose.swing.render.on.graphics", "true")*/

    /*System.setProperty("prism.order", "sw") // программный рендеринг
    System.setProperty("prism.verbose", "true")
    // Явно указываем путь к JavaFX модулям
    val javaHome = System.getProperty("java.home")
    val javafxPath = "D:\\Projects\\Kotlin\\Android\\KMP\\BigDisplayProject\\javafx-sdk-17.0.16\\lib"
*/
    Window(
        onCloseRequest = ::exitApplication,
        title = "BigDisplayProject",
        state = rememberWindowState().apply {
            placement = WindowPlacement.Fullscreen
        },
        undecorated = true
    ) {
        Box(
            modifier = Modifier
                .pointerHoverIcon(
                    PointerIcon(setCustomCursor())
                )
        ) {
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