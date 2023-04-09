package tw.idv.louislee.accountingbook.page.accountingevent.detail

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import tw.idv.louislee.accountingbook.domain.service.AccountingEventService

@KoinViewModel
class AccountingEventDetailViewModel(private val service: AccountingEventService) : ViewModel() {
    fun findById(id: Long) = service.findById(id = id)
}