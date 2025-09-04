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
    val spec = remember(effectId) { Effects.specFor(effectId) }
    val local = remember(spec) {
        mutableStateMapOf<String, Float>().apply {
            spec.params.forEach { put(it.key, it.default) }
            PortalState.paramsByEffect[effectId]?.let { putAll(it) }
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
                        PortalState.paramsByEffect[effectId] = local.toMutableMap()
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
                onFrame = { s, size, time -> spec.apply(s, local, size, time) }
            )
            spec.params.forEach { param ->
                val value = local[param.key] ?: param.default
                Text("${param.label}: ${String.format("%.2f", value)}")
                Slider(
                    value = value,
                    onValueChange = { local[param.key] = it },
                    valueRange = param.min..param.max
                )
            }
        }
    }
}

