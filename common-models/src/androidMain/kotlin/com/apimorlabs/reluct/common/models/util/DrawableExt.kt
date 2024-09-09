package com.apimorlabs.reluct.common.models.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import kotlinx.serialization.builtins.ByteArraySerializer
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer


fun Drawable.getBitmap(): Bitmap? =
    try {
        val bitmap: Bitmap = Bitmap.createBitmap(
            this.intrinsicWidth,
            this.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        this.setBounds(0, 0, canvas.width, canvas.height)
        this.draw(canvas)
        bitmap
    } catch (e: OutOfMemoryError) {
        // Handle the error
        Log.d("GetBitmap", "Bitmap Convert Error: ${e.message}")
        null
    }

/**
 * Converts bitmap to the byte array without compression
 * @return result byte array
 */
fun Bitmap.toByteArrayUncompressed(): ByteArray {
    val s = ByteArray(size = this.byteCount)
    val byteBuffer = ByteBuffer.allocate(this.byteCount)
    this.copyPixelsToBuffer(byteBuffer)
    byteBuffer.rewind()
    return byteBuffer.array()
}

/**
 * Converts bitmap to byte array in PNG format
 * @param bitmap source bitmap
 * @return result byte array
 */
fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
    var baos: ByteArrayOutputStream? = null
    try {
        baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    } finally {
        if (baos != null) {
            try {
                baos.close()
            } catch (e: IOException) {
                Log.e(
                    "Convert Bitmap",
                    "ByteArrayOutputStream was not closed"
                )
            }
        }
    }
}

/**
 * Converts compressed byte array to bitmap
 * @param src source array
 * @return result bitmap
 */
fun convertCompressedByteArrayToBitmap(src: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(src, 0, src.size)
}