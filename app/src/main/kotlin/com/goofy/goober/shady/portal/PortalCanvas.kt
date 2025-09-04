package com.goofy.goober.shady.portal

import android.graphics.RuntimeShader
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import com.goofy.goober.sketch.SketchWithCache

@Composable
fun PortalCanvas(
    shader: RuntimeShader,
    ringThicknessDp: Float = 6f,
    modifier: Modifier = Modifier.size(260.dp)
) {
    val brush = remember(shader) { ShaderBrush(shader) }
    SketchWithCache(modifier = modifier) { time ->
        shader.setFloatUniform("resolution", size.width, size.height)
        shader.setFloatUniform("time", time)
        val ringThicknessPx = ringThicknessDp.dp.toPx()
        val ringRadius = size.minDimension / 2f - ringThicknessPx / 2f
        val clipRadius = ringRadius - ringThicknessPx / 2f
        onDrawBehind {
            val center = Offset(size.width / 2f, size.height / 2f)
            drawCircle(
                color = Color.White,
                radius = ringRadius,
                center = center,
                style = Stroke(width = ringThicknessPx)
            )
            val path = Path().apply {
                addOval(Rect(center = center, radius = clipRadius))
            }
            clipPath(path) {
                drawRect(brush)
            }
        }
    }
}

