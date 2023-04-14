package tw.idv.louislee.accountingbook.extension

import android.content.Intent
import android.os.Build

inline fun <reified T> Intent.getParcelableExtraCompat(name: String) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableExtra(name)
    }
