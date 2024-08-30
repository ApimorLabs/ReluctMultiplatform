package com.apimorlabs.reluct.system.services.haptics

/**
 * This is currently not used in desktop but we still need to provide a skeleton implementation
 * This is so that we don't have any errors when we try to use HapticFeedback in commonMain code
 */

internal class DesktopHapticFeedback : HapticFeedback {
    override fun tick() { /** ACTION **/ }

    override fun click() { /** ACTION **/ }

    override fun doubleClick() { /** ACTION **/ }

    override fun heavyClick() { /** ACTION **/ }

    override fun cascadeFall() { /** ACTION **/ }

    override fun spinAndFall() { /** ACTION **/ }

    override fun customDuration(durationMillis: Long) { /** ACTION **/ }
}
