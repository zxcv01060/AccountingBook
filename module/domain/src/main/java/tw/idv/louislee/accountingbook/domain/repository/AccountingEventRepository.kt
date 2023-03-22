package tw.idv.louislee.accountingbook.domain.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventFormDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEvent
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventQueries
import java.time.Clock
import java.time.ZonedDateTime
import kotlin.coroutines.CoroutineContext

internal interface AccountingEventRepository {
    fun findAll(context: CoroutineContext = Dispatchers.Default): Flow<List<AccountingEvent>>

    fun add(event: AccountingEventFormDto)
}

@Single
internal class AccountingEventRepositoryImpl(
    private val query: AccountingEventQueries
) : AccountingEventRepository {
    override fun findAll(context: CoroutineContext): Flow<List<AccountingEvent>> =
        query.findAll()
            .asFlow()
            .mapToList(context)

    override fun add(event: AccountingEventFormDto) = query.add(
        type = event.type,
        isIncome = event.type.isIncome,
        price = event.price,
        note = event.note,
        createDate = ZonedDateTime.now(Clock.systemUTC()),
        lastUpdateDate = ZonedDateTime.now(Clock.systemUTC())
    )
}
