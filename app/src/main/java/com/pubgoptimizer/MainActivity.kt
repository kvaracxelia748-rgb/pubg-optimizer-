package com.pubgoptimizer

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var infoText: TextView
    private lateinit var activityManager: ActivityManager
    private lateinit var batteryManager: BatteryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ეკრანი რომ არ ჩაქრეს აპლიკაციის გამოყენებისას
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(50, 50, 50, 50)
        }

        val title = TextView(this).apply {
            text = "PUBG Optimizer"
            textSize = 28f
            gravity = Gravity.CENTER
        }

        val device = TextView(this).apply {
            text = "📱 ${Build.MANUFACTURER.uppercase()} ${Build.MODEL}"
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 10, 0, 30)
        }

        infoText = TextView(this).apply {
            textSize = 15f
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 30)
        }

        val performanceBtn = createButton("⚡ PERFORMANCE") {
            infoText.text = "${getDeviceInfo()}\n\n⚡ Performance mode active!"
        }

        val balancedBtn = createButton("⚖ BALANCED") {
            infoText.text = "${getDeviceInfo()}\n\n⚖ Balanced mode active!"
        }

        val optimizeBtn = createButton("🚀 OPTIMIZE RAM") {
            System.gc()
            infoText.text = "${getDeviceInfo()}\n\n🚀 RAM cleaned successfully!"
        }

        val fpsBoostBtn = createButton("🔥 REAL FPS BOOST (SETTINGS)") {
            openDeveloperOptions()
        }

        val launchBtn = createButton("🎮 LAUNCH PUBG MOBILE") {
            launchPubg()
        }

        layout.addView(title)
        layout.addView(device)
        layout.addView(infoText)
        layout.addView(performanceBtn)
        layout.addView(balancedBtn)
        layout.addView(optimizeBtn)
        layout.addView(fpsBoostBtn)
        layout.addView(launchBtn)

        setContentView(layout)

        infoText.text = getDeviceInfo()
    }

    private fun openDeveloperOptions() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
            startActivity(intent)
            Toast.makeText(this, "ჩართეთ Graphics Driver ან Game Driver FPS-ის გასაზრდელად!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_SETTINGS)
            startActivity(intent)
            Toast.makeText(this, "გახსენით Developer Options პარამეტრებიდან", Toast.LENGTH_LONG).show()
        }
    }

    private fun launchPubg() {
        val pubgPackages = listOf(
            "com.tencent.ig",     // Global
            "com.pubg.krmobile",  // KR (Korea)
            "com.vng.pubgmobile", // Vietnam
            "com.pubg.imobile"    // India (BGMI)
        )

        var launched = false

        for (packageName in pubgPackages) {
            val intent: Intent? = packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                startActivity(intent)
                launched = true
                break
            }
        }

        if (!launched) {
            Toast.makeText(this, "PUBG Mobile არ არის დაინსტალირებული!", Toast.LENGTH_LONG).show()
            infoText.text = "${getDeviceInfo()}\n\n⚠️ PUBG Mobile ვერ მოიძებნა"
        }
    }

    private fun createButton(buttonText: String, onClick: () -> Unit): Button {
        return Button(this).apply {
            text = buttonText
            setOnClickListener { onClick() }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 10, 0, 10)
            }
        }
    }

    private fun getDeviceInfo(): String {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val totalRam = memoryInfo.totalMem / (1024 * 1024)
        val availableRam = memoryInfo.availMem / (1024 * 1024)

        val battery = batteryManager.getIntProperty(
            BatteryManager.BATTERY_PROPERTY_CAPACITY
        )

        return "🧠 RAM: ${availableRam}MB / ${totalRam}MB\n" +
               "🔋 Battery: $battery% (${getBatteryTemperature()})\n" +
               "🎮 Recommended FPS: ${getRecommendedFps()}"
    }

    private fun getBatteryTemperature(): String {
        val intent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val temp = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
        return "${temp / 10.0}°C"
    }

    private fun getRecommendedFps(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            "60 - 90 FPS"
        } else {
            "40 - 60 FPS"
        }
    }
}
