package com.apimorlabs.reluct.compose.ui.statsHelpers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Dispatcher necessary for loading charts statistics
 * This is necessary because it map the dispatcher to Dispatchers.Swing on desktop
 * And Dispatcher.IO on Android
 * Depending on Dispatchers.IO for this UI related task on Desktop will crash the app with:
 * `"AWT-EventQueue-0" java.lang.IllegalStateException: Method should be called from AWT event dispatch thread`
 *
 * This makes sure the mapping is done on the correct Event dispatcher for Swing on Desktop
 */
object StatsDispatcher {
    val Dispatcher: CoroutineDispatcher = Dispatchers.IO
}
