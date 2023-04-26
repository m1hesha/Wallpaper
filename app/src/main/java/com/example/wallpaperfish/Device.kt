package com.example.wallpaperfish

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager


object Device {
    fun getBatteryLevel(context: Context): Int {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = level / scale.toFloat()
        return (batteryPct * 100).toInt()
    }
    var screenWidth: Int = 0
    var screenHeight: Int = 0
}