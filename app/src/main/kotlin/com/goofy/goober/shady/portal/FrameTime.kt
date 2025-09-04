package com.goofy.goober.shady.portal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos

@Composable
fun rememberFrameSeconds(paused: Boolean = false): Float {
    var time by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(paused) {
        var lastNanos = 0L
        while (true) {
            withFrameNanos { nanos ->
                if (lastNanos == 0L) {
                    lastNanos = nanos
                }
                if (!paused) {
                    val delta = (nanos - lastNanos) / 1_000_000_000f
                    time += delta
                }
                lastNanos = nanos
            }
        }
    }
    return time
}

