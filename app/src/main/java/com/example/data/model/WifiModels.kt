package com.example.data.model

data class WifiConnectionInfo(
    val ssid: String = "<Unknown SSID>",
    val bssid: String = "00:00:00:00:00:00",
    val rssi: Int = -60, // in dBm
    val signalPercentage: Int = 75, // 0 - 100
    val signalLevel: SignalQuality = SignalQuality.GOOD,
    val linkSpeedMbps: Int = 433,
    val txLinkSpeedMbps: Int = 433,
    val rxLinkSpeedMbps: Int = 433,
    val frequencyMhz: Int = 5180,
    val band: WifiBand = WifiBand.BAND_5_GHZ,
    val channelNumber: Int = 36,
    val channelWidthMhz: Int = 80,
    val ipAddress: String = "192.168.1.100",
    val gatewayIp: String = "192.168.1.1",
    val subnetMask: String = "255.255.255.0",
    val dns1: String = "8.8.8.8",
    val dns2: String = "8.8.4.4",
    val macAddress: String = "02:00:00:00:00:00",
    val securityType: String = "WPA2/WPA3 Personal",
    val standard: String = "Wi-Fi 6 (802.11ax)",
    val isConnected: Boolean = true,
    val isHidden: Boolean = false,
    val healthScore: Int = 88 // 0 - 100
)

enum class SignalQuality(val label: String, val minDbm: Int) {
    EXCELLENT("Excellent", -50),
    GOOD("Good", -65),
    FAIR("Fair", -75),
    POOR("Poor", -85),
    VERY_POOR("Very Poor", -100);

    companion object {
        fun fromRssi(rssi: Int): SignalQuality = when {
            rssi >= -50 -> EXCELLENT
            rssi >= -65 -> GOOD
            rssi >= -75 -> FAIR
            rssi >= -85 -> POOR
            else -> VERY_POOR
        }
    }
}

enum class WifiBand(val displayName: String) {
    BAND_2_4_GHZ("2.4 GHz"),
    BAND_5_GHZ("5 GHz"),
    BAND_6_GHZ("6 GHz"),
    BAND_UNKNOWN("Unknown");

    companion object {
        fun fromFrequency(freqMhz: Int): WifiBand = when {
            freqMhz in 2400..2495 -> BAND_2_4_GHZ
            freqMhz in 5150..5895 -> BAND_5_GHZ
            freqMhz in 5925..7125 -> BAND_6_GHZ
            else -> BAND_UNKNOWN
        }
    }
}

data class ScanNetworkItem(
    val ssid: String,
    val bssid: String,
    val rssi: Int,
    val frequencyMhz: Int,
    val band: WifiBand,
    val channel: Int,
    val channelWidthMhz: Int = 20,
    val capabilities: String = "WPA2-PSK",
    val distanceEstimateMeters: Double = 3.5,
    val isConnected: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
) {
    val signalQuality: SignalQuality
        get() = SignalQuality.fromRssi(rssi)

    val signalPercentage: Int
        get() = ((rssi + 100).coerceIn(0, 50) * 2).coerceIn(0, 100)
}

data class ChannelAnalysis(
    val channel: Int,
    val centerFreqMhz: Int,
    val band: WifiBand,
    val networkCount: Int,
    val maxRssi: Int?,
    val starRating: Float, // 1.0 to 5.0
    val ratingLabel: String, // "Optimal", "Good", "Moderate", "Congested"
    val networks: List<ScanNetworkItem> = emptyList()
)

data class SignalHistoryPoint(
    val timestamp: Long,
    val rssi: Int,
    val linkSpeedMbps: Int
)

data class PingResult(
    val targetName: String,
    val targetHost: String,
    val latencyMs: Long,
    val isSuccess: Boolean,
    val packetLossPercent: Int = 0,
    val jitterMs: Long = 2,
    val statusMessage: String = "Reachable"
)

data class LanDevice(
    val ip: String,
    val hostname: String,
    val mac: String = "",
    val isGateway: Boolean = false,
    val isSelf: Boolean = false,
    val deviceType: String = "Network Device",
    val responseTimeMs: Long = 4
)
