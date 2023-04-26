package com.example.wallpaperfish

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Handler
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class Wallpaper : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return MyWallpaperEngine()
    }

    inner class MyWallpaperEngine : Engine() {
        private var backgroundImage: Bitmap? = null
        private var bgImage: Bitmap? = null
        private var batteryLevel = 100
        private val fishes = mutableListOf<Fish>()
        @Suppress("DEPRECATION")
        private val handler = Handler()

        private val drawRunnable = object : Runnable {
            override fun run() {
                drawFrame()
                handler.postDelayed(this, 60)
            }
        }

        init {
            backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.water2)
            bgImage = BitmapFactory.decodeResource(resources, R.drawable.background)
            batteryLevel = Device.getBatteryLevel(applicationContext)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            if (visible) {
                batteryLevel = Device.getBatteryLevel(applicationContext)
            }
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            val screenHeight = Device.screenHeight
            val screenWidth = Device.screenWidth
            fishes.apply {
                add(Fish(applicationContext, screenWidth, Fish.DIRECTION_RIGHT, screenHeight * 0.9f))
                add(Fish(applicationContext, screenWidth, Fish.DIRECTION_LEFT, screenHeight* 0.7f))
                add(Fish(applicationContext, screenWidth, Fish.DIRECTION_RIGHT, screenHeight* 0.5f))
                add(Fish(applicationContext, screenWidth, Fish.DIRECTION_LEFT, screenHeight* 0.3f))
                add(Fish(applicationContext, screenWidth, Fish.DIRECTION_RIGHT, screenHeight* 0.1f))
            }
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            handler.post(drawRunnable)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            handler.removeCallbacks(drawRunnable)
        }

        private fun drawFrame() {
            val surfaceHolder = surfaceHolder
            var canvas: Canvas? = null

            try {
                canvas = surfaceHolder.lockCanvas()
                if (canvas != null) {
                    bgImage?.let { canvas.drawBitmap(it, 0f, 0f, null) }
                    val yOffset = Device.screenHeight * (100 - batteryLevel) / 100
                    canvas.drawBitmap(backgroundImage!!, 0f, yOffset.toFloat(), null)

                    for (fish in fishes) {
                        fish.update()

                        if (fish.y < yOffset) {
                            fish.die()
                        } else {
                            fish.revive()
                        }

                        fish.draw(canvas)
                    }
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
        }

        private val batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                batteryLevel = Device.getBatteryLevel(applicationContext)
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            applicationContext.registerReceiver(batteryReceiver, filter)
        }

        override fun onDestroy() {
            super.onDestroy()
            applicationContext.unregisterReceiver(batteryReceiver)
        }
    }

}