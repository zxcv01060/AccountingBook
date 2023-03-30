package tw.idv.louislee.accountingbook.extension

import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.domain.entity.AccountType
import tw.idv.louislee.accountingbook.domain.entity.AccountType.*

val AccountType.titleId: Int
    get() = when (this) {
        CASH -> R.string.account_type_cash
        BANK -> R.string.account_type_bank
        CREDIT_CARD -> R.string.account_type_credit_card
    }
