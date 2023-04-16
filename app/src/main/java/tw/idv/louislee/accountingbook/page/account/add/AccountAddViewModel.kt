package tw.idv.louislee.accountingbook.page.account.add

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import tw.idv.louislee.accountingbook.domain.service.AccountService
import tw.idv.louislee.accountingbook.state.AccountFormState

@KoinViewModel
class AccountAddViewModel(private val service: AccountService) : ViewModel() {
    val state = AccountFormState()

    fun add() = service.add(state.form)
}