package tw.idv.louislee.accountingbook.page

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.get
import tw.idv.louislee.accountingbook.AndroidLogger
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.AppToolbarLayout
import tw.idv.louislee.accountingbook.component.scanner.ElectronicInvoiceScanner
import tw.idv.louislee.accountingbook.domain.Logger
import tw.idv.louislee.accountingbook.dto.ElectronicInvoiceBarcodeDto
import tw.idv.louislee.accountingbook.extension.finish
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

class ElectronicInvoiceScannerActivity : ComponentActivity() {
    companion object {
        const val INTENT_BARCODE = "ElectronicInvoiceScannerActivity.barcode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content(
                logger = get(),
                onScan = {
                    setResult(
                        RESULT_OK,
                        Intent().apply { putExtra(INTENT_BARCODE, it) }
                    )
                    finish()
                }
            )
        }
    }
}

@Composable
private fun Content(logger: Logger, onScan: (barcode: ElectronicInvoiceBarcodeDto) -> Unit) {
    AccountingBookTheme {
        AppToolbarLayout(title = stringResource(id = R.string.electronic_invoice_scanner_title)) {
            var isPermissionDenied by remember {
                mutableStateOf(false)
            }
            if (isPermissionDenied) {
                NoCameraPermissionDialog()
                return@AppToolbarLayout
            }

            ElectronicInvoiceScanner(
                logger = logger,
                onCameraPermissionDenied = { isPermissionDenied = true },
                onScan = onScan
            )
        }
    }
}

@Composable
private fun NoCameraPermissionDialog() {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = context::finish,
        title = { Text(text = stringResource(id = R.string.common_no_permission)) },
        text = { Text(text = stringResource(id = R.string.electronic_invoice_scanner_required_permission_message)) },
        confirmButton = {
            Button(onClick = context::finish) {
                Text(text = stringResource(id = R.string.common_ok))
            }
        }
    )
}

@AppPreview
@Composable
private fun Preview() {
    Content(logger = AndroidLogger(), onScan = {})
}
