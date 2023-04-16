package tw.idv.louislee.accountingbook.domain.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDetailDto
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDto
import tw.idv.louislee.accountingbook.domain.dto.account.AccountFormDto
import tw.idv.louislee.accountingbook.domain.entity.Account
import tw.idv.louislee.accountingbook.domain.extension.toDetail
import tw.idv.louislee.accountingbook.domain.repository.AccountRepository
import tw.idv.louislee.accountingbook.domain.repository.AccountingEventRepository
import kotlin.coroutines.CoroutineContext

interface AccountService {
    fun findAll(context: CoroutineContext = Dispatchers.Default): Flow<List<AccountDto>>

    fun findById(id: Long, context: CoroutineContext = Dispatchers.Default): Flow<AccountDetailDto?>

    fun add(account: AccountFormDto)
}

@Single
internal class AccountServiceImpl(
    private val repository: AccountRepository,
    private val accountingEventRepository: AccountingEventRepository
) : AccountService {
    override fun findAll(context: CoroutineContext): Flow<List<AccountDto>> =
        repository.findAll(context)
            .map { it.map(::accountMapper) }

    private fun accountMapper(account: Account): AccountDto = AccountDto(
        id = account.id,
        type = account.type,
        name = account.name,
        balance = account.balance
    )

    override fun findById(id: Long, context: CoroutineContext): Flow<AccountDetailDto?> =
        repository.findById(id = id, context = context)
            .combine(
                accountingEventRepository.findByAccountId(
                    accountId = id,
                    context = context
                )
            ) { account, accountingEvents ->
                if (account == null) {
                    return@combine null
                }

                return@combine AccountDetailDto(
                    id = account.id,
                    name = account.name,
                    type = account.type,
                    balance = account.balance,
                    createDate = account.createDate,
                    lastUpdateDate = account.lastUpdateDate,
                    accountingEvents = accountingEvents.map { it.toDetail() }
                )
            }

    override fun add(account: AccountFormDto) = repository.add(account)
}
