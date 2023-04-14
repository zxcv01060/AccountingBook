package tw.idv.louislee.accountingbook.page.accountingevent.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import tw.idv.louislee.accountingbook.domain.service.AccountService
import tw.idv.louislee.accountingbook.domain.service.AccountingEventService
import tw.idv.louislee.accountingbook.state.AccountingEventFormState

@KoinViewModel
class AccountingEventEditViewModel(
    private val service: AccountingEventService,
    accountService: AccountService,
    @InjectedParam private val id: Long,
    @InjectedParam val state: AccountingEventFormState
) : ViewModel() {
    val accounts = accountService.findAll(viewModelScope.coroutineContext)

    fun update() = service.updateById(id = id, event = state.form)
}