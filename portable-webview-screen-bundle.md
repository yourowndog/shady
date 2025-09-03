# Portable WebView Screen Bundle

**Source file path(s):**
- apps/daemon-portal/app/src/main/java/com/daemon/portal/ui/DesktopWebViewScreen.kt

**Kotlin version:** 2.0.20

## Kotlin File (sanitized)

```kotlin
package PORTABLE.REPLACE.ME.feature.web

import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesktopWebViewScreen(
    url: String,
    title: String,
    onNavigateUp: () -> Unit,
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var progress by remember { mutableStateOf(0) }
    var errorUrl by remember { mutableStateOf<String?>(null) }

    BackHandler {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            onNavigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    TextButton(onClick = onNavigateUp) { Text("Back") }
                },
                actions = {
                    TextButton(onClick = { webView?.reload() }) { Text("Reload") }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (errorUrl != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Failed to load $errorUrl")
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = {
                        errorUrl = null
                        webView?.loadUrl(url)
                    }) { Text("Retry") }
                }
            } else {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        WebView(ctx).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.useWideViewPort = true
                            settings.loadWithOverviewMode = true
                            settings.userAgentString =
                                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"
                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    progress = newProgress
                                }
                            }
                            webViewClient = object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                ): Boolean {
                                    val target = request?.url ?: return false
                                    val scheme = target.scheme
                                    return if (scheme == "http" || scheme == "https") {
                                        false
                                    } else {
                                        try {
                                            ctx.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, target))
                                        } catch (_: Exception) {
                                        }
                                        true
                                    }
                                }

                                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                                    progress = 0
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    progress = 100
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?,
                                ) {
                                    if (request?.isForMainFrame == true) {
                                        errorUrl = request.url.toString()
                                    }
                                }
                            }
                            loadUrl(url)
                            webView = this
                        }
                    },
                    update = { it.loadUrl(url) },
                )
            }

            if (progress < 100) {
                LinearProgressIndicator(
                    progress = progress / 100f,
                    modifier = Modifier
   
                     .fillMaxWidth()
                        .align(Alignment.TopCenter),
                )
            }
        }
    }
}
```

