package tw.idv.louislee.accountingbook.extension

import android.app.Activity
import android.content.Context
import android.content.Intent

fun Context.finish() {
    val activity =
        this as? Activity ?: throw UnsupportedOperationException("不支援非Activity類型的Context操作")

    activity.finish()
}

inline fun <reified A : Activity> Context.startActivity(intentBuilder: Intent.() -> Unit = {}) {
    val intent = Intent(this, A::class.java)
    intent.intentBuilder()

    startActivity(intent)
}
