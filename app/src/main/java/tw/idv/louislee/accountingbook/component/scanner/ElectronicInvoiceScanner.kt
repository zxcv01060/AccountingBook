package tw.idv.louislee.accountingbook.component.scanner

import android.Manifest
import android.content.Context
import android.view.View
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import tw.idv.louislee.accountingbook.AndroidLogger
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.domain.Logger
import tw.idv.louislee.accountingbook.dto.ElectronicInvoiceBarcodeDto
import tw.idv.louislee.accountingbook.extension.finish
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ElectronicInvoiceScanner(
    logger: Logger,
    onCameraPermissionDenied: () -> Unit,
    onScan: (electronicInvoiceBarcode: ElectronicInvoiceBarcodeDto) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
        Content(modifier = modifier, logger = logger, onScan = onScan)
        return
    }

    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = {
            Button(onClick = cameraPermissionState::launchPermissionRequest) {
                Text(text = stringResource(id = R.string.common_ok))
            }
        },
        dismissButton = {
            Button(onClick = onCameraPermissionDenied) {
                Text(text = stringResource(id = R.string.common_back))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.common_request_permission))
        },
        text = {
            Text(text = stringResource(id = R.string.common_camera_permission_request_message))
        }
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    logger: Logger,
    onScan: (electronicInvoiceBarcode: ElectronicInvoiceBarcodeDto) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { createCameraView(logger, it, onScan) }
    )
}

private fun createCameraView(
    logger: Logger,
    context: Context,
    onScan: (electronicInvoiceBarcode: ElectronicInvoiceBarcodeDto) -> Unit
): View {
    val previewView = PreviewView(context).also {
        it.scaleType = PreviewView.ScaleType.FILL_CENTER
    }
    val cameraController = LifecycleCameraController(context)

    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    val barcodeScanner = BarcodeScanning.getClient(options)
    val imageAnalyzer = MlKitAnalyzer(
        listOf(barcodeScanner),
        CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED,
        ContextCompat.getMainExecutor(context)
    ) { result: MlKitAnalyzer.Result? ->
        val barcodeResults = result?.getValue(barcodeScanner)
        if (
            (barcodeResults == null) ||
            (barcodeResults.size == 0) ||
            (barcodeResults.first() == null)
        ) {
            return@MlKitAnalyzer
        }

        val invoice = ElectronicInvoiceBarcodeParser.parse(barcodeResults)
        if (invoice != null) {
            onScan(invoice)
        }
    }
    cameraController.setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(context), imageAnalyzer)
    cameraController.bindToLifecycle(context as LifecycleOwner)

    previewView.controller = cameraController

    return previewView
}

@AppPreview
@Composable
private fun PreviewScanner() {
    AccountingBookTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var electronicInvoiceBarcode by remember {
                mutableStateOf<ElectronicInvoiceBarcodeDto?>(null)
            }

            if (electronicInvoiceBarcode == null) {
                ElectronicInvoiceScanner(
                    logger = AndroidLogger(),
                    onCameraPermissionDenied = {},
                    onScan = { electronicInvoiceBarcode = it }
                )
            } else {
                val context = LocalContext.current
                val scrollState = rememberScrollState()

                AlertDialog(
                    onDismissRequest = context::finish,
                    title = { Text(text = "掃描結果") },
                    text = {
                        data class InvoiceRow(val label: String, val content: String)

                        val rows = arrayOf(
                            InvoiceRow(
                                label = "發票號碼",
                                content = electronicInvoiceBarcode!!.invoiceNumber
                            ),
                            InvoiceRow(
                                label = "發票開立日期",
                                content = electronicInvoiceBarcode!!.date.format(
                                    DateTimeFormatter.ofPattern("yyyy/MM/dd")
                                )
                            ),
                            InvoiceRow(
                                label = "隨機碼",
                                content = electronicInvoiceBarcode!!.randomCode
                            ),
                            InvoiceRow(
                                label = "未稅的銷售額",
                                content = electronicInvoiceBarcode!!.untaxedPrice.toString()
                            ),
                            InvoiceRow(
                                label = "銷售額",
                                content = electronicInvoiceBarcode!!.price.toString()
                            ),
                            InvoiceRow(
                                label = "買方的統一編號",
                                content = electronicInvoiceBarcode!!.buyerUnifiedBusinessNumber.toString()
                            ),
                            InvoiceRow(
                                label = "賣方的統一編號",
                                content = electronicInvoiceBarcode!!.sellerUnifiedBusinessNumber
                            ),
                            InvoiceRow(
                                label = "加密驗證資訊",
                                content = electronicInvoiceBarcode!!.verificationInformation
                            ),
                            InvoiceRow(
                                label = "營業人自行使用區",
                                content = electronicInvoiceBarcode!!.sellerCustomInformation.toString()
                            ),
                            InvoiceRow(
                                label = "左右條碼上面記載的商品數",
                                content = electronicInvoiceBarcode!!.qrCodeProductCount.toString()
                            ),
                            InvoiceRow(
                                label = "發票上的商品數",
                                content = electronicInvoiceBarcode!!.invoiceProductCount.toString()
                            ),
                            InvoiceRow(
                                label = "商品資訊的編碼",
                                content = electronicInvoiceBarcode!!.encoding.name
                            ),
                            InvoiceRow(
                                label = "營業人的補充資訊",
                                content = electronicInvoiceBarcode!!.additionalInformation
                            ),
                        )
                        val productRows = electronicInvoiceBarcode!!.products
                            .mapIndexed { index, product ->
                                InvoiceRow(
                                    label = "商品${index + 1}",
                                    content = "名稱：${product.name}，數量：${product.count}，單價：${product.unitPrice}"
                                )
                            }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (row in rows) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(text = row.label)

                                    Text(text = row.content)
                                }
                            }

                            Divider()

                            for (productRow in productRows) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(text = productRow.label)

                                    Text(text = productRow.content)
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = context::finish) {
                            Text(text = "關閉")
                        }
                    }
                )
            }

        }
    }
}
