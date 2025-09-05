package com.goofy.goober.shady.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.goofy.goober.shady.portal.PortalCanvas
import com.goofy.goober.shady.portal.PortalState
import com.goofy.goober.shady.portal.shaderFor

@Composable
fun EffectEditorScreen(
    effectId: String,
    onUse: () -> Unit,
    onBack: () -> Unit
) {
    val shader = shaderFor(effectId)
    val params = PortalState.params
    var tempSpeed by remember { mutableStateOf(params.speed) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(effectId) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
                actions = {
                    TextButton(onClick = onUse) { Text("Use") }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            PortalCanvas(shader = shader, params = params)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("speed", fontFamily = FontFamily.Monospace)
                Slider(
                    value = tempSpeed,
                    onValueChange = { tempSpeed = it },
                    valueRange = 0f..1f,
                    onValueChangeFinished = {
                        PortalState.params = PortalState.params.copy(speed = tempSpeed)
                    }
                )
            }
        }
    }
}
