package tw.idv.louislee.accountingbook.domain.repository

import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.entity.AccountingBookDatabase

@org.koin.core.annotation.Module
internal class RepositoryModule {
    @Single
    fun accountingEventQuery(database: AccountingBookDatabase) = database.accountingEventQueries

    @Single
    fun accountQuery(database: AccountingBookDatabase) = database.accountQueries
}