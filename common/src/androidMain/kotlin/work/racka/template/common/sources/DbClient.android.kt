package work.racka.template.common.sources

import android.content.Context
import android.os.Build

internal actual class DbClient(
    private val context: Context
) {
    actual fun platformName(): String {
        return "Android ${Build.VERSION.SDK_INT}, pkg: ${context.packageName}"
    }
}
