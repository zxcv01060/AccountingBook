package tw.idv.louislee.accountingbook.domain.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.entity.InvoiceProduct
import tw.idv.louislee.accountingbook.domain.entity.InvoiceProductQueries
import kotlin.coroutines.CoroutineContext

internal interface InvoiceProductRepository {
    fun findByInvoiceId(
        invoiceId: String,
        context: CoroutineContext = Dispatchers.Default
    ): Flow<List<InvoiceProduct>>
}

@Single
internal class InvoiceProductRepositoryImpl(
    private val query: InvoiceProductQueries
) : InvoiceProductRepository {
    override fun findByInvoiceId(
        invoiceId: String,
        context: CoroutineContext
    ): Flow<List<InvoiceProduct>> = query.findByInvoiceId(invoiceId = invoiceId)
        .asFlow()
        .mapToList(context = context)
}
