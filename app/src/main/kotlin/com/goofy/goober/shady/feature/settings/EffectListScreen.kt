package com.goofy.goober.shady.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.goofy.goober.shady.portal.EffectId

@Composable
fun EffectListScreen(
    onOpen: (EffectId) -> Unit,
    onBack: () -> Unit
) {
    val effects = listOf(EffectId.WARP_TUNNEL, EffectId.NOODLE_ZOOM, EffectId.GRADIENT)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Effects") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            effects.forEach { effect ->
                Button(
                    onClick = { onOpen(effect) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) { Text(effect.name) }
            }
        }
    }
}
