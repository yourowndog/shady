package com.goofy.goober.shady.portal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.withFrameNanos

@Composable
fun rememberFrameSeconds(): State<Float> {
    return produceState(0f) {
        var start = 0L
        while (true) {
            withFrameNanos { now ->
                if (start == 0L) start = now
                value = (now - start) / 1_000_000_000f
            }
        }
    }
}
