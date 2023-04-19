package tw.idv.louislee.accountingbook.extension

import androidx.annotation.StringRes
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.BONUS
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.ENTERTAINMENT
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.FOOD_OR_DRINK
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.INVOICE
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.LIFE
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.SALARY
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.SAVE_MONEY
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.TAX
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.TRAFFIC
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.UNKNOWN_EXPENSES
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.UNKNOWN_INCOME
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.WORK

@get:StringRes
val AccountingEventType.textId: Int
    get() = when (this) {
        UNKNOWN_INCOME, UNKNOWN_EXPENSES -> R.string.accounting_event_type_unknown
        SALARY -> R.string.accounting_event_type_salary
        BONUS -> R.string.accounting_event_type_bonus
        INVOICE -> R.string.accounting_event_type_bill
        SAVE_MONEY -> R.string.accounting_event_type_save_money
        FOOD_OR_DRINK -> R.string.accounting_event_type_food_or_drink
        LIFE -> R.string.accounting_event_type_life
        TRAFFIC -> R.string.accounting_event_type_traffic
        ENTERTAINMENT -> R.string.accounting_event_type_entertainment
        TAX -> R.string.accounting_event_type_tax
        WORK -> R.string.accounting_event_type_work
    }

