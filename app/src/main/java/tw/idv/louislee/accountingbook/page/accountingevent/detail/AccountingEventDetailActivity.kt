package tw.idv.louislee.accountingbook.page.accountingevent.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.AppToolbarLayout
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventDetailDto
import tw.idv.louislee.accountingbook.domain.dto.ElectronicInvoiceBarcodeEncoding
import tw.idv.louislee.accountingbook.domain.dto.ElectronicInvoiceDto
import tw.idv.louislee.accountingbook.domain.dto.ElectronicInvoiceProductDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.dto.parcelable
import tw.idv.louislee.accountingbook.extension.finish
import tw.idv.louislee.accountingbook.extension.startActivity
import tw.idv.louislee.accountingbook.extension.textId
import tw.idv.louislee.accountingbook.page.accountingevent.edit.AccountingEventEditActivity
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview
import tw.idv.louislee.accountingbook.utils.AppDateFormatter
import tw.idv.louislee.accountingbook.utils.AppDateFormatterImpl
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class AccountingEventDetailActivity : ComponentActivity() {
    companion object {
        const val INTENT_ID = "accounting_event_detail.id"
        const val ID_NOT_FOUND = -1L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getLongExtra(INTENT_ID, ID_NOT_FOUND)
        if (id == ID_NOT_FOUND) {
            throw IllegalStateException("記帳詳細記錄頁面需要'INTENT_ID'參數")
        }
        setContent {
            val viewModel = getViewModel<AccountingEventDetailViewModel>()
            val event by viewModel.findById(id = id)
                .collectAsStateWithLifecycle(initialValue = AccountingEventDetailDto.placeholder)
            val context = LocalContext.current

            Content(
                id = id,
                event = event,
                onEditClick = {
                    context.startActivity<AccountingEventEditActivity> {
                        putExtra(AccountingEventEditActivity.INTENT_ID, event?.id)
                        putExtra(AccountingEventEditActivity.INTENT_EVENT, event?.parcelable)
                    }
                },
                onDeleteConfirm = {
                    viewModel.delete(id)
                    context.finish()
                }
            )
        }
    }
}

@Composable
private fun Content(
    id: Long,
    event: AccountingEventDetailDto?,
    dateFormatter: AppDateFormatter = get(),
    onEditClick: () -> Unit = {},
    onDeleteConfirm: () -> Unit = {}
) {
    val context = LocalContext.current
    var isShowingDeleteConfirmDialog by remember {
        mutableStateOf(false)
    }

    AccountingBookTheme {
        AppToolbarLayout(
            onNavigateBack = context::finish,
            title = stringResource(id = R.string.accounting_event_detail_title),
            actions = {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.common_edit)
                    )
                }
                IconButton(onClick = { isShowingDeleteConfirmDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.common_delete)
                    )
                }
            }
        ) {
            if (isShowingDeleteConfirmDialog) {
                DeleteConfirmDialog(
                    onDismissRequest = { isShowingDeleteConfirmDialog = false },
                    onConfirm = onDeleteConfirm
                )
            }

            AccountingEventDetail(id = id, event = event, dateFormatter = dateFormatter)
        }
    }
}

@Composable
private fun DeleteConfirmDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(id = R.string.accounting_event_detail_delete_title))
        },
        text = {
            Text(text = stringResource(id = R.string.accounting_event_detail_delete_message))
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = stringResource(id = R.string.common_ok))
            }
        },
        dismissButton = {
            Button(onClick = onConfirm) {
                Text(text = stringResource(id = R.string.common_cancel))
            }
        }
    )
}

@Composable
private fun AccountingEventDetail(
    id: Long,
    event: AccountingEventDetailDto?,
    dateFormatter: AppDateFormatter
) {
    if (event == null) {
        EventNotFoundDialog(id = id)
        return
    }

    data class DetailRow(val label: String, val content: String)

    val rows = arrayOf(
        DetailRow(
            label = stringResource(id = R.string.accounting_event_price),
            content = stringResource(id = R.string.common_price, event.price)
        ),
        DetailRow(
            label = stringResource(id = R.string.accounting_event_type),
            content = stringResource(id = event.type.textId)
        ),
        DetailRow(
            label = stringResource(id = R.string.common_create_date),
            content = dateFormatter.formatDateTime(event.createDate)
        ),
        DetailRow(
            label = stringResource(id = R.string.common_last_update_date),
            content = dateFormatter.formatDateTime(event.lastUpdateDate)
        ),
        DetailRow(
            label = stringResource(id = R.string.accounting_event_record_date),
            content = dateFormatter.formatDateTime(event.recordDate)
        ),
        DetailRow(
            label = stringResource(id = R.string.accounting_event_note),
            content = event.note ?: ""
        )
    )
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in rows) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.fillMaxWidth(), text = row.label)

                Text(modifier = Modifier.fillMaxWidth(), text = row.content)
            }
        }

        InvoiceDetail(invoice = event.invoice, dateFormatter = dateFormatter)
    }
}

