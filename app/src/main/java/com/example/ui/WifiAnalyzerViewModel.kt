package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChannelAnalysis
import com.example.data.model.LanDevice
import com.example.data.model.PingResult
import com.example.data.model.ScanNetworkItem
import com.example.data.model.SignalHistoryPoint
import com.example.data.model.WifiBand
import com.example.data.model.WifiConnectionInfo
import com.example.data.scanner.WifiScannerHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class SortOption(val label: String) {
    SIGNAL_DESC("Signal Strength (Strongest)"),
    SSID_ASC("Network Name (A-Z)"),
    CHANNEL_ASC("Channel (Low to High)")
}

class WifiAnalyzerViewModel(application: Application) : AndroidViewModel(application) {

    private val scannerHelper = WifiScannerHelper(application.applicationContext, viewModelScope)

    val connectionInfo: StateFlow<WifiConnectionInfo> = scannerHelper.connectionInfo
    val isScanning: StateFlow<Boolean> = scannerHelper.isScanning
    val signalHistory: StateFlow<List<SignalHistoryPoint>> = scannerHelper.signalHistory
    val channels24GHz: StateFlow<List<ChannelAnalysis>> = scannerHelper.channels24GHz
    val channels5GHz: StateFlow<List<ChannelAnalysis>> = scannerHelper.channels5GHz
    val pingResults: StateFlow<List<PingResult>> = scannerHelper.pingResults
    val lanDevices: StateFlow<List<LanDevice>> = scannerHelper.lanDevices
    val isLanScanning: StateFlow<Boolean> = scannerHelper.isLanScanning
    val lanScanProgress: StateFlow<Float> = scannerHelper.lanScanProgress

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedBandFilter = MutableStateFlow<WifiBand?>(null)
    val selectedBandFilter: StateFlow<WifiBand?> = _selectedBandFilter.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.SIGNAL_DESC)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _selectedSpectrumBand = MutableStateFlow(WifiBand.BAND_2_4_GHZ)
    val selectedSpectrumBand: StateFlow<WifiBand> = _selectedSpectrumBand.asStateFlow()

    private val _isSimulationActive = MutableStateFlow(false)
    val isSimulationActive: StateFlow<Boolean> = _isSimulationActive.asStateFlow()

    private val _hasPermissions = MutableStateFlow(false)
    val hasPermissions: StateFlow<Boolean> = _hasPermissions.asStateFlow()

    // Filtered & Sorted Networks
    val filteredNetworks: StateFlow<List<ScanNetworkItem>> = combine(
        scannerHelper.scanResults,
        _searchQuery,
        _selectedBandFilter,
        _sortOption
    ) { networks, query, bandFilter, sort ->
        var list = networks
        if (query.isNotBlank()) {
            list = list.filter {
                it.ssid.contains(query, ignoreCase = true) ||
                        it.bssid.contains(query, ignoreCase = true)
            }
        }
        if (bandFilter != null) {
            list = list.filter { it.band == bandFilter }
        }
        when (sort) {
            SortOption.SIGNAL_DESC -> list.sortedByDescending { it.rssi }
            SortOption.SSID_ASC -> list.sortedBy { it.ssid.lowercase() }
            SortOption.CHANNEL_ASC -> list.sortedBy { it.channel }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Run initial diagnostics
        scannerHelper.runPingDiagnostics()
    }

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setBandFilter(band: WifiBand?) {
        _selectedBandFilter.value = band
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun setSpectrumBand(band: WifiBand) {
        _selectedSpectrumBand.value = band
    }

    fun setPermissionsGranted(granted: Boolean) {
        _hasPermissions.value = granted
        if (granted) {
            scannerHelper.refreshCurrentConnection()
            scannerHelper.triggerScan()
        }
    }

    fun toggleSimulationMode() {
        val next = !_isSimulationActive.value
        _isSimulationActive.value = next
        scannerHelper.setSimulationMode(next)
    }

    fun refreshAll() {
        scannerHelper.refreshCurrentConnection()
        scannerHelper.triggerScan()
        val gw = connectionInfo.value.gatewayIp
        scannerHelper.runPingDiagnostics(gw)
    }

    fun runPingDiagnostics() {
        val gw = connectionInfo.value.gatewayIp
        scannerHelper.runPingDiagnostics(gw)
    }

    fun runLanDeviceScan() {
        val gw = connectionInfo.value.gatewayIp
        scannerHelper.scanLanSubnet(gw)
    }

    override fun onCleared() {
        super.onCleared()
        scannerHelper.cleanUp()
    }
}
