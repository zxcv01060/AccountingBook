package tw.idv.louislee.accountingbook.domain.dto.account

import tw.idv.louislee.accountingbook.domain.entity.AccountType

data class AccountDto(
    val id: Long,
    val name: String,
    val type: AccountType,
    val balance: Long
)
