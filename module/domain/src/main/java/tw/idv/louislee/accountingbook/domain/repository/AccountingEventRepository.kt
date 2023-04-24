package tw.idv.louislee.accountingbook.domain.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventFormDto
import tw.idv.louislee.accountingbook.domain.dto.ElectronicInvoiceDto
import tw.idv.louislee.accountingbook.domain.entity.AccountQueries
import tw.idv.louislee.accountingbook.domain.entity.AccountingEvent
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventQueries
import tw.idv.louislee.accountingbook.domain.entity.Invoice
import tw.idv.louislee.accountingbook.domain.entity.InvoiceProductQueries
import tw.idv.louislee.accountingbook.domain.entity.InvoiceQueries
import tw.idv.louislee.accountingbook.domain.utils.DateTimeProvider
import kotlin.coroutines.CoroutineContext

internal interface AccountingEventRepository {
    fun findAll(context: CoroutineContext = Dispatchers.Default): Flow<List<AccountingEvent>>

    fun findById(id: Long, context: CoroutineContext = Dispatchers.Default): Flow<AccountingEvent?>

    fun findByAccountId(
        accountId: Long,
        context: CoroutineContext = Dispatchers.Default
    ): Flow<List<AccountingEvent>>

    fun add(event: AccountingEventFormDto, electronicInvoice: ElectronicInvoiceDto?)

    fun delete(id: Long)

    fun updateById(id: Long, event: AccountingEventFormDto)
}

@Single
internal class AccountingEventRepositoryImpl(
    private val dateTimeProvider: DateTimeProvider,
    private val query: AccountingEventQueries,
    private val accountQuery: AccountQueries,
    private val invoiceQuery: InvoiceQueries,
    private val invoiceProductQuery: InvoiceProductQueries
) : AccountingEventRepository {
    override fun findAll(context: CoroutineContext): Flow<List<AccountingEvent>> =
        query.findAll()
            .asFlow()
            .mapToList(context)

    override fun findById(id: Long, context: CoroutineContext): Flow<AccountingEvent?> =
        query.findById(id = id)
            .asFlow()
            .mapToOneOrNull(context)

    override fun findByAccountId(
        accountId: Long,
        context: CoroutineContext
    ): Flow<List<AccountingEvent>> = query.findByAccountId(accountId = accountId)
        .asFlow()
        .mapToList(context)

    override fun add(event: AccountingEventFormDto, electronicInvoice: ElectronicInvoiceDto?) =
        query.transaction {
            val price = if (event.type.isIncome) {
                event.price
            } else {
                -event.price
            }
            val newBalance =
                updateAccountBalance(accountId = event.accountId, balanceInterval = price)

            if (electronicInvoice != null) {
                invoiceQuery.add(
                    Invoice(
                        id = electronicInvoice.invoiceNumber,
                        date = electronicInvoice.date,
                        randomCode = electronicInvoice.randomCode,
                        untaxedPrice = electronicInvoice.untaxedPrice,
                        price = electronicInvoice.price,
                        buyerUnifiedBusinessNumber = electronicInvoice.buyerUnifiedBusinessNumber,
                        sellerUnifiedBusinessNumber = electronicInvoice.sellerUnifiedBusinessNumber,
                        verificationInformation = electronicInvoice.verificationInformation,
                        sellerCustomInformation = electronicInvoice.sellerCustomInformation,
                        qrCodeProductCount = electronicInvoice.qrCodeProductCount.toLong(),
                        invoiceProductCount = electronicInvoice.invoiceProductCount.toLong(),
                        encoding = electronicInvoice.encoding,
                        additionalInformation = electronicInvoice.additionalInformation,
                        leftBarcode = electronicInvoice.leftBarcode,
                        rightBarcode = electronicInvoice.rightBarcode,
                        createDate = dateTimeProvider.now,
                    )
                )
                for (product in electronicInvoice.products) {
                    invoiceProductQuery.add(
                        invoiceId = electronicInvoice.invoiceNumber,
                        name = product.name,
                        unitPrice = product.unitPrice,
                        count = product.count
                    )
                }
            }
            query.add(
                accountId = event.accountId,
                invoiceId = electronicInvoice?.invoiceNumber,
                type = event.type,
                isIncome = event.type.isIncome,
                price = price,
                balance = newBalance,
                note = event.note,
                recordDate = event.recordDate ?: dateTimeProvider.now,
                createDate = dateTimeProvider.now
            )
        }

    private fun updateAccountBalance(accountId: Long, balanceInterval: Long): Long {
        val accountBalance = accountQuery.findBalanceById(id = accountId).executeAsOne()
        if (balanceInterval == 0L) {
            return accountBalance
        }

        val balance = accountBalance + balanceInterval
        accountQuery.updateBalanceById(
            id = accountId,
            balance = balance,
            lastUpdateDate = dateTimeProvider.now
        )

        return balance
    }

    override fun delete(id: Long) = query.transaction {
        val event = query.findById(id = id)
            .executeAsOneOrNull() ?: return@transaction
        updateAccountBalance(
            accountId = event.accountId,
            balanceInterval = -event.price
        )

        query.plusBalanceByIdGreaterThan(
            id = id,
            price = -event.price,
            lastUpdateDate = dateTimeProvider.now
        )
        query.deleteById(id = id)
        if (event.invoiceId != null) {
            invoiceQuery.deleteById(invoiceId = event.invoiceId)
        }
    }

    override fun updateById(id: Long, event: AccountingEventFormDto) = query.transaction {
        val originalEvent = query.findById(id = id)
            .executeAsOneOrNull() ?: return@transaction
        val price = if (event.type.isIncome) {
            event.price
        } else {
            -event.price
        }
        //  原       新
        // +100 -> +300 = +200
        // +100 -> +50  = -50
        // +100 -> -200 = -300
        // +100 -> +100 = +0
        // -100 -> -300 = -200
        // -100 -> -50  = +50
        val newBalance = updateAccountBalance(
            accountId = event.accountId,
            balanceInterval = price - originalEvent.price
        )

        query.plusBalanceByIdGreaterThan(
            id = id,
            price = -event.price,
            lastUpdateDate = dateTimeProvider.now
        )
        query.updateById(
            id = id,
            accountId = event.accountId,
            invoiceId = event.invoiceId,
            type = event.type,
            isIncome = event.type.isIncome,
            price = price,
            balance = newBalance,
            note = event.note,
            recordDate = event.recordDate ?: originalEvent.recordDate,
            lastUpdateDate = dateTimeProvider.now
        )
    }
}
