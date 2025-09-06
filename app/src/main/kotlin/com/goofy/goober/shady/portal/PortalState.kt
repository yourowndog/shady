package com.goofy.goober.shady.portal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object PortalState {
    var effectId: EffectId = EffectId.WARP_TUNNEL
    var params by mutableStateOf(EffectParams())
}
