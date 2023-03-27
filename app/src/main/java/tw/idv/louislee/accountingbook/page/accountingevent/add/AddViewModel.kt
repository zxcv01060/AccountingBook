package tw.idv.louislee.accountingbook.page.accountingevent.add

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventFormDto
import tw.idv.louislee.accountingbook.domain.service.AccountingEventService

@KoinViewModel
class AddViewModel(private val accountingEventService: AccountingEventService) : ViewModel() {
    fun add(accountId: Long, form: AccountingEventFormDto) =
        accountingEventService.add(accountId, form)
}