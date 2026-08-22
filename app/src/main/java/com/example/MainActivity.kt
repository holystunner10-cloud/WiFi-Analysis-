package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiChannel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.WifiAnalyzerViewModel
import com.example.ui.screens.ChannelSpectrumScreen
import com.example.ui.screens.ConnectionOverviewScreen
import com.example.ui.screens.DiagnosticsScreen
import com.example.ui.screens.NetworksScannerScreen
import com.example.ui.theme.DarkSlateCard
import com.example.ui.theme.LightBorder
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.theme.VibrantBlue
import com.example.ui.theme.VibrantBlueLight
import com.example.ui.theme.VibrantBlueSoft
import com.example.ui.theme.VibrantGreen
import com.example.ui.theme.VibrantGreenLight
import com.example.ui.theme.VibrantOrange
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: WifiAnalyzerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                WifiAnalyzerApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun WifiAnalyzerApp(viewModel: WifiAnalyzerViewModel) {
    val context = LocalContext.current

    val selectedTab by viewModel.selectedTab.collectAsState()
    val connectionInfo by viewModel.connectionInfo.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    val signalHistory by viewModel.signalHistory.collectAsState()
    val filteredNetworks by viewModel.filteredNetworks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedBandFilter by viewModel.selectedBandFilter.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val selectedSpectrumBand by viewModel.selectedSpectrumBand.collectAsState()
    val channels24G by viewModel.channels24GHz.collectAsState()
    val channels5G by viewModel.channels5GHz.collectAsState()
    val pingResults by viewModel.pingResults.collectAsState()
    val lanDevices by viewModel.lanDevices.collectAsState()
    val isLanScanning by viewModel.isLanScanning.collectAsState()
    val lanScanProgress by viewModel.lanScanProgress.collectAsState()
    val isSimulationActive by viewModel.isSimulationActive.collectAsState()

    var hasLocationPermission by remember {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        mutableStateOf(fineLocation)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission = granted
        viewModel.setPermissionsGranted(granted)
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            val perms = mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                perms.add(Manifest.permission.NEARBY_WIFI_DEVICES)
            }
            permissionLauncher.launch(perms.toTypedArray())
        } else {
            viewModel.setPermissionsGranted(true)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            WifiTopAppBar(
                ssid = connectionInfo.ssid,
                rssi = connectionInfo.rssi,
                isScanning = isScanning,
                isSimulationActive = isSimulationActive,
                onToggleSimulation = { viewModel.toggleSimulationMode() },
                onRefresh = { viewModel.refreshAll() }
            )
        },
        bottomBar = {
            WifiBottomNavigation(
                selectedTab = selectedTab,
                onSelectTab = { viewModel.setSelectedTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> ConnectionOverviewScreen(
                    connectionInfo = connectionInfo,
                    signalHistory = signalHistory,
                    onRunPing = {
                        viewModel.setSelectedTab(3)
                        viewModel.runPingDiagnostics()
                    },
                    onViewChannels = {
                        viewModel.setSelectedTab(1)
                    }
                )

                1 -> ChannelSpectrumScreen(
                    selectedBand = selectedSpectrumBand,
                    onSelectBand = { viewModel.setSpectrumBand(it) },
                    networks = filteredNetworks,
                    channels24G = channels24G,
                    channels5G = channels5G
                )

                2 -> NetworksScannerScreen(
                    networks = filteredNetworks,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                    selectedBandFilter = selectedBandFilter,
                    onBandFilterChange = { viewModel.setBandFilter(it) },
                    sortOption = sortOption,
                    onSortOptionChange = { viewModel.setSortOption(it) },
                    isScanning = isScanning,
                    onTriggerScan = { viewModel.refreshAll() }
                )

                3 -> DiagnosticsScreen(
                    connectionInfo = connectionInfo,
                    pingResults = pingResults,
                    lanDevices = lanDevices,
                    isLanScanning = isLanScanning,
                    lanScanProgress = lanScanProgress,
                    onRunPing = { viewModel.runPingDiagnostics() },
                    onScanLan = { viewModel.runLanDeviceScan() }
                )
            }
        }
    }
}

@Composable
fun WifiTopAppBar(
    ssid: String,
    rssi: Int,
    isScanning: Boolean,
    isSimulationActive: Boolean,
    onToggleSimulation: () -> Unit,
    onRefresh: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .testTag("app_top_bar"),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // App Title & Connected Pill
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(VibrantBlueSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Sensors,
                        contentDescription = "App Logo",
                        tint = VibrantBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "WiFi Analyzer",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(VibrantGreen, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "$ssid ($rssi dBm)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = VibrantBlue
                        )
                    }
                }
            }

            // Top Action Controls (Demo Mode & Refresh)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Demo Mode Switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = "Demo",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSimulationActive) VibrantBlue else Slate500
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Switch(
                        checked = isSimulationActive,
                        onCheckedChange = { onToggleSimulation() },
                        modifier = Modifier.size(width = 38.dp, height = 24.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = VibrantBlue,
                            uncheckedThumbColor = Slate400,
                            uncheckedTrackColor = LightBorder
                        )
                    )
                }

                // Refresh Button
                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .size(40.dp)
                        .background(VibrantBlueSoft, CircleShape)
                        .testTag("btn_refresh_scan")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Scan",
                        tint = VibrantBlue,
                        modifier = Modifier
                            .size(20.dp)
                            .then(if (isScanning) Modifier.rotate(rotation) else Modifier)
                    )
                }
            }
        }
    }
}

@Composable
fun WifiBottomNavigation(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        val items = listOf(
            Triple(0, "Dashboard", Icons.Default.Wifi),
            Triple(1, "Spectrum", Icons.Default.GraphicEq),
            Triple(2, "Networks", Icons.Default.List),
            Triple(3, "Diagnostics", Icons.Default.Speed)
        )

        items.forEach { (index, label, icon) ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectTab(index) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.2).sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VibrantBlue,
                    selectedTextColor = VibrantBlue,
                    indicatorColor = VibrantBlueLight,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("nav_tab_$index")
            )
        }
    }
}
