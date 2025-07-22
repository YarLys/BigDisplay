package org.example.bigdisplayproject.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageConfig
import com.google.zxing.client.j2se.MatrixToImageWriter
import org.example.bigdisplayproject.ui.theme.LightWhite
import java.awt.image.BufferedImage

fun generateQRCode(
    text: String,
    width: Int,
    height: Int
): BufferedImage {
    val matrix = MultiFormatWriter().encode(
        text,
        BarcodeFormat.QR_CODE,
        width,
        height
    )
    return MatrixToImageWriter.toBufferedImage(matrix, MatrixToImageConfig(0xFF000000.toInt(), 0xE2E2E2))
}

private fun BufferedImage.toComposeImage(): java.awt.Image {
    return this
}

@Composable
fun QrCode(
    src: String,
    width: Int = 200,
    height: Int = 200,
    modifier: Modifier = Modifier
) {
    val qrCodeBitmap = remember(src) {
        generateQRCode(src, width, height)
            .toComposeImageBitmap()
    }

    Image(
        bitmap = qrCodeBitmap,
        contentDescription = "QR Code for $src",
        modifier = modifier
    )
}