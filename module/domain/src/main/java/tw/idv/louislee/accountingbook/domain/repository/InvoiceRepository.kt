package tw.idv.louislee.accountingbook.domain.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.Logger
import tw.idv.louislee.accountingbook.domain.dto.invoice.ElectronicInvoiceDto
import tw.idv.louislee.accountingbook.domain.entity.Invoice
import tw.idv.louislee.accountingbook.domain.entity.InvoiceProductQueries
import tw.idv.louislee.accountingbook.domain.entity.InvoiceQueries
import tw.idv.louislee.accountingbook.domain.utils.DateTimeProvider
import kotlin.coroutines.CoroutineContext

internal interface InvoiceRepository {
    fun findById(
        id: String,
        context: CoroutineContext = Dispatchers.Default
    ): Flow<Invoice?>

    fun add(electronicInvoice: ElectronicInvoiceDto)
}

@Single
internal class InvoiceRepositoryImpl(
    private val logger: Logger,
    private val dateTimeProvider: DateTimeProvider,
    private val query: InvoiceQueries,
    private val productQuery: InvoiceProductQueries
) : InvoiceRepository {
    override fun findById(id: String, context: CoroutineContext): Flow<Invoice?> =
        query.findById(id = id)
            .asFlow()
            .mapToOneOrNull(context)

    override fun add(electronicInvoice: ElectronicInvoiceDto) = query.transaction {
        val invoice = query.findById(id = electronicInvoice.invoiceNumber)
            .executeAsOneOrNull()
        if (invoice != null) {
            logger.warn("發票已存在，略過更新")

            return@transaction
        }

        query.add(
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
            productQuery.add(
                invoiceId = electronicInvoice.invoiceNumber,
                name = product.name,
                unitPrice = product.unitPrice,
                count = product.count
            )
        }
    }
}
