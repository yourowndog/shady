package com.goofy.goober.shady.portal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class EffectParams(val speed: Float = 0.5f)

object PortalState {
    var effectId: String = "WARP_TUNNEL"
    var params by mutableStateOf(EffectParams())
}
