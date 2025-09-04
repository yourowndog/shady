package com.goofy.goober.shady.portal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawRect
import androidx.compose.ui.graphics.drawscope.drawCircle
import androidx.compose.ui.unit.dp
import android.graphics.RuntimeShader

@Composable
fun PortalCanvas(
    modifier: Modifier = Modifier,
    shader: RuntimeShader,
    ringThicknessDp: Float = 6f,
    onFrame: ((shader: RuntimeShader, sizePx: Size, timeSec: Float) -> Unit)? = null,
    timeSec: Float = rememberFrameSeconds()
) {
    // Remember brush once per shader instance
    val brush = remember(shader) { ShaderBrush(shader) }
    // Keep a current callback reference
    val currentOnFrame by rememberUpdatedState(onFrame)

    androidx.compose.foundation.layout.Box(
        modifier = modifier.drawWithCache {
            // cache setup
            val ringThicknessPx = ringThicknessDp.dp.toPx()
            val ringRadius = size.minDimension / 2f - ringThicknessPx / 2f
            val clipRadius = ringRadius - ringThicknessPx / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            // prebuild clip path
            val clipPath = Path().apply {
                addOval(Rect(center = center, radius = clipRadius))
            }

            onDrawWithContent {
                // per-frame uniform updates before draw
                currentOnFrame?.invoke(shader, size, timeSec)

                // ring stroke
                drawCircle(
                    color = Color.White,
                    radius = ringRadius,
                    center = center,
                    style = Stroke(width = ringThicknessPx)
                )

                // shader fill clipped to circle
                clipPath(clipPath) {
                    drawRect(brush = brush)
                }
            }
        }
    )
}
