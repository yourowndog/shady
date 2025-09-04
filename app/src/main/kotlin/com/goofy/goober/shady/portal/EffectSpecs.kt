package com.goofy.goober.shady.portal

import androidx.compose.ui.graphics.RuntimeShader

data class Param(val key: String, val label: String, val min: Float, val max: Float, val default: Float)

data class EffectSpec(
    val runtimeId: String,
    val params: List<Param>,
    val apply: (RuntimeShader, Map<String, Float>) -> Unit
) {
    fun defaults(): MutableMap<String, Float> =
        params.associate { it.key to it.default }.toMutableMap()
}

object Effects {
    val WARP_TUNNEL = EffectSpec(
        runtimeId = "WARP_TUNNEL",
        params = listOf(
            Param("speed", "Speed", 0f, 2f, 1.0f)
        ),
        apply = { _, _ -> }
    )

    val NOODLE_ZOOM = EffectSpec(
        runtimeId = "NOODLE_ZOOM",
        params = listOf(
            Param("speed", "Speed", 0f, 2f, 1.0f)
        ),
        apply = { _, _ -> }
    )

    val GRADIENT_FIELD = EffectSpec(
        runtimeId = "GRADIENT_FIELD",
        params = listOf(
            Param("speed", "Speed", 0f, 2f, 1.0f)
        ),
        apply = { _, _ -> }
    )

    fun specFor(id: String) = when (id) {
        "WARP_TUNNEL" -> WARP_TUNNEL
        "NOODLE_ZOOM" -> NOODLE_ZOOM
        "GRADIENT_FIELD" -> GRADIENT_FIELD
        else -> GRADIENT_FIELD
    }
}
