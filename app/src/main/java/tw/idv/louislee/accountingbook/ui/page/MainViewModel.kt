package tw.idv.louislee.accountingbook.ui.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.android.annotation.KoinViewModel
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventFormDto
import tw.idv.louislee.accountingbook.domain.service.AccountingEventService

@KoinViewModel
class MainViewModel(private val service: AccountingEventService) : ViewModel() {
    fun findAll() = service.findAll(viewModelScope.coroutineContext)

    fun add(event: AccountingEventFormDto) = service.add(event)
}