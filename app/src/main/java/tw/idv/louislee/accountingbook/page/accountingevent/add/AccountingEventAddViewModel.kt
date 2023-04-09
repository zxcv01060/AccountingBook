package tw.idv.louislee.accountingbook.page.accountingevent.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDto
import tw.idv.louislee.accountingbook.domain.service.AccountService
import tw.idv.louislee.accountingbook.domain.service.AccountingEventService
import tw.idv.louislee.accountingbook.state.AccountingEventFormState

@KoinViewModel
class AccountingEventAddViewModel(
    private val accountingEventService: AccountingEventService,
    private val accountService: AccountService
) : ViewModel() {
    val state = AccountingEventFormState()

    fun add() = accountingEventService.add(state.form)

    fun findAllAccount(): Flow<List<AccountDto>> =
        accountService.findAll(viewModelScope.coroutineContext)
}