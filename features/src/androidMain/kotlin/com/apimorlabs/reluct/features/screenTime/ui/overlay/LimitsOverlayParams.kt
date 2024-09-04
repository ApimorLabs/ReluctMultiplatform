package com.apimorlabs.reluct.features.screenTime.ui.overlay

import android.graphics.PixelFormat
import android.view.WindowManager

object LimitsOverlayParams {

    fun getParams(): WindowManager.LayoutParams {
        val overlayLayoutFlag: Int =
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            overlayLayoutFlag,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }
}
