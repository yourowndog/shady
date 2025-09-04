package com.goofy.goober.shady.portal

import android.graphics.RuntimeShader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawWithCache
import androidx.compose.ui.unit.dp

@Composable
fun PortalCanvas(
    shader: RuntimeShader,
    ringThicknessDp: Float = 6f,
    modifier: Modifier = Modifier.size(260.dp),
    onFrame: ((shader: RuntimeShader, sizePx: Size, timeSec: Float) -> Unit)? = null,
) {
    val brush = remember(shader) { ShaderBrush(shader) }
    val time = rememberFrameSeconds()
    val currentTime by rememberUpdatedState(time)
    val currentOnFrame by rememberUpdatedState(onFrame)
    Canvas(
        modifier = modifier.drawWithCache {
            val ringThicknessPx = ringThicknessDp.dp.toPx()
            val ringRadius = size.minDimension / 2f - ringThicknessPx / 2f
            val clipRadius = ringRadius - ringThicknessPx / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            val path = Path().apply { addOval(Rect(center = center, radius = clipRadius)) }
            onDrawBehind {
                currentOnFrame?.invoke(shader, size, currentTime)
                drawCircle(
                    color = Color.White,
                    radius = ringRadius,
                    center = center,
                    style = Stroke(width = ringThicknessPx)
                )
                clipPath(path) { drawRect(brush) }
            }
        }
    )
}

