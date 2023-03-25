package tw.idv.louislee.accountingbook.page.accountingevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventDto
import tw.idv.louislee.accountingbook.domain.service.AccountingEventService

interface AccountingEventViewModel {
    fun findAll(): Flow<List<AccountingEventDto>>
}

@KoinViewModel
class AccountingEventViewModelImpl(
    private val service: AccountingEventService
) : ViewModel(), AccountingEventViewModel {
    override fun findAll() = service.findAll(viewModelScope.coroutineContext)
}
