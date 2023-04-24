package tw.idv.louislee.accountingbook.domain.dto

import tw.idv.louislee.accountingbook.domain.DomainConstant
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import java.time.ZonedDateTime

data class AccountingEventFormDto(
    var type: AccountingEventType = AccountingEventType.UNKNOWN_EXPENSES,
    var price: Long = 0,
    var accountId: Long = DomainConstant.CASH_ACCOUNT_ID,
    var invoiceId: String? = null,
    var note: String? = null,
    var recordDate: ZonedDateTime? = null
)
