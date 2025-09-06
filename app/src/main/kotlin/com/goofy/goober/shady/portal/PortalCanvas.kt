package com.goofy.goober.shady.portal

import android.graphics.RuntimeShader
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush

enum class EffectId { GRADIENT, NOODLE_ZOOM, WARP_TUNNEL }

private const val TAG = "Ultima/Shader"

private inline fun trySet(
    effect: EffectId,
    shader: RuntimeShader,
    name: String,
    set: () -> Unit
) {
    try { set() } catch (t: Throwable) {
        Log.e(TAG, "Uniform set failed [${effect.name}] $name -> ${t.message}", t)
        throw t
    }
}

@Composable
fun PortalCanvas(
    shader: RuntimeShader,
    effectId: EffectId,
    params: EffectParams,
    timeSeconds: Float,
) {
    Canvas(Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Shared uniforms
        trySet(effectId, shader, "resolution") { shader.setFloatUniform("resolution", w, h) }
        trySet(effectId, shader, "time")       { shader.setFloatUniform("time", timeSeconds) }
        trySet(effectId, shader, "uSpeed")     { shader.setFloatUniform("uSpeed", params.speed) }

        // Effect-specific (guarded!)
        if (effectId == EffectId.GRADIENT) {
            val baseLin = (params.base ?: Color(0.8f, 0.8f, 0.8f)).toLinear()
            val ampLin  = (params.amp  ?: Color(0.2f, 0.2f, 0.2f)).toLinear()
            val ph      =  params.phase ?: Triple(1f, 2f, 4f)

            trySet(effectId, shader, "uBase")  { shader.setFloatUniform("uBase",  baseLin[0], baseLin[1], baseLin[2]) }
            trySet(effectId, shader, "uAmp")   { shader.setFloatUniform("uAmp",   ampLin[0],  ampLin[1],  ampLin[2]) }
            trySet(effectId, shader, "uPhase") { shader.setFloatUniform("uPhase", ph.first, ph.second, ph.third) }
        }

        // Draw portal (replace with circle mask if desired)
        drawRect(ShaderBrush(shader))
    }
}
