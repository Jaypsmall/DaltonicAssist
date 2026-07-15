package com.jaylizapp.daltonicassist.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun CameraHud(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2
        val rectSize = 100.dp.toPx()

        // Dibujar el visor central (brackets)
        val bracketLength = 20.dp.toPx()
        val strokeWidth = 2.dp.toPx()
        val color = Color.White.copy(alpha = 0.7f)

        // Top-left bracket
        drawPath(
            path = androidx.compose.ui.graphics.Path().apply {
                moveTo(centerX - rectSize / 2, centerY - rectSize / 2 + bracketLength)
                lineTo(centerX - rectSize / 2, centerY - rectSize / 2)
                lineTo(centerX - rectSize / 2 + bracketLength, centerY - rectSize / 2)
            },
            color = color,
            style = Stroke(width = strokeWidth)
        )

        // Top-right bracket
        drawPath(
            path = androidx.compose.ui.graphics.Path().apply {
                moveTo(centerX + rectSize / 2 - bracketLength, centerY - rectSize / 2)
                lineTo(centerX + rectSize / 2, centerY - rectSize / 2)
                lineTo(centerX + rectSize / 2, centerY - rectSize / 2 + bracketLength)
            },
            color = color,
            style = Stroke(width = strokeWidth)
        )

        // Bottom-left bracket
        drawPath(
            path = androidx.compose.ui.graphics.Path().apply {
                moveTo(centerX - rectSize / 2, centerY + rectSize / 2 - bracketLength)
                lineTo(centerX - rectSize / 2, centerY + rectSize / 2)
                lineTo(centerX - rectSize / 2 + bracketLength, centerY + rectSize / 2)
            },
            color = color,
            style = Stroke(width = strokeWidth)
        )

        // Bottom-right bracket
        drawPath(
            path = androidx.compose.ui.graphics.Path().apply {
                moveTo(centerX + rectSize / 2 - bracketLength, centerY + rectSize / 2)
                lineTo(centerX + rectSize / 2, centerY + rectSize / 2)
                lineTo(centerX + rectSize / 2, centerY + rectSize / 2 - bracketLength)
            },
            color = color,
            style = Stroke(width = strokeWidth)
        )

        // Centro cruz
        drawLine(
            color = color,
            start = Offset(centerX - 10.dp.toPx(), centerY),
            end = Offset(centerX + 10.dp.toPx(), centerY),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = color,
            start = Offset(centerX, centerY - 10.dp.toPx()),
            end = Offset(centerX, centerY + 10.dp.toPx()),
            strokeWidth = 1.dp.toPx()
        )
    }
}
