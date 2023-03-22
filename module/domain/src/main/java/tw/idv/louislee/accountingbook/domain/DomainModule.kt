package tw.idv.louislee.accountingbook.domain

import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.entity.AccountingBookDatabase
import tw.idv.louislee.accountingbook.domain.entity.AccountingEvent
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventTypeAdapter
import tw.idv.louislee.accountingbook.domain.entity.adapter.DateTimeAdapter
import tw.idv.louislee.accountingbook.domain.repository.RepositoryModule

@Module(includes = [RepositoryModule::class])
@ComponentScan
class DomainModule {
    @Single
    fun getDatabaseSchema() = AccountingBookDatabase.Schema

    @Single
    fun createDatabase(driver: SqlDriver) = AccountingBookDatabase(
        driver,
        AccountingEvent.Adapter(
            typeAdapter = AccountingEventTypeAdapter,
            createDateAdapter = DateTimeAdapter,
            lastUpdateDateAdapter = DateTimeAdapter
        )
    )
}
