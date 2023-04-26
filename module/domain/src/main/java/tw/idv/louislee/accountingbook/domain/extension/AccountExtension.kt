package tw.idv.louislee.accountingbook.domain.extension

import tw.idv.louislee.accountingbook.domain.dto.account.AccountAccountingEventItemDto
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDetailDto
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDto
import tw.idv.louislee.accountingbook.domain.entity.Account

fun Account.toDto() = AccountDto(
    id = id,
    type = type,
    name = name,
    balance = balance
)

fun Account.toDetail(accountingEvents: List<AccountAccountingEventItemDto>) = AccountDetailDto(
    id = id,
    name = name,
    type = type,
    balance = balance,
    createDate = createDate,
    lastUpdateDate = lastUpdateDate,
    accountingEvents = accountingEvents
)
