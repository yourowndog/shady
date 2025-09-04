package com.goofy.goober.shady.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.goofy.goober.shady.portal.Effects
import com.goofy.goober.shady.portal.PortalCanvas
import com.goofy.goober.shady.portal.PortalState
import com.goofy.goober.shady.portal.shaderFor

@Composable
fun EffectEditorScreen(
    effectId: String,
    onUse: () -> Unit,
    onBack: () -> Unit,
) {
    val spec = Effects.specFor(effectId)
    val params = remember(spec) {
        mutableStateMapOf<String, Float>().apply {
            putAll(PortalState.paramsByEffect[effectId] ?: spec.defaults())
        }
    }
    val shader = remember(effectId) { shaderFor(effectId) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(effectId) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
                actions = {
                    TextButton(onClick = {
                        PortalState.effectId = effectId
                        PortalState.paramsByEffect[effectId] = params.toMutableMap()
                        onUse()
                    }) { Text("Use") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PortalCanvas(
                shader = shader,
                onFrame = { s, size, t -> spec.apply(s, params, size, t) }
            )
            spec.params.forEach { param ->
                val value = params[param.key] ?: param.default
                Text("${param.label}: ${String.format("%.2f", value)}")
                Slider(
                    value = value,
                    onValueChange = { params[param.key] = it },
                    valueRange = param.min..param.max
                )
            }
        }
    }
}

