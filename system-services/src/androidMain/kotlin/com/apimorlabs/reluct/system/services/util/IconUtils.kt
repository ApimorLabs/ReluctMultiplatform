package com.apimorlabs.reluct.system.services.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import com.apimorlabs.reluct.common.models.domain.appInfo.Icon
import java.io.ByteArrayInputStream
import java.io.InputStream


object IconUtils {
    fun ByteArray.toBitmap(): Bitmap {
        // Convert byte array to InputStream
        val inputStream: InputStream = ByteArrayInputStream(this)

        // Decode InputStream to Bitmap
        val bitmap = BitmapFactory.decodeStream(inputStream)

        // Return the Bitmap object
        return bitmap
    }

    fun Icon.toBitmap(): Bitmap = this.icon.toBitmap()

    fun Icon.toDrawable(context: Context): Drawable = this.toBitmap().toDrawable(context.resources)
}
