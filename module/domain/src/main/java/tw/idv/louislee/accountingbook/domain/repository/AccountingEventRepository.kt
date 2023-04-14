package tw.idv.louislee.accountingbook.domain.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
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

    fun findById(id: Long, context: CoroutineContext = Dispatchers.Default): Flow<AccountingEvent?>

    fun add(event: AccountingEventFormDto)

    fun delete(id: Long)

    fun updateById(id: Long, event: AccountingEventFormDto)
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

    override fun findById(id: Long, context: CoroutineContext): Flow<AccountingEvent?> =
        query.findById(id = id)
            .asFlow()
            .mapToOneOrNull(context)

    override fun add(event: AccountingEventFormDto) = query.transaction {
        val price = if (event.type.isIncome) {
            event.price
        } else {
            -event.price
        }
        val newBalance = updateAccountBalance(accountId = event.accountId, balanceInterval = price)

        query.add(
            accountId = event.accountId,
            type = event.type,
            isIncome = event.type.isIncome,
            price = price,
            balance = newBalance,
            note = event.note,
            createDate = dateTimeProvider.now,
            lastUpdateDate = dateTimeProvider.now
        )
    }

    private fun updateAccountBalance(accountId: Long, balanceInterval: Long): Long {
        val accountBalance = accountQuery.findBalanceById(id = accountId).executeAsOne()
        if (balanceInterval == 0L) {
            return accountBalance
        }
        
        val balance = accountBalance + balanceInterval
        accountQuery.updateBalanceById(
            id = accountId,
            balance = balance,
            lastUpdateDate = dateTimeProvider.now
        )

        return balance
    }

    override fun delete(id: Long) = query.transaction {
        val event = query.findById(id = id)
            .executeAsOneOrNull() ?: return@transaction
        updateAccountBalance(
            accountId = event.accountId,
            balanceInterval = -event.price
        )

        query.plusBalanceByIdGreaterThan(
            id = id,
            price = -event.price,
            lastUpdateDate = dateTimeProvider.now
        )
        query.deleteById(id = id)
    }

    override fun updateById(id: Long, event: AccountingEventFormDto) = query.transaction {
        val originalEvent = query.findById(id = id)
            .executeAsOneOrNull() ?: return@transaction
        val price = if (event.type.isIncome) {
            event.price
        } else {
            -event.price
        }
        //  原       新
        // +100 -> +300 = +200
        // +100 -> +50  = -50
        // +100 -> -200 = -300
        // +100 -> +100 = +0
        // -100 -> -300 = -200
        // -100 -> -50  = +50
        val newBalance = updateAccountBalance(
            accountId = event.accountId,
            balanceInterval = price - originalEvent.price
        )

        query.plusBalanceByIdGreaterThan(
            id = id,
            price = -event.price,
            lastUpdateDate = dateTimeProvider.now
        )
        query.updateById(
            id = id,
            accountId = event.accountId,
            type = event.type,
            isIncome = event.type.isIncome,
            price = price,
            balance = newBalance,
            note = event.note,
            lastUpdateDate = dateTimeProvider.now
        )
    }
}
