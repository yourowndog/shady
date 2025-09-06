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
import com.goofy.goober.shaders.GradientShader
import kotlin.math.pow

private fun Color.toLinear(): Triple<Float, Float, Float> {
    fun channel(c: Float): Float {
        return if (c <= 0.04045f) {
            c / 12.92f
        } else {
            ((c + 0.055f) / 1.055f).toDouble().pow(2.4).toFloat()
        }
    }
    return Triple(channel(red), channel(green), channel(blue))
}

@Composable
fun PortalCanvas(
    shader: RuntimeShader,
    params: EffectParams,
    ringThicknessDp: Float = 6f,
    modifier: Modifier = Modifier.size(260.dp)
) {
    val brush = remember(shader) { ShaderBrush(shader) }
    SketchWithCache(modifier = modifier) { time ->
        shader.setFloatUniform("resolution", size.width, size.height)
        shader.setFloatUniform("time", time)
        shader.setFloatUniform("uSpeed", params.speed)
        if (shader === GradientShader) {
            val (br, bg, bb) = (params.base ?: Color(0xFFCCCCCC)).toLinear()
            shader.setFloatUniform("uBase", br, bg, bb)
            val (ar, ag, ab) = (params.amp ?: Color(0xFF333333)).toLinear()
            shader.setFloatUniform("uAmp", ar, ag, ab)
            val phase = params.phase ?: Triple(1f, 2f, 4f)
            shader.setFloatUniform("uPhase", phase.first, phase.second, phase.third)
        }
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

