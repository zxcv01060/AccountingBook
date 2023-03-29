package tw.idv.louislee.accountingbook.page.accountingevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.android.annotation.KoinViewModel
import tw.idv.louislee.accountingbook.domain.service.AccountingEventService

@KoinViewModel
class AccountingEventViewModel(private val service: AccountingEventService) : ViewModel() {
    fun findAll() = service.findAll(viewModelScope.coroutineContext)
}
