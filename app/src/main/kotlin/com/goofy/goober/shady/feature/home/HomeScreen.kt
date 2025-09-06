package com.goofy.goober.shady.feature.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.goofy.goober.shady.nav.Routes
import com.goofy.goober.shady.portal.PortalCanvas
import com.goofy.goober.shady.portal.PortalState
import com.goofy.goober.shady.portal.shaderFor
import com.goofy.goober.sketch.produceDrawLoopCounter

@Composable
fun HomeScreen(navController: NavController) {
    val params = PortalState.params
    val time by produceDrawLoopCounter()
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ULTIMA",
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 36.sp,
                letterSpacing = 2.sp,
            )
            Spacer(modifier = Modifier.height(32.dp))
            PortalCanvas(
                shader = shaderFor(PortalState.effectId),
                effectId = PortalState.effectId,
                params = params,
                timeSeconds = time
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                NavText(label = "codex") {
                    navController.navigate(Routes.Codex)
                }
                Text(
                    text = " · ",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 1.5.sp,
                )
                NavText(label = "daemon") {
                    navController.navigate(Routes.Daemon)
                }
                Text(
                    text = " · ",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 1.5.sp,
                )
                NavText(label = "settings") {
                    navController.navigate(Routes.Settings)
                }
            }
        }
    }
}

@Composable
private fun NavText(label: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val targetAlpha = if (pressed) 0.4f else 0.7f
    val alpha by animateFloatAsState(targetValue = targetAlpha, label = "navAlpha")

    Text(
        text = label,
        modifier = Modifier
            .semantics { contentDescription = label }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = alpha),
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 1.5.sp,
    )
}
