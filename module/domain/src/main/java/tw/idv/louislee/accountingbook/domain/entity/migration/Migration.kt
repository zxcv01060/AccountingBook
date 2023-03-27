package tw.idv.louislee.accountingbook.domain.entity.migration

import com.squareup.sqldelight.db.SqlDriver

interface Migration {
    val afterVersion: Int

    fun migrate(driver: SqlDriver)
}
