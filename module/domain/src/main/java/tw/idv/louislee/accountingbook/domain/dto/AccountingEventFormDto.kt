package tw.idv.louislee.accountingbook.domain.dto

import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType

data class AccountingEventFormDto(
    var type: AccountingEventType = AccountingEventType.UNKNOWN_EXPENSES,
    var price: Long = 0,
    var note: String? = null
)
