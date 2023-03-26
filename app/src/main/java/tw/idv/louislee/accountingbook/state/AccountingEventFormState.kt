package tw.idv.louislee.accountingbook.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventFormDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType

class AccountingEventFormState(
    price: UInt = 0u,
    type: AccountingEventType = AccountingEventType.UNKNOWN_EXPENSES,
    note: String? = null
) {
    var price by mutableStateOf(price)
    var type by mutableStateOf(type)
    var isIncome by mutableStateOf(type.isIncome)
    var note by mutableStateOf(note ?: "")

    val form
        get() = AccountingEventFormDto(
            price = price.toLong(),
            type = type,
            note = if (note.isNotBlank()) {
                note.trim()
            } else {
                null
            }
        )
}
