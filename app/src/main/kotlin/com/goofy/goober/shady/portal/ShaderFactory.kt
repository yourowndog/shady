package com.goofy.goober.shady.portal

import android.graphics.RuntimeShader
import android.util.Log

object ShaderFactory {
    private const val TAG = "Ultima/Shader"

    fun create(effect: EffectId, src: String): RuntimeShader {
        return try {
            RuntimeShader(src)
        } catch (t: Throwable) {
            Log.e(TAG, "Shader compile failed [${effect.name}]: ${t.message}", t)
            throw t
        }
    }
}
