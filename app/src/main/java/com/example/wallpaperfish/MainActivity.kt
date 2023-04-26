package com.example.wallpaperfish

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        intent.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(this, Wallpaper::class.java)
        )

        startActivityForResult(intent, 0)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val displayMetrics = DisplayMetrics()
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val display: Display? = wm.defaultDisplay

        display?.getMetrics(displayMetrics)

        Device.screenHeight = displayMetrics.heightPixels
        Device.screenWidth = displayMetrics.widthPixels
    }
}