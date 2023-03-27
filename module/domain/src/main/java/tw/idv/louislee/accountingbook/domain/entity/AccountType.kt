package tw.idv.louislee.accountingbook.domain.entity

import com.squareup.sqldelight.ColumnAdapter

enum class AccountType {
    CASH,
    BANK,
    CREDIT_CARD
}

internal class AccountTypeAdapter : ColumnAdapter<AccountType, Long> {
    override fun decode(databaseValue: Long): AccountType =
        AccountType.values()[databaseValue.toInt()]

    override fun encode(value: AccountType): Long = value.ordinal.toLong()
}
