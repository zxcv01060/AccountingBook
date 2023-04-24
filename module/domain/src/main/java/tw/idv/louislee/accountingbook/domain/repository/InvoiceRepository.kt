package tw.idv.louislee.accountingbook.domain.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.entity.Invoice
import tw.idv.louislee.accountingbook.domain.entity.InvoiceQueries
import kotlin.coroutines.CoroutineContext

internal interface InvoiceRepository {
    fun findById(
        id: String,
        context: CoroutineContext = Dispatchers.Default
    ): Flow<Invoice?>
}

@Single
internal class InvoiceRepositoryImpl(private val query: InvoiceQueries) : InvoiceRepository {
    override fun findById(id: String, context: CoroutineContext): Flow<Invoice?> =
        query.findById(id = id)
            .asFlow()
            .mapToOneOrNull(context)
}
