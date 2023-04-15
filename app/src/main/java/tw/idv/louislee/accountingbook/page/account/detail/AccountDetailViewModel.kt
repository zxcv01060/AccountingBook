package tw.idv.louislee.accountingbook.page.account.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import tw.idv.louislee.accountingbook.domain.service.AccountService

@KoinViewModel
class AccountDetailViewModel(
    private val service: AccountService,
    @InjectedParam id: Long
) : ViewModel() {
    val account = service.findById(id, viewModelScope.coroutineContext)
}