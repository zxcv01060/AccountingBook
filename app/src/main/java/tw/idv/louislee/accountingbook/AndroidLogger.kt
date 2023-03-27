package tw.idv.louislee.accountingbook

import android.util.Log
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.Logger

@Single
class AndroidLogger : Logger {
    private companion object {
        private const val TAG = "AccountingBook"
    }

    override fun information(message: String, vararg args: Any) {
        Log.i(TAG, message.format(*args))
    }

    override fun debug(message: String, vararg args: Any) {
        Log.d(TAG, message.format(*args))
    }

    override fun warn(message: String, vararg args: Any) {
        Log.w(TAG, message.format(*args))
    }

    override fun error(e: Throwable, message: String, vararg args: Any) {
        Log.e(TAG, message.format(*args), e)
    }

    override fun error(message: String, vararg args: Any) {
        Log.e(TAG, message.format(*args))
    }
}