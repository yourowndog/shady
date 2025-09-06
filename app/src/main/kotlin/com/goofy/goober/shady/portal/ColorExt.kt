package com.goofy.goober.shady.portal

import androidx.compose.ui.graphics.Color
import kotlin.math.pow

fun Color.toLinear(): FloatArray {
    fun c(c: Float) = if (c <= 0.04045f) c / 12.92f else ((c + 0.055f) / 1.055f).pow(2.4f)
    return floatArrayOf(c(red), c(green), c(blue))
}
