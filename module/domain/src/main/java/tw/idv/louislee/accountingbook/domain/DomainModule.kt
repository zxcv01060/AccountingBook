package tw.idv.louislee.accountingbook.domain

import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import tw.idv.louislee.accountingbook.domain.dto.ElectronicInvoiceBarcodeEncoding
import tw.idv.louislee.accountingbook.domain.entity.Account
import tw.idv.louislee.accountingbook.domain.entity.AccountTypeAdapter
import tw.idv.louislee.accountingbook.domain.entity.AccountingBookDatabase
import tw.idv.louislee.accountingbook.domain.entity.AccountingEvent
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventTypeAdapter
import tw.idv.louislee.accountingbook.domain.entity.Invoice
import tw.idv.louislee.accountingbook.domain.entity.adapter.DateTimeAdapter
import tw.idv.louislee.accountingbook.domain.entity.adapter.InvoiceDateAdapter
import tw.idv.louislee.accountingbook.domain.repository.RepositoryModule

@Module(includes = [RepositoryModule::class])
@ComponentScan
class DomainModule {
    @Single
    fun getDatabaseSchema() = AccountingBookDatabase.Schema

    @Single
    fun createDatabase(driver: SqlDriver): AccountingBookDatabase {
        val dateTimeAdapter = DateTimeAdapter()

        return AccountingBookDatabase(
            driver = driver,
            AccountAdapter = Account.Adapter(
                typeAdapter = AccountTypeAdapter(),
                createDateAdapter = dateTimeAdapter,
                lastUpdateDateAdapter = dateTimeAdapter
            ),
            AccountingEventAdapter = AccountingEvent.Adapter(
                typeAdapter = AccountingEventTypeAdapter,
                recordDateAdapter = dateTimeAdapter,
                createDateAdapter = dateTimeAdapter,
                lastUpdateDateAdapter = dateTimeAdapter
            ),
            InvoiceAdapter = Invoice.Adapter(
                dateAdapter = InvoiceDateAdapter(),
                encodingAdapter = ElectronicInvoiceBarcodeEncoding.ColumnAdapter(),
                createDateAdapter = dateTimeAdapter
            )
        )
    }
}
