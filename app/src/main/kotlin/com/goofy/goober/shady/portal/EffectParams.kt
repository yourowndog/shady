package com.goofy.goober.shady.portal

import androidx.compose.ui.graphics.Color

data class EffectParams(
    val speed: Float = 0.5f,
    // Gradient-only palette (null = use defaults)
    val base: Color? = null,    // default (0.8, 0.8, 0.8)
    val amp: Color? = null,     // default (0.2, 0.2, 0.2)
    val phase: Triple<Float, Float, Float>? = null // default (1, 2, 4)
)
