package org.example.bigdisplayproject.ui.cursor

import org.example.bigdisplayproject.data.remote.dto.cursor.ScreenPosition
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import java.awt.Cursor
import java.awt.Frame
import java.awt.Point
import java.awt.Robot
import java.awt.Toolkit
import java.awt.Window
import java.awt.event.InputEvent
import java.awt.image.BufferedImage
import java.io.File
import java.util.Timer
import javax.imageio.ImageIO
import kotlin.concurrent.schedule

class CursorManager {
    private val robot = Robot()
    private var currentCursorType: CursorType = CursorType.DEFAULT
    private val cursors = mutableMapOf<CursorType, Cursor>()
    private var isMousePressed = false

    private var lastClickTime = 0L
    private val clickCooldown = 2500L // клик произойдёт, если ползователь "кликает" > этого времени
    private var clickRequested = false

    private val cursorScale = 2

    init {
        loadCursors()
        setCursor(CursorType.DEFAULT)
    }

    private fun loadCursors() {
        cursors[CursorType.DEFAULT] = loadCursor("customCursorMove.png")
        cursors[CursorType.CLICK] = loadCursor("customCursorClick.png")
        cursors[CursorType.DRAG] = loadCursor("customCursorDrag.png")
    }

    private fun loadCursor(imagePath: String): Cursor {
        return try {
            val image = ImageIO.read(File("${File("").absoluteFile}\\src\\commonMain\\composeResources\\drawable\\$imagePath"))
            Toolkit.getDefaultToolkit().createCustomCursor(
                image,
                Point(0, 0),
                "custom_cursor_${imagePath.hashCode()}"
            )
        } catch (e: Exception) {
            println("Error loading cursor $imagePath: ${e.message}")
            Cursor.getDefaultCursor()
        }
    }

    fun updateCursor(position: ScreenPosition, gesture: Int) {
        // Перемещение курсора
        robot.mouseMove(position.x.toInt(), -position.y.toInt())

        // Определяем тип курсора по жесту
        val newCursorType = when (gesture) {
            1 -> CursorType.DRAG      // Удержание
            2 -> CursorType.CLICK     // Клик
            else -> CursorType.DEFAULT
        }

        // Обработка действий мыши в зависимости от жеста
        handleMouseActions(gesture)

        // Обновляем курсор если тип изменился
        if (newCursorType != currentCursorType) {
            setCursor(newCursorType)
            currentCursorType = newCursorType
        }
    }

    private fun setCursor(cursorType: CursorType) {
        val cursor = cursors[cursorType] ?: Cursor.getDefaultCursor()

        // Получаем все фреймы окна и устанавливаем курсор
        Frame.getFrames().forEach { frame ->
            frame.cursor = cursor
        }

        // Также устанавливаем для всех компонентов
        Window.getWindows().forEach { window ->
            window.cursor = cursor
        }
    }

    private fun handleMouseActions(currentGesture: Int) {
        when (currentGesture) {
            1 -> { // DRAG - нажатие и удержание левой кнопки мыши
                if (!isMousePressed) {
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
                    isMousePressed = true
                    println("CURSOR: Mouse pressed for drag")
                } // Если мышь уже нажата, продолжаем перемещение

                clickRequested = false
            }

            2 -> { // CLICK - одиночный клик
                // Если мышь была нажата (например, из предыдущего drag), отпускаем
                if (isMousePressed) {
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
                    isMousePressed = false
                }

                if (!clickRequested) {
                    clickRequested = true
                    lastClickTime = System.currentTimeMillis()
                }
                else {
                    if (lastClickTime - System.currentTimeMillis() >= clickCooldown) {
                        performClick()
                    }
                }
            }

            else -> { // DEFAULT - отпускаем кнопку, если была нажата
                if (isMousePressed) {
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
                    isMousePressed = false
                    println("CURSOR: Mouse released")
                }

                clickRequested = false
            }
        }
    }

    private fun performClick() {
        // Выполняем клик: нажатие + отпускание
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
        println("CURSOR: Single click performed")

        clickRequested = false
    }

    fun dispose() {
        if (isMousePressed) {
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
        }
        cursors.clear()
    }
}