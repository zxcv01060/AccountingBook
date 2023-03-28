package tw.idv.louislee.accountingbook.domain.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventFormDto
import tw.idv.louislee.accountingbook.domain.entity.AccountQueries
import tw.idv.louislee.accountingbook.domain.entity.AccountingEvent
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventQueries
import tw.idv.louislee.accountingbook.domain.utils.DateTimeProvider
import kotlin.coroutines.CoroutineContext

internal interface AccountingEventRepository {
    fun findAll(context: CoroutineContext = Dispatchers.Default): Flow<List<AccountingEvent>>

    fun add(event: AccountingEventFormDto)
}

@Single
internal class AccountingEventRepositoryImpl(
    private val dateTimeProvider: DateTimeProvider,
    private val query: AccountingEventQueries,
    private val accountQuery: AccountQueries
) : AccountingEventRepository {
    override fun findAll(context: CoroutineContext): Flow<List<AccountingEvent>> =
        query.findAll()
            .asFlow()
            .mapToList(context)

    override fun add(event: AccountingEventFormDto) = query.transaction {
        val price = if (event.type.isIncome) {
            event.price
        } else {
            -event.price
        }
        val accountBalance = accountQuery.findBalanceById(event.accountId).executeAsOne()
        val balance = price + accountBalance

        query.add(
            accountId = event.accountId,
            type = event.type,
            isIncome = event.type.isIncome,
            price = price,
            balance = balance,
            note = event.note,
            createDate = dateTimeProvider.now,
            lastUpdateDate = dateTimeProvider.now
        )
        accountQuery.updateBalanceById(
            id = event.accountId,
            balance = balance,
            lastUpdateDate = dateTimeProvider.now
        )
    }
}
