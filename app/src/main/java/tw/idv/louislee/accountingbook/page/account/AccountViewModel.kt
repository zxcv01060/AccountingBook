package tw.idv.louislee.accountingbook.page.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.android.annotation.KoinViewModel
import tw.idv.louislee.accountingbook.domain.service.AccountService

@KoinViewModel
class AccountViewModel(private val accountService: AccountService) : ViewModel() {
    fun findAll() = accountService.findAll(viewModelScope.coroutineContext)
}