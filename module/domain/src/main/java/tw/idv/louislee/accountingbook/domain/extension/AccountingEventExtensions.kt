package tw.idv.louislee.accountingbook.domain.extension

import tw.idv.louislee.accountingbook.domain.dto.ElectronicInvoiceDto
import tw.idv.louislee.accountingbook.domain.dto.accountingevent.AccountingEventDetailDto
import tw.idv.louislee.accountingbook.domain.dto.accountingevent.AccountingEventDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEvent

fun AccountingEvent.toDto() = AccountingEventDto(
    id = id,
    type = type,
    price = price,
    note = note
)

fun AccountingEvent.toDetail(invoice: ElectronicInvoiceDto?) = AccountingEventDetailDto(
    id = id,
    accountId = accountId,
    invoice = invoice,
    type = type,
    price = price,
    note = note,
    recordDate = recordDate,
    createDate = createDate,
    lastUpdateDate = lastUpdateDate
)
