package tw.idv.louislee.accountingbook.domain

interface Logger {
    fun information(message: String, vararg args: Any)

    fun debug(message: String, vararg args: Any)

    fun warn(message: String, vararg args: Any)

    fun error(e: Throwable, message: String, vararg args: Any)

    fun error(message: String, vararg args: Any)
}