@Composable
private fun EventNotFoundDialog(id: Long) {
    val context = LocalContext.current
    val onDismiss: () -> Unit = context::finish

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.common_error_title))
        },
        text = {
            Text(text = stringResource(id = R.string.accounting_event_detail_event_not_found, id))
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.common_ok))
            }
        }
    )
}

@Composable
fun InvoiceDetail(invoice: ElectronicInvoiceDto?, dateFormatter: AppDateFormatter) {
    if (invoice == null) {
        return
    }

    data class InvoiceRow(val label: String, val content: String)

    val rows = arrayOf(
        InvoiceRow(
            label = stringResource(id = R.string.electronic_invoice_invoice_number),
            content = invoice.invoiceNumber
        ),
        InvoiceRow(
            label = stringResource(id = R.string.electronic_invoice_date),
            content = dateFormatter.formatDate(invoice.date)
        ),
        InvoiceRow(
            label = stringResource(id = R.string.electronic_invoice_price),
            content = stringResource(id = R.string.common_price, invoice.price)
        ),
        InvoiceRow(
            label = stringResource(id = R.string.electronic_invoice_invoice_product_count),
            content = invoice.invoiceProductCount.toString()
        )
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (row in rows) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = row.label)

                Text(text = row.content)
            }
        }

        InvoiceProductTable(products = invoice.products)
    }
}

@Composable
fun InvoiceProductTable(products: List<ElectronicInvoiceProductDto>) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Column(
            modifier = Modifier.weight(weight = 4f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = stringResource(id = R.string.electronic_invoice_product_name))
            for (product in products) {
                Text(text = product.name)
            }
        }
        Column(
            modifier = Modifier.weight(weight = 2f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = stringResource(id = R.string.electronic_invoice_product_unit_price))
            for (product in products) {
                Text(text = stringResource(id = R.string.common_price, product.unitPrice))
            }
        }
        Column(
            modifier = Modifier.weight(weight = 2f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = stringResource(id = R.string.electronic_invoice_product_count))
            for (product in products) {
                Text(text = product.count.toString())
            }
        }
        Column(
            modifier = Modifier.weight(weight = 2f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = stringResource(id = R.string.electronic_invoice_product_total_price))
            for (product in products) {
                Text(text = stringResource(id = R.string.common_price, product.totalPrice))
            }
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    Content(
        id = 1,
        event = AccountingEventDetailDto(
            id = 1,
            accountId = 1,
            type = AccountingEventType.FOOD_OR_DRINK,
            price = 35,
            note = "早餐 巧克力吐司 + 奶茶",
            invoice = ElectronicInvoiceDto(
                leftBarcode = "",
                rightBarcode = "",
                invoiceNumber = "MF37884541",
                date = LocalDate.of(2023, 4, 3),
                randomCode = "2469",
                untaxedPrice = null,
                price = 111,
                buyerUnifiedBusinessNumber = null,
                sellerUnifiedBusinessNumber = "40020690",
                verificationInformation = "",
                sellerCustomInformation = null,
                qrCodeProductCount = 1,
                invoiceProductCount = 1,
                encoding = ElectronicInvoiceBarcodeEncoding.BIG_5,
                products = listOf(
                    ElectronicInvoiceProductDto(
                        name = "92無鉛汽油",
                        unitPrice = 3.29,
                        count = 1.298
                    )
                ),
                additionalInformation = ""
            ),
            recordDate = ZonedDateTime.of(
                2023, 5, 29,
                7, 29, 43, 0,
                ZoneId.of("Asia/Taipei")
            ),
            createDate = ZonedDateTime.of(
                2023, 5, 29,
                7, 29, 43, 0,
                ZoneId.of("Asia/Taipei")
            ),
            lastUpdateDate = ZonedDateTime.of(
                2023, 5, 29,
                19, 0, 19, 0,
                ZoneId.of("Asia/Taipei")
            )
        ),
        dateFormatter = AppDateFormatterImpl(context = LocalContext.current)
    )
}
