package com.goofy.goober.shady.portal

import android.graphics.RuntimeShader

private val shaderCache = mutableMapOf<EffectId, RuntimeShader>()

fun shaderFor(effect: EffectId): RuntimeShader {
    return shaderCache.getOrPut(effect) {
        val src = when (effect) {
            EffectId.GRADIENT -> gradientSrc
            EffectId.NOODLE_ZOOM -> noodleSrc
            EffectId.WARP_TUNNEL -> warpSrc
        }
        ShaderFactory.create(effect, src)
    }
}

private val gradientSrc = """
uniform float2 resolution;
uniform float  time;
uniform float  uSpeed;

// palette uniforms (no initializers in AGSL)
uniform float3 uBase;    // e.g., (0.8, 0.8, 0.8) from Kotlin
uniform float3 uAmp;     // e.g., (0.2, 0.2, 0.2) from Kotlin
uniform float3 uPhase;   // e.g., (1.0, 2.0, 4.0) from Kotlin

half4 main(float2 fragCoord) {
    float speedScale = mix(0.1, 2.0, clamp(uSpeed, 0.0, 1.0));
    float t2 = time * speedScale;
    float2 uv = fragCoord / resolution.xy;

    float3 col = uBase + uAmp * cos(t2 * 2.0 + uv.xxx * 2.0 + uPhase);
    return half4(col, 1.0);
}
""".trimIndent()

private val noodleSrc = """
uniform float2 resolution;
uniform float  time;
uniform float  uSpeed;   // 0..1
float speedScale = mix(0.1, 2.0, clamp(uSpeed, 0.0, 1.0));
float t2 = time * speedScale;   // scaled time

// Source: @notargs https://twitter.com/notargs/status/1250468645030858753
float f(vec3 p) {
    p.z -= t2 * 10.;
    float a = p.z * .1;
    p.xy *= mat2(cos(a), sin(a), -sin(a), cos(a));
    return .1 - length(cos(p.xy) + sin(p.yz));
}

half4 main(vec2 fragcoord) {
    vec3 d = .5 - fragcoord.xy1 / resolution.y;
    vec3 p=vec3(0);
    for (int i = 0; i < 32; i++) {
      p += f(p) * d;
    }
    return ((sin(p) + vec3(2, 5, 12)) / length(p)).xyz1;
}
""".trimIndent()

private val warpSrc = """
        // 'Warp Speed 2'
        // David Hoskins 2015.
        // License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.

        // Fork of:-   https://www.shadertoy.com/view/Msl3WH
        //----------------------------------------------------------------------------------------
        uniform float2 resolution;      // Viewport resolution (pixels)
        uniform float  time;            // Shader playback time (s)
        uniform float uSpeed;  // 0..1

        vec4 main( in float2 fragCoord )
        {
            float s = 0.0, v = 0.0;
            vec2 uv = (fragCoord / resolution.xy) * 2.0 - 1.;
            float t = (time-2.0)*58.0;
            float speedScale = mix(0.1, 2.0, clamp(uSpeed, 0.0, 1.0));
            t *= speedScale;
            vec3 col = vec3(0);
            vec3 init = vec3(sin(t * .0032)*.3, .35 - cos(t * .005)*.3, t * 0.002);
            for (int r = 0; r < 100; r++)
            {
                vec3 p = init + s * vec3(uv, 0.05);
                p.z = fract(p.z);
                // Thanks to Kali's little chaotic loop...
                for (int i=0; i < 10; i++)      p = abs(p * 2.04) / dot(p, p) - .9;
                v += pow(dot(p, p), .7) * .06;
                col +=  vec3(v * 0.2+.4, 12.-s*2., .1 + v * 1.) * v * 0.00003;
                s += .025;
            }
            return vec4(clamp(col, 0.0, 1.0), 1.0);
        }
""".trimIndent()
