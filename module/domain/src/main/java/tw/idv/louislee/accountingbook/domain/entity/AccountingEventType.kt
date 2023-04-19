package tw.idv.louislee.accountingbook.domain.entity

import com.squareup.sqldelight.ColumnAdapter

enum class AccountingEventType(val isIncome: Boolean = false) {
    /**
     * 未分類收入(預設)
     */
    UNKNOWN_INCOME(isIncome = true),

    /**
     * 未分類支出(預設)
     */
    UNKNOWN_EXPENSES,

    /**
     * 薪水
     */
    SALARY(isIncome = true),

    /**
     * 獎金
     */
    BONUS(isIncome = true),

    /**
     * 發票
     */
    INVOICE(isIncome = true),

    /**
     * 存款
     */
    SAVE_MONEY(isIncome = true),

    /**
     * 食物、飲料
     */
    FOOD_OR_DRINK,

    /**
     * 生活費
     */
    LIFE,

    /**
     * 交通費
     */
    TRAFFIC,

    /**
     * 娛樂費
     */
    ENTERTAINMENT,

    /**
     * 税
     */
    TAX,

    /**
     * 工作
     */
    WORK
}

fun Array<AccountingEventType>.filter(isIncome: Boolean) = filter {
    if (isIncome) {
        it.isIncome
    } else {
        !it.isIncome
    }
}

fun Iterable<AccountingEventType>.filter(isIncome: Boolean) = filter {
    if (isIncome) {
        it.isIncome
    } else {
        !it.isIncome
    }
}

internal object AccountingEventTypeAdapter : ColumnAdapter<AccountingEventType, Long> {
    override fun decode(databaseValue: Long): AccountingEventType =
        AccountingEventType.values()[databaseValue.toInt()]

    override fun encode(value: AccountingEventType): Long = value.ordinal.toLong()
}
