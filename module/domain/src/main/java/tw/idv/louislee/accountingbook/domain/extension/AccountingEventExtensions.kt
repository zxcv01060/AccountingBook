package tw.idv.louislee.accountingbook.domain.extension

import tw.idv.louislee.accountingbook.domain.dto.AccountingEventDetailDto
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEvent

fun AccountingEvent.toDto() = AccountingEventDto(
    id = id,
    type = type,
    price = price,
    note = note
)

fun AccountingEvent.toDetail() = AccountingEventDetailDto(
    id = id,
    accountId = accountId,
    type = type,
    price = price,
    note = note,
    createDate = createDate,
    lastUpdateDate = lastUpdateDate
)
