package tw.idv.louislee.accountingbook.domain.dto.account

import tw.idv.louislee.accountingbook.domain.entity.AccountType

data class AccountFormDto(
    val name: String,
    val type: AccountType,
    val balance: Long
)
