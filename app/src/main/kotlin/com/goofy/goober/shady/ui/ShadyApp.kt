package com.goofy.goober.shady.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.goofy.goober.shady.nav.Routes
import com.goofy.goober.shady.feature.home.HomeScreen
import com.goofy.goober.shady.feature.settings.EffectEditorScreen
import com.goofy.goober.shady.feature.settings.EffectListScreen
import com.goofy.goober.shady.feature.web.DesktopWebViewScreen
import com.goofy.goober.shady.animated.animatedShadersGraph
import com.goofy.goober.shady.static.textureShadersGraph
import com.goofy.goober.style.ShadyTheme

@Composable
fun ShadyApp() {
    ShadyTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.Home) {
            composable(Routes.Home) { HomeScreen(navController) }
            composable(Routes.Codex) {
                DesktopWebViewScreen(
                    url = "https://chatgpt.com/codex",
                    title = "Codex",
                    onNavigateUp = { navController.popBackStack() }
                )
            }
            composable(Routes.Daemon) {
                DesktopWebViewScreen(
                    url = "https://chatgpt.com/daemon",
                    title = "Daemon",
                    onNavigateUp = { navController.popBackStack() }
                )
            }
            composable(Routes.Settings) {
                EffectListScreen(
                    onOpen = { id -> navController.navigate("effect_editor/$id") },
                    onBack = { navController.navigateUp() }
                )
            }
            composable("effect_editor/{id}") { backStack ->
                val id = backStack.arguments?.getString("id")!!
                EffectEditorScreen(
                    effectId = id,
                    onUse = { navController.navigateUp() },
                    onBack = { navController.navigateUp() }
                )
            }
            textureShadersGraph { navController.navigate(it.title) }
            animatedShadersGraph { navController.navigate(it.title) }
        }
    }
}
