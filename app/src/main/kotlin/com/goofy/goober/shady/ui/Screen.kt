package com.goofy.goober.shady.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.goofy.goober.style.LargeCard
import com.goofy.goober.style.Space

sealed interface Screen {
    val title: String
    val description: String?
}

data class DestinationScreen(
    override val title: String,
    override val description: String? = null,
    val content: @Composable () -> Unit
) : Screen

fun NavGraphBuilder.nestedContent(
    onNavigate: (Screen) -> Unit,
    screens: List<DestinationScreen>,
    home: String
) {
    composable(home) {
        List(
            screens = screens,
            onClick = { screen ->
                onNavigate(screen)
            }
        )
    }
    screens.forEach { screen ->
        composable(screen.title) { screen.content() }
    }
}

@Composable
fun List(
    screens: List<Screen>,
    onClick: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .wrapContentHeight()
            .padding(Space.Three),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Spacer(Modifier.height(Space.Four))
        }
        items(screens, key = { it.title }) { screen ->
            LargeCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Space.Five),
                title = screen.title,
                subtitle = screen.description,
                onClick = { onClick(screen) }
            )
            Spacer(Modifier.height(Space.Three))
        }
        item {
            Spacer(Modifier.height(Space.Twelve))
        }
    }
}
