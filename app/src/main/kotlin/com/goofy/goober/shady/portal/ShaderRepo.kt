package com.goofy.goober.shady.portal

import androidx.compose.ui.graphics.RuntimeShader
import com.goofy.goober.shaders.GradientShader
import com.goofy.goober.shaders.NoodleZoomShader
import com.goofy.goober.shaders.WarpSpeedShader

object ShaderRepo {
    fun get(id: String): RuntimeShader = when (id) {
        "NOODLE_ZOOM" -> NoodleZoomShader
        "GRADIENT_FIELD" -> GradientShader
        else -> WarpSpeedShader
    }
}
