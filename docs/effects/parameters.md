# Effects Parameter Recon

## GradientShader
**Files:** shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt, app/src/main/kotlin/com/goofy/goober/shady/portal/PortalCanvas.kt

### A. Current inputs (list, exact)
- **Uniforms:**
  - `resolution: float2` – output size【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L10-L10】
  - `time: float` – animation clock【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L11-L11】
  - `uSpeed: float` – speed control (0..1)【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L12-L12】
- **Constants:**
  - speed range `mix(0.1, 2.0, …)`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L13-L13】
  - color base `0.8` & amp `0.2`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L21-L21】
  - color freq `2.0` (time & uv multipliers)【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L21-L21】
- **Derived values:**
  - `speedScale` – mixes speed range【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L13-L13】
  - `t2` – scaled time【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L14-L14】
  - `uv` – normalized coords【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L18-L18】

### B. Candidate slider params (ranked)
| Param | Source (uniform/const/derived) | Proposed uniform name | Suggested range | UI control | Mapping notes |
|---|---|---|---|---|---|
| Speed | uniform `uSpeed` | `uSpeed` (already) | 0..1 | Slider | `mix(0.1,2.0,uSpeed)`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L13-L14】|
| Color frequency | const `2.0` in `t2*2.0` | `uColorFreq` | 0.5..4 (uncertain) | Slider | multiplies time term |
| Color amplitude | const `0.2` | `uColorAmp` | 0..0.5 | Slider | scales cosine output |

### C. Color analysis
- **Color sources:** analytic cosine palette with phase offsets `vec3(1,2,4)`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L21-L21】
- **Option 1 — Global hue shift:** single `uHue` uniform; minimal but cos palette may clip.
- **Option 2 — Palette uniforms:** expose base & amp colors; more control.
- **Recommend:** Option 1 for simple global styling; effect already procedural.

### D. Minimal insertion points (exact)
```glsl
uniform float2 resolution;
uniform float  time;
uniform float  uSpeed;   // 0..1
float speedScale = mix(0.1, 2.0, clamp(uSpeed, 0.0, 1.0));
float t2 = time * speedScale;   // scaled time
// INSERT uColorFreq here

vec4 main(vec2 fragCoord) {
    vec2 uv = fragCoord/resolution.xy;
    vec3 col = 0.8 + 0.2 * cos(t2*uColorFreq+uv.xxx*2.0+vec3(1,2,4)); // INSERT uColorAmp
    return vec4(col,1.0);
}
```

### E. Risk & perf notes
- Extra uniforms trivial; color frequency >4 may alias.

## NoodleZoomShader
**Files:** shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt, app/src/main/kotlin/com/goofy/goober/shady/portal/PortalCanvas.kt

### A. Current inputs (list, exact)
- **Uniforms:** `resolution: float2`, `time: float`, `uSpeed: float`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L34-L37】
- **Constants:**
  - zoom rate `10.` in `p.z -= t2 * 10.`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L42-L42】
  - rotation factor `.1`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L43-L43】
  - loop count `32`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L51-L51】
  - color offset `vec3(2,5,12)`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L54-L54】
- **Derived values:** `speedScale`, `t2`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L37-L38】

### B. Candidate slider params (ranked)
| Param | Source (uniform/const/derived) | Proposed uniform name | Suggested range | UI control | Mapping notes |
|---|---|---|---|---|---|
| Speed | uniform `uSpeed` | `uSpeed` (already) | 0..1 | Slider | `mix(0.1,2.0,uSpeed)`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L37-L38】|
| Zoom rate | const `10.` | `uZoomRate` | 2..20 | Slider | linear scale on `p.z` |
| Iterations | loop `32` | `uDepth` | 16..64 | Slider (discrete) | affects perf |

### C. Color analysis
- **Color sources:** mix of `sin(p)` and fixed `vec3(2,5,12)` offset【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L54-L54】
- **Option 1 — Global hue shift:** feasible; palette is analytic.
- **Option 2 — Palette uniforms:** expose offset color.
- **Recommend:** Option 2 for stylistic control over base palette.

### D. Minimal insertion points (exact)
```glsl
float f(vec3 p) {
    p.z -= t2 * uZoomRate;          // INSERT uZoomRate here
    float a = p.z * .1;
    p.xy *= mat2(cos(a), sin(a), -sin(a), cos(a));
    return .1 - length(cos(p.xy) + sin(p.yz));
}

half4 main(vec2 fragcoord) {
    vec3 d = .5 - fragcoord.xy1 / resolution.y;
    vec3 p=vec3(0);
    for (int i = 0; i < uDepth; i++) {   // INSERT uDepth here
        p += f(p) * d;
    }
    vec3 base = vec3(2, 5, 12); // INSERT uColorA/B
    return ((sin(p) + base) / length(p)).xyz1;
}
```

### E. Risk & perf notes
- Raising `uDepth` increases 3D loop cost; zoom rate too high may skip detail.

## WarpSpeedShader
**Files:** shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt, app/src/main/kotlin/com/goofy/goober/shady/portal/PortalCanvas.kt

