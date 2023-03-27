package tw.idv.louislee.accountingbook.domain.entity.migration

import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.Logger

@Single
internal class AccountingEventBalanceMigration(private val logger: Logger) : Migration {
    override val afterVersion: Int = 1

    override fun migrate(driver: SqlDriver) {
        val cursor = driver.executeQuery(
            null,
            "SELECT id, isIncome, price FROM AccountingEvent;",
            0
        )

        cursor.use {
            var balance = 0L

            while (it.next()) {
                val id = it.getLong(0)!!
                val isIncome = it.getLong(1) == 1L
                val price = it.getLong(2)!!

                logger.debug("id: $id")
                logger.debug("isIncome: $isIncome")
                logger.debug("price: $price")

                balance += if (isIncome) {
                    price
                } else {
                    -price
                }

                logger.debug("balance: $balance")

                driver.execute(
                    null,
                    "UPDATE AccountingEvent SET balance = ? WHERE id = ?;",
                    2
                ) {
                    bindLong(1, balance)
                    bindLong(2, id)
                }
            }
        }
    }
}