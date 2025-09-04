package com.goofy.goober.shady.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.goofy.goober.shady.nav.Routes
import com.goofy.goober.shady.portal.PortalCanvas
import com.goofy.goober.shady.portal.PortalState
import com.goofy.goober.shady.portal.shaderFor

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PortalCanvas(shader = shaderFor(PortalState.effectId))
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate(Routes.Codex) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) { Text("Codex") }
            Button(
                onClick = { navController.navigate(Routes.Daemon) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) { Text("Daemon") }
            Button(
                onClick = { navController.navigate(Routes.Settings) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) { Text("Settings") }
        }
    }
}
