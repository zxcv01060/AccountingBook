package tw.idv.louislee.accountingbook

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.AfterVersionWithDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.DomainModule
import tw.idv.louislee.accountingbook.domain.entity.migration.Migration

@Module([DomainModule::class])
@ComponentScan
class AppModule {
    // 最少要有一個Single之類的元件才會自動產生Module Class
    @Single
    fun createAndroidDriver(
        context: Context,
        schema: SqlDriver.Schema,
        migrations: List<Migration>
    ): SqlDriver {
        val callbacks = migrations
            .sortedBy { it.afterVersion }
            .map { migration ->
                AfterVersionWithDriver(migration.afterVersion) {
                    migration.migrate(it)
                }
            }
            .toTypedArray()

        return AndroidSqliteDriver(
            schema = schema,
            context = context,
            name = "accounting_book.db",
            callback = AndroidSqliteDriver.Callback(schema = schema, callbacks = callbacks)
        )
    }
}