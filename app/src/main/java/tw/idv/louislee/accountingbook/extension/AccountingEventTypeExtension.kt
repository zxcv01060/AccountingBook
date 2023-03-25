package tw.idv.louislee.accountingbook.extension

import androidx.annotation.StringRes
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType.*

@get:StringRes
val AccountingEventType.textId: Int
    get() = when (this) {
        UNKNOWN_INCOME, UNKNOWN_EXPENSES -> R.string.accounting_event_type_unknown
        SALARY -> R.string.accounting_event_type_salary
        BONUS -> R.string.accounting_event_type_bonus
        BILL -> R.string.accounting_event_type_bill
        FOOD_OR_DRINK -> R.string.accounting_event_type_food_or_drink
        LIFE -> R.string.accounting_event_type_life
        TRAFFIC -> R.string.accounting_event_type_traffic
        ENTERTAINMENT -> R.string.accounting_event_type_entertainment
        TAX -> R.string.accounting_event_type_tax
        WORK -> R.string.accounting_event_type_work
    }

