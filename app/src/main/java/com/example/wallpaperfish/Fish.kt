package com.example.wallpaperfish

import android.content.Context
import android.graphics.*
import com.example.wallpaperfish.Device.screenHeight
import kotlin.math.cos
import kotlin.random.Random

class Fish(
    context: Context,
    private val screenWidth: Int,
    direction: Int,
    yConst: Float
) {
    private var bitmap: Bitmap
    private var x: Float = 0f
    var y: Float = 0f
    private var speed: Float = 0f
    private var angle: Float = 0f
    private val bounds = RectF()
    private var isAlive = true
    private var isFlipped = false

    init {
        val resourceId = when (Random.nextInt(1, 4)) {
            1 -> R.drawable.fish
            2 -> R.drawable.fish2
            else -> R.drawable.fish3
        }
        bitmap = BitmapFactory.decodeResource(context.resources, resourceId)

        speed = Random.nextFloat() * 10f + 20f

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)

        when (direction) {
            DIRECTION_LEFT -> {
                x = screenWidth.toFloat()
                y = Random.nextFloat() * (screenHeight - bitmap.height)
                angle = 180f
            }
            DIRECTION_RIGHT -> {
                x = (-bitmap.width).toFloat()
                y = Random.nextFloat() * (screenHeight - bitmap.height)
                angle = 180f
            }
        }

        bounds.set(x, y, x + bitmap.width, y + bitmap.height)
    }

    fun draw(canvas: Canvas) {
        if(isAlive) {
            if (x < -bitmap.width || x > screenWidth) {
                val matrix = Matrix()
                matrix.setScale(-1f, 1f)
                matrix.postTranslate(bitmap.width.toFloat(), 0f)
                val mirroredBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
                bitmap = mirroredBitmap
                canvas.drawBitmap(bitmap, x, y, null)
            } else {
                canvas.drawBitmap(bitmap, x, y, null)
            }
        }
        else drawDead(canvas)
    }

    fun update() {
        if(isAlive) {
            x += speed * cos(Math.toRadians(angle.toDouble())).toFloat()

            if (x < -bitmap.width || x > screenWidth) {
                angle += 180f
                y = Random.nextFloat() * (screenHeight - bitmap.height)
            }

            bounds.offsetTo(x, y)
        }
    }

    fun die() {
        if(isAlive){
            speed = 0f
            isAlive = false
        }
    }

    fun revive() {
        if(!isAlive){
            speed = Random.nextFloat() * 10f + 20f
            isAlive = true
            if (isFlipped) {
                val matrix = Matrix()
                matrix.setScale(1f, -1f)
                matrix.postTranslate(bitmap.width.toFloat(), 0f)
                val unflippedBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
                bitmap = unflippedBitmap
                isFlipped = false
            }
        }
    }

    private fun drawDead(canvas: Canvas) {
        if (!isAlive) {
            if (!isFlipped) {
                val matrix = Matrix()
                matrix.setScale(1f, -1f)
                matrix.postTranslate(bitmap.width.toFloat(), 0f)
                val mirroredBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
                bitmap = mirroredBitmap
                isFlipped = true
            }
            canvas.drawBitmap(bitmap, x, y, null)
        }
    }

    companion object {
        const val DIRECTION_LEFT = 1
        const val DIRECTION_RIGHT = 2
    }


}