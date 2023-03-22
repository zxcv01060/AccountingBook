package tw.idv.louislee.accountingbook.domain.dto

import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType

data class AccountingEventFormDto(
    val type: AccountingEventType,
    val price: Long,
    val note: String?
)