### A. Current inputs (list, exact)
- **Uniforms:** `resolution: float2`, `time: float`, `uSpeed: float`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L70-L72】
- **Constants:**
  - time scale `(time-2.0)*58.0`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L78-L79】
  - inner loop `r < 100`, chaotic loop `i < 10`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L83-L88】
  - turbulence mult `2.04`, offset `-.9`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L88-L88】
- **Derived values:** `speedScale`, scaled `t`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L79-L80】

### B. Candidate slider params (ranked)
| Param | Source | Proposed uniform name | Suggested range | UI control | Mapping notes |
|---|---|---|---|---|---|
| Speed | uniform `uSpeed` | `uSpeed` (already) | 0..1 | Slider | scales `t`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L79-L80】|
| Warp depth | loop `r < 100` | `uDepth` | 50..150 | Slider (discrete) | strong perf impact |
| Turbulence | const `2.04` | `uTurbulence` | 1.0..3.0 | Slider | multiplies chaotic loop |

### C. Color analysis
- **Color sources:** analytic mix of `v` and `s` with constants `0.2`, `.4`, `12.` etc【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L90-L90】
- **Option 1 — Global hue shift:** minimal and consistent.
- **Option 2 — Palette uniforms:** would require exposing multiple coefficients.
- **Recommend:** Option 1 for simplicity.

### D. Minimal insertion points (exact)
```glsl
float t = (time-2.0)*58.0;
float speedScale = mix(0.1, 2.0, clamp(uSpeed, 0.0, 1.0));
t *= speedScale;
// INSERT uTimeScale here if separate from speed
vec3 init = vec3(sin(t * .0032)*.3, .35 - cos(t * .005)*.3, t * 0.002);
for (int r = 0; r < uDepth; r++) {    // INSERT uDepth here
    vec3 p = init + s * vec3(uv, 0.05);
    p.z = fract(p.z);
    for (int i=0; i < 10; i++) p = abs(p * uTurbulence) / dot(p, p) - .9; // INSERT uTurbulence
    ...
}
```

### E. Risk & perf notes
- `uDepth` dramatically affects frame time.
- High turbulence may introduce NaNs if `dot(p,p)` → 0.

## LightScatteringShader
**Files:** shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt

### A. Current inputs (list, exact)
- **Uniforms:** `resolution: float2`, `time: float`, `iMouse: float2`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L104-L106】
- **Constants:** `fov=tan(radians(60.0))`, `cameraheight=5e1`, `Gamma=2.2`, `Rayleigh=1.`, `Mie=1.`, `RayleighAtt=1.`, `MieAtt=1.2`, `g=-0.9`【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L111-L120】
- **Derived values:** `_betaR`, `_betaM`, `ts`, etc.【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L121-L126】

### B. Candidate slider params (ranked)
| Param | Source | Proposed uniform name | Suggested range | UI control | Mapping notes |
|---|---|---|---|---|---|
| Anisotropy | const `g` | `uG` | -0.99..0.99 | Slider | impacts forward/back scatter |
| Rayleigh scale | const `Rayleigh` | `uRayleigh` | 0..2 | Slider | linear multiply |
| Gamma | const `2.2` | `uGamma` | 1..3 | Slider | post-color curve |

### C. Color analysis
- **Color sources:** physically-based scattering constants (`_betaR`, `_betaM`)【F:shaders/src/main/kotlin/com/goofy/goober/shaders/Animated.kt†L121-L123】
- **Option 1 — Global hue shift:** may disrupt realism.
- **Option 2 — Palette uniforms:** expose `_betaR`/`_betaM` for sky tint.
- **Recommend:** Option 2 to preserve physical parameterization.

### D. Minimal insertion points (exact)
```glsl
const float Rayleigh = uRayleigh;  // INSERT uRayleigh
const float Mie = 1.;
float g = uG;                      // INSERT uG
vec3 _betaR = vec3(1.95e-2, 1.1e-1, 2.94e-1);
const float Gamma = uGamma;        // INSERT uGamma
```

### E. Risk & perf notes
- Changing scattering constants alters lighting balance; extreme g near ±1 can cause banding.

---

### Global Section: Color picker UI plan
1. **State model:** extend `EffectParams` with either `hue: Float` or `colorA: Color`, `colorB: Color`.
2. **UI control:** use Material hue slider with optional saturation/value picker.
3. **Uniform wiring:**
   - Option 1: `shader.setFloatUniform("uHue", params.hue)`.
   - Option 2: `shader.setFloatUniform("uColorA", r,g,b)` and same for `uColorB`.
4. **Color space:** convert Compose `Color` from sRGB to linear before sending; AGSL expects 0–1 floats.
5. **Backwards-compat:** defaults: `hue=0f` or colors null -> existing visuals.

---

### Next candidates
- **GradientShader:** Speed, Color frequency — using global hue shift.
- **NoodleZoomShader:** Zoom rate, Iterations — prefer palette uniforms.
- **WarpSpeedShader:** Warp depth, Turbulence — use global hue shift.
- **LightScatteringShader:** Anisotropy, Rayleigh scale — expose scattering palette.
