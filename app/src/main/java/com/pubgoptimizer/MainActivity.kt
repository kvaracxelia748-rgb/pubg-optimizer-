package com.pubgoptimizer

import android.app.Activity
import android.os.Bundle
import android.os.Build
import android.app.ActivityManager
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.view.Gravity

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(40, 40, 40, 40)

        val title = TextView(this)
        title.text = "PUBG Optimizer"
        title.textSize = 28f

        val device = TextView(this)
        device.text = "მოწყობილობა: ${Build.MANUFACTURER} ${Build.MODEL}"
        device.textSize = 18f

        val ram = TextView(this)
        ram.text = getRamInfo()
        ram.textSize = 18f

        val optimize = Button(this)
        optimize.text = "OPTIMIZE"

        optimize.setOnClickListener {
            ram.text = getRamInfo()
        }

        layout.addView(title)
        layout.addView(device)
        layout.addView(ram)
        layout.addView(optimize)

        setContentView(layout)
    }

    private fun getRamInfo(): String {
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val info = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(info)

        val total = info.totalMem / (1024 * 1024)
        val available = info.availMem / (1024 * 1024)

        return "RAM: ${available}MB თავისუფალი / ${total}MB"
    }
}