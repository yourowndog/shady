package com.goofy.goober.shady.portal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PortalCanvas(
    spec: EffectSpec,
    params: Map<String, Float>,
    modifier: Modifier = Modifier,
    ringThicknessDp: Dp = 8.dp
) {
    val shader = remember(spec.runtimeId) { ShaderRepo.get(spec.runtimeId) }
    val brush = remember(shader) { ShaderBrush(shader) }
    val t by rememberFrameSeconds()

    androidx.compose.foundation.layout.Box(
        modifier = modifier.drawWithCache {
            val ringThicknessPx = ringThicknessDp.toPx()
            val ringRadius = size.minDimension / 2f - ringThicknessPx / 2f
            val clipRadius = ringRadius - ringThicknessPx / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            val path = Path().apply { addOval(Rect(center = center, radius = clipRadius)) }

            onDrawWithContent {
                val speed = params["speed"] ?: 1f
                shader.setFloatUniform("time", t * speed)
                shader.setFloatUniform("resolution", size.width, size.height)
                spec.apply(shader, params)

                drawCircle(
                    color = Color.White,
                    radius = ringRadius,
                    center = center,
                    style = Stroke(width = ringThicknessPx)
                )

                clipPath(path) {
                    drawRect(brush = brush)
                }
            }
        }
    )
}
