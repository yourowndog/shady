package com.goofy.goober.shady.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.goofy.goober.shady.portal.PortalCanvas
import com.goofy.goober.shady.portal.shaderFor

@Composable
fun EffectEditorScreen(
    effectId: String,
    onUse: () -> Unit,
    onBack: () -> Unit
) {
    val shader = shaderFor(effectId)
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
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PortalCanvas(shader = shader)
        }
    }
}
