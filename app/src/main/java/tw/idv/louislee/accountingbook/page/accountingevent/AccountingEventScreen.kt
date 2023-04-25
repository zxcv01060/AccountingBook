package tw.idv.louislee.accountingbook.page.accountingevent

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.getViewModel
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.AppToolbarLayout
import tw.idv.louislee.accountingbook.component.PriceText
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.dto.ElectronicInvoiceBarcodeDto
import tw.idv.louislee.accountingbook.extension.getParcelableExtraCompat
import tw.idv.louislee.accountingbook.extension.startActivity
import tw.idv.louislee.accountingbook.extension.textId
import tw.idv.louislee.accountingbook.page.ElectronicInvoiceScannerActivity
import tw.idv.louislee.accountingbook.page.accountingevent.add.AccountingEventAddActivity
import tw.idv.louislee.accountingbook.page.accountingevent.detail.AccountingEventDetailActivity
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

@Composable
fun AccountingEventScreen(viewModel: AccountingEventViewModel = getViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val events by viewModel.findAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            context = coroutineScope.coroutineContext
        )

    AccountingEventScreen(events = events)
}

@Composable
fun AccountingEventScreen(events: List<AccountingEventDto>) {
    AccountingBookTheme {
        AppToolbarLayout(
            title = stringResource(id = R.string.accounting_event_list_title),
            actions = {
                val context = LocalContext.current
                val electronicInvoiceBarcodeScannerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult(),
                    onResult = {
                        context.startActivity<AccountingEventAddActivity> {
                            putExtra(
                                AccountingEventAddActivity.INTENT_ELECTRONIC_INVOICE_BARCODE,
                                it.data?.getParcelableExtraCompat<ElectronicInvoiceBarcodeDto>(
                                    ElectronicInvoiceScannerActivity.INTENT_BARCODE
                                )
                            )
                        }
                    }
                )

                AddAccountingEventButton(requestLaunchInvoiceScanner = {
                    electronicInvoiceBarcodeScannerLauncher.launch(
                        Intent(context, ElectronicInvoiceScannerActivity::class.java)
                    )
                })
            }
        ) {
            AccountingEventList(events = events)
        }
    }
}

@Composable
private fun AddAccountingEventButton(requestLaunchInvoiceScanner: () -> Unit) {
    val context = LocalContext.current
    var isExpanded by remember {
        mutableStateOf(false)
    }

    IconButton(onClick = { isExpanded = true }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.common_add)
        )
    }
    DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
        DropdownMenuItem(
            text = { Text("手動輸入") },
            onClick = {
                context.startActivity<AccountingEventAddActivity>()
                isExpanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(text = "掃描發票") },
            onClick = {
                requestLaunchInvoiceScanner()
                isExpanded = false
            }
        )
    }
}

@Composable
private fun AccountingEventList(events: List<AccountingEventDto>) {
    val context = LocalContext.current

    LazyColumn {
        items(events) {
            AccountingEventRow(event = it) {
                context.startActivity<AccountingEventDetailActivity> {
                    putExtra(AccountingEventDetailActivity.INTENT_ID, it.id)
                }
            }
        }
    }
}

@Composable
private fun AccountingEventRow(event: AccountingEventDto, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = event.type.textId))

            PriceText(price = event.price)
        }

        if (event.note?.isNotBlank() == true) {
            Row {
                Text(text = event.note ?: "")
            }
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    val events = listOf(
        AccountingEventDto(
            id = 1,
            price = -500,
            type = AccountingEventType.FOOD_OR_DRINK
        ),
        AccountingEventDto(
            id = 2,
            price = 2000,
            type = AccountingEventType.INVOICE,
            note = "2023年2、3月的發票中一張"
        )
    )

    AccountingEventScreen(events = events)
}
