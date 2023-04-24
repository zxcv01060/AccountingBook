package tw.idv.louislee.accountingbook.page.accountingevent.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.domain.service.AccountService
import tw.idv.louislee.accountingbook.domain.service.AccountingEventService
import tw.idv.louislee.accountingbook.dto.ElectronicInvoiceBarcodeDto
import tw.idv.louislee.accountingbook.state.AccountingEventFormState
import tw.idv.louislee.accountingbook.utils.AppDateFormatter
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@KoinViewModel
class AccountingEventAddViewModel(
    private val accountingEventService: AccountingEventService,
    private val accountService: AccountService,
    private val dateFormatter: AppDateFormatter,
    @InjectedParam private val barcode: ElectronicInvoiceBarcodeDto?
) : ViewModel() {
    val state = if (barcode == null) {
        AccountingEventFormState()
    } else {
        AccountingEventFormState(
            price = barcode.price.toUInt(),
            type = AccountingEventType.UNKNOWN_EXPENSES,
            recordDate = ZonedDateTime.of(
                barcode.date,
                LocalTime.MIN,
                ZoneId.of("Asia/Taipei")
            )
        )
    }

    fun add() = accountingEventService.add(state.form, barcode?.form)

    fun findAllAccount(): Flow<List<AccountDto>> =
        accountService.findAll(viewModelScope.coroutineContext)
}