package tw.idv.louislee.accountingbook.domain.dto.accountingevent

import tw.idv.louislee.accountingbook.domain.dto.invoice.ElectronicInvoiceDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import java.time.ZoneId
import java.time.ZonedDateTime

data class AccountingEventDetailDto(
    val id: Long,
    val accountId: Long,
    val type: AccountingEventType,
    val price: Long,
    val recordDate: ZonedDateTime,
    val note: String?,
    val invoice: ElectronicInvoiceDto?,
    val createDate: ZonedDateTime,
    val lastUpdateDate: ZonedDateTime
) {
    companion object {
        val placeholder
            get() = AccountingEventDetailDto(
                id = Long.MAX_VALUE,
                accountId = Long.MAX_VALUE,
                type = AccountingEventType.UNKNOWN_EXPENSES,
                price = 0,
                note = null,
                invoice = null,
                recordDate = ZonedDateTime.of(
                    2023, 4, 9,
                    20, 14, 28,
                    0, ZoneId.systemDefault()
                ),
                createDate = ZonedDateTime.of(
                    2023, 4, 9,
                    20, 14, 28,
                    0, ZoneId.systemDefault()
                ),
                lastUpdateDate = ZonedDateTime.of(
                    2023, 4, 9,
                    20, 14, 28,
                    0, ZoneId.systemDefault()
                )
            )
    }
}
