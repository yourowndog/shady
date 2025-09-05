package com.goofy.goober.shady.portal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

data class EffectParams(
    val speed: Float = 0.5f,
    val base: Color? = null,
    val amp: Color? = null,
    val phase: Triple<Float, Float, Float>? = null,
)

object PortalState {
    var effectId: String = "WARP_TUNNEL"
    var params by mutableStateOf(EffectParams())
}
