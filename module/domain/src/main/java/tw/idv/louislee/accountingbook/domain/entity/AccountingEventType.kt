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
    BILL(isIncome = true),

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

internal object AccountingEventTypeAdapter : ColumnAdapter<AccountingEventType, Long> {
    override fun decode(databaseValue: Long): AccountingEventType =
        AccountingEventType.values()[databaseValue.toInt()]

    override fun encode(value: AccountingEventType): Long = value.ordinal.toLong()
}
