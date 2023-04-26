package tw.idv.louislee.accountingbook.domain.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.dto.accountingevent.AccountingEventDetailDto
import tw.idv.louislee.accountingbook.domain.dto.accountingevent.AccountingEventDto
import tw.idv.louislee.accountingbook.domain.dto.accountingevent.AccountingEventFormDto
import tw.idv.louislee.accountingbook.domain.dto.invoice.ElectronicInvoiceDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEvent
import tw.idv.louislee.accountingbook.domain.extension.toDetail
import tw.idv.louislee.accountingbook.domain.extension.toDto
import tw.idv.louislee.accountingbook.domain.repository.AccountingEventRepository
import tw.idv.louislee.accountingbook.domain.repository.InvoiceProductRepository
import tw.idv.louislee.accountingbook.domain.repository.InvoiceRepository
import kotlin.coroutines.CoroutineContext

interface AccountingEventService {
    fun findAll(context: CoroutineContext = Dispatchers.Default): Flow<List<AccountingEventDto>>

    fun findById(
        id: Long,
        context: CoroutineContext = Dispatchers.Default
    ): Flow<AccountingEventDetailDto?>

    fun add(event: AccountingEventFormDto, electronicInvoice: ElectronicInvoiceDto?)

    fun delete(id: Long)

    fun updateById(id: Long, event: AccountingEventFormDto)
}

@Single
internal class AccountingEventServiceImpl(
    private val repository: AccountingEventRepository,
    private val invoiceRepository: InvoiceRepository,
    private val invoiceProductRepository: InvoiceProductRepository
) : AccountingEventService {
    override fun findAll(context: CoroutineContext): Flow<List<AccountingEventDto>> =
        repository.findAll(context)
            .map { it.map(::eventMapper) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findById(id: Long, context: CoroutineContext): Flow<AccountingEventDetailDto?> =
        repository.findById(id = id, context)
            .flatMapLatest {
                if (it == null) {
                    return@flatMapLatest flowOf(null)
                }

                if (it.invoiceId != null) {
                    findInvoiceById(event = it, invoiceId = it.invoiceId, context = context)
                } else {
                    flowOf(it.toDetail(null))
                }
            }

    private fun findInvoiceById(
        event: AccountingEvent,
        invoiceId: String,
        context: CoroutineContext
    ) = invoiceRepository.findById(id = invoiceId, context = context)
        .combine(
            invoiceProductRepository.findByInvoiceId(
                invoiceId = invoiceId,
                context = context
            )
        ) { invoice, products ->
            event.toDetail(invoice = invoice?.toDto(products))
        }

    private fun eventMapper(event: AccountingEvent) = AccountingEventDto(
        id = event.id,
        type = event.type,
        price = event.price,
        note = event.note
    )

    override fun add(event: AccountingEventFormDto, electronicInvoice: ElectronicInvoiceDto?) =
        repository.add(event, electronicInvoice)

    override fun delete(id: Long) = repository.delete(id = id)
    override fun updateById(id: Long, event: AccountingEventFormDto) =
        repository.updateById(id = id, event = event)
}
