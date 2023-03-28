package tw.idv.louislee.accountingbook.domain.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDto
import tw.idv.louislee.accountingbook.domain.entity.Account
import tw.idv.louislee.accountingbook.domain.repository.AccountRepository
import kotlin.coroutines.CoroutineContext

interface AccountService {
    fun findAll(context: CoroutineContext = Dispatchers.Default): Flow<List<AccountDto>>
}

@Single
internal class AccountServiceImpl(private val repository: AccountRepository) : AccountService {
    override fun findAll(context: CoroutineContext): Flow<List<AccountDto>> =
        repository.findAll(context)
            .map { it.map(::accountMapper) }

    private fun accountMapper(account: Account): AccountDto = AccountDto(
        id = account.id,
        type = account.type,
        name = account.name,
        balance = account.balance
    )
}
