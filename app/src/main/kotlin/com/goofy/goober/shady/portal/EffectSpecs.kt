package com.goofy.goober.shady.portal

import android.graphics.RuntimeShader
import androidx.compose.ui.geometry.Size

data class Param(val key: String, val label: String, val min: Float, val max: Float, val default: Float)

data class EffectSpec(
    val id: String,
    val params: List<Param>,
    val apply: (shader: RuntimeShader, params: Map<String, Float>, sizePx: Size, timeSec: Float) -> Unit
) {
    fun defaults(): MutableMap<String, Float> =
        params.associate { it.key to it.default }.toMutableMap()
}

object Effects {
    val WARP_TUNNEL = EffectSpec(
        id = "WARP_TUNNEL",
        params = listOf(
            Param("speed", "Speed", 0f, 2f, 1.0f)
        ),
        apply = { shader, params, size, timeSec ->
            shader.setFloatUniform("resolution", size.width, size.height)
            shader.setFloatUniform("time", timeSec * (params["speed"] ?: 1f))
        }
    )

    val NOODLE_ZOOM = EffectSpec(
        id = "NOODLE_ZOOM",
        params = listOf(
            Param("speed", "Speed", 0f, 2f, 1.0f)
        ),
        apply = { shader, params, size, timeSec ->
            shader.setFloatUniform("resolution", size.width, size.height)
            shader.setFloatUniform("time", timeSec * (params["speed"] ?: 1f))
        }
    )

    val GRADIENT_FIELD = EffectSpec(
        id = "GRADIENT_FIELD",
        params = listOf(
            Param("speed", "Speed", 0f, 2f, 1.0f)
        ),
        apply = { shader, params, size, timeSec ->
            shader.setFloatUniform("resolution", size.width, size.height)
            shader.setFloatUniform("time", timeSec * (params["speed"] ?: 1f))
        }
    )

    fun specFor(id: String) = when (id) {
        "WARP_TUNNEL" -> WARP_TUNNEL
        "NOODLE_ZOOM" -> NOODLE_ZOOM
        "GRADIENT_FIELD" -> GRADIENT_FIELD
        else -> GRADIENT_FIELD
    }
}

