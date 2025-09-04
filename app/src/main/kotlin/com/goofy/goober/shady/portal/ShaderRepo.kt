package com.goofy.goober.shady.portal

import android.graphics.RuntimeShader
import com.goofy.goober.shaders.GradientShader
import com.goofy.goober.shaders.NoodleZoomShader
import com.goofy.goober.shaders.WarpSpeedShader

fun shaderFor(effectId: String): RuntimeShader = when (effectId) {
    "NOODLE_ZOOM" -> NoodleZoomShader
    "GRADIENT_FIELD" -> GradientShader
    else -> WarpSpeedShader
}
