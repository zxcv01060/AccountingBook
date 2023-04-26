package tw.idv.louislee.accountingbook.domain.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.dto.account.AccountAccountingEventItemDto
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDetailDto
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDto
import tw.idv.louislee.accountingbook.domain.dto.account.AccountFormDto
import tw.idv.louislee.accountingbook.domain.extension.toAccountItem
import tw.idv.louislee.accountingbook.domain.extension.toDetail
import tw.idv.louislee.accountingbook.domain.extension.toDto
import tw.idv.louislee.accountingbook.domain.repository.AccountRepository
import tw.idv.louislee.accountingbook.domain.repository.AccountingEventRepository
import tw.idv.louislee.accountingbook.domain.repository.InvoiceProductRepository
import tw.idv.louislee.accountingbook.domain.repository.InvoiceRepository
import kotlin.coroutines.CoroutineContext

interface AccountService {
    fun findAll(context: CoroutineContext = Dispatchers.Default): Flow<List<AccountDto>>

    fun findById(id: Long, context: CoroutineContext = Dispatchers.Default): Flow<AccountDetailDto?>

    fun add(account: AccountFormDto)
}

@Single
internal class AccountServiceImpl(
    private val repository: AccountRepository,
    private val accountingEventRepository: AccountingEventRepository,
    private val invoiceRepository: InvoiceRepository,
    private val invoiceProductRepository: InvoiceProductRepository
) : AccountService {
    override fun findAll(context: CoroutineContext): Flow<List<AccountDto>> =
        repository.findAll(context)
            .map { accounts -> accounts.map { it.toDto() } }

    override fun findById(id: Long, context: CoroutineContext): Flow<AccountDetailDto?> =
        repository.findById(id = id, context = context)
            .combine(
                findAccountingEventDetails(id = id, context = context)
            ) { account, accountingEvents ->
                account?.toDetail(accountingEvents)
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun findAccountingEventDetails(
        id: Long,
        context: CoroutineContext
    ): Flow<List<AccountAccountingEventItemDto>> {
        return accountingEventRepository.findByAccountId(accountId = id, context = context)
            .mapLatest { events ->
                events.map { it.toAccountItem() }
            }
    }

    override fun add(account: AccountFormDto) = repository.add(account)
}
