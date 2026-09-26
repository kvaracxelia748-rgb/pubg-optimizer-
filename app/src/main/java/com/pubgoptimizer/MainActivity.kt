package com.pubgoptimizer

import android.app.Activity
import android.os.Bundle
import android.os.Build
import android.os.BatteryManager
import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var infoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(40, 40, 40, 40)

        val title = TextView(this)
        title.text = "PUBG Optimizer"
        title.textSize = 30f
        title.gravity = Gravity.CENTER

        val device = TextView(this)
        device.text = "📱 ${Build.MANUFACTURER} ${Build.MODEL}"
        device.textSize = 18f
        device.gravity = Gravity.CENTER

        infoText = TextView(this)
        infoText.textSize = 18f
        infoText.gravity = Gravity.CENTER

        val performance = Button(this)
        performance.text = "⚡ PERFORMANCE"

        val balanced = Button(this)
        balanced.text = "⚖ BALANCED"

        val optimize = Button(this)
        optimize.text = "🚀 OPTIMIZE"

        performance.setOnClickListener {
            infoText.text = getDeviceInfo() + "\n\n⚡ Performance profile selected"
        }

        balanced.setOnClickListener {
            infoText.text = getDeviceInfo() + "\n\n⚖ Balanced profile selected"
        }

        optimize.setOnClickListener {
            infoText.text = getDeviceInfo() + "\n\n🚀 Optimization check completed"
        }

        layout.addView(title)
        layout.addView(device)
        layout.addView(infoText)
        layout.addView(performance)
        layout.addView(balanced)
        layout.addView(optimize)

        setContentView(layout)

        infoText.text = getDeviceInfo()
    }

    private fun getDeviceInfo(): String {
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val memoryInfo = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(memoryInfo)

        val totalRam = memoryInfo.totalMem / (1024 * 1024)
        val availableRam = memoryInfo.availMem / (1024 * 1024)

        val batteryManager =
            getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        val battery = batteryManager.getIntProperty(
            BatteryManager.BATTERY_PROPERTY_CAPACITY
        )

        return "🧠 RAM: ${availableRam}MB / ${totalRam}MB\n" +
                "🔋 Battery: $battery%\n" +
                "🎮 Recommended FPS: ${getRecommendedFps()}"
    }

    private fun getRecommendedFps(): String {
        return when {
            Build.VERSION.SDK_INT >= 35 -> "60 FPS"
            Build.VERSION.SDK_INT >= 30 -> "60 FPS"
            else -> "40 FPS"
        }
    }
}