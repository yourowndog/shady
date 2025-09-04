package com.goofy.goober.shaders

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RuntimeShader

fun RuntimeShader.setResolution(size: Size) {
    setFloatUniform("resolution", size.width, size.height)
}

fun RuntimeShader.setTime(time: Float) {
    setFloatUniform("time", time)
}

fun RuntimeShader.setMouse(offset: Offset) {
    setFloatUniform("iMouse", offset.x, offset.y)
}

