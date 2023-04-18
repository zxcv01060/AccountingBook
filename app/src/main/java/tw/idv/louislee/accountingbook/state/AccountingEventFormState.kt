package tw.idv.louislee.accountingbook.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import tw.idv.louislee.accountingbook.domain.DomainConstant
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventFormDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import java.time.ZonedDateTime

class AccountingEventFormState(
    price: UInt = 0u,
    type: AccountingEventType = AccountingEventType.UNKNOWN_EXPENSES,
    accountId: Long = DomainConstant.CASH_ACCOUNT_ID,
    note: String? = null,
    recordDate: ZonedDateTime? = null
) {
    var price by mutableStateOf(price)
    var type by mutableStateOf(type)
    var isIncome by mutableStateOf(type.isIncome)
    var accountId by mutableStateOf(accountId)
    var note by mutableStateOf(note ?: "")
    var recordDate by mutableStateOf(recordDate)

    val form
        get() = AccountingEventFormDto(
            price = price.toLong(),
            type = type,
            accountId = accountId,
            note = if (note.isNotBlank()) {
                note.trim()
            } else {
                null
            },
            recordDate = recordDate
        )
}
