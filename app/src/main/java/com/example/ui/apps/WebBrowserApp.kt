package com.example.ui.apps

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebBrowserApp(
    initialUrl: String = "https://www.wikipedia.org",
    modifier: Modifier = Modifier
) {
    var currentUrl by remember { mutableStateOf(initialUrl) }
    var inputUrl by remember { mutableStateOf(initialUrl) }
    var webView: WebView? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var pageTitle by remember { mutableStateOf("Web Browser") }

    val bookmarks = listOf(
        Pair("Wikipedia", "https://www.wikipedia.org"),
        Pair("Photopea", "https://www.photopea.com"),
        Pair("VS Code", "https://vscode.dev"),
        Pair("DevDocs", "https://devdocs.io"),
        Pair("GitHub", "https://github.com"),
        Pair("Google", "https://www.google.com"),
        Pair("Excalidraw", "https://excalidraw.com")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1120))
    ) {
        // Browser Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .border(0.5.dp, Color(0x3360A5FA), RoundedCornerShape(0.dp))
                .padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (webView?.canGoBack() == true) webView?.goBack() },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                onClick = { if (webView?.canGoForward() == true) webView?.goForward() },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Forward",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                onClick = { webView?.reload() },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reload",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // URL Address Bar
            OutlinedTextField(
                value = inputUrl,
                onValueChange = { inputUrl = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "SSL",
                        tint = Color(0xFF4ADE80),
                        modifier = Modifier.size(13.dp)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            val target = formatUrl(inputUrl)
                            currentUrl = target
                            webView?.loadUrl(target)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Go",
                            tint = Color(0xFF60A5FA),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = {
                        val target = formatUrl(inputUrl)
                        currentUrl = target
                        webView?.loadUrl(target)
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x221E293B),
                    unfocusedContainerColor = Color(0x111E293B),
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color(0x3360A5FA),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("browser_address_bar")
            )
        }

        // Loading indicator
        if (isLoading) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = Color(0xFF3B82F6),
                trackColor = Color.Transparent
            )
        }

        // Bookmark Bar
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF020617))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(bookmarks) { (name, url) ->
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x221E293B))
                        .clickable {
                            inputUrl = url
                            currentUrl = url
                            webView?.loadUrl(url)
                        }
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = name,
                        tint = Color(0xFF60A5FA),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }

        // WebView Surface
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                            cacheMode = WebSettings.LOAD_DEFAULT
                            userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                        }
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                isLoading = true
                                url?.let {
                                    currentUrl = it
                                    inputUrl = it
                                }
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false
                                url?.let {
                                    currentUrl = it
                                    inputUrl = it
                                }
                            }
                        }
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                progress = newProgress / 100f
                            }

                            override fun onReceivedTitle(view: WebView?, title: String?) {
                                title?.let { pageTitle = it }
                            }
                        }
                        loadUrl(currentUrl)
                        webView = this
                    }
                },
                update = { view ->
                    if (view.url != currentUrl) {
                        view.loadUrl(currentUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun formatUrl(input: String): String {
    val trimmed = input.trim()
    return when {
        trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
        trimmed.contains(".") && !trimmed.contains(" ") -> "https://$trimmed"
        else -> "https://www.google.com/search?q=" + java.net.URLEncoder.encode(trimmed, "UTF-8")
    }
}
