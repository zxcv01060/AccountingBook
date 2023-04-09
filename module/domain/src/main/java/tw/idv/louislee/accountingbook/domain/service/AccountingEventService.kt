package tw.idv.louislee.accountingbook.domain.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventDetailDto
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventDto
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventFormDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEvent
import tw.idv.louislee.accountingbook.domain.extension.toDetail
import tw.idv.louislee.accountingbook.domain.repository.AccountingEventRepository
import kotlin.coroutines.CoroutineContext

interface AccountingEventService {
    fun findAll(context: CoroutineContext = Dispatchers.Default): Flow<List<AccountingEventDto>>

    fun findById(
        id: Long,
        context: CoroutineContext = Dispatchers.Default
    ): Flow<AccountingEventDetailDto?>

    fun add(event: AccountingEventFormDto)

    fun delete(id: Long)
}

@Single
internal class AccountingEventServiceImpl(
    private val repository: AccountingEventRepository
) : AccountingEventService {
    override fun findAll(context: CoroutineContext): Flow<List<AccountingEventDto>> =
        repository.findAll(context)
            .map { it.map(::eventMapper) }

    override fun findById(id: Long, context: CoroutineContext): Flow<AccountingEventDetailDto?> =
        repository.findById(id = id, context)
            .map { it?.toDetail() }

    private fun eventMapper(event: AccountingEvent) = AccountingEventDto(
        id = event.id,
        type = event.type,
        price = event.price,
        note = event.note
    )

    override fun add(event: AccountingEventFormDto) =
        repository.add(event)

    override fun delete(id: Long) = repository.delete(id = id)
}
