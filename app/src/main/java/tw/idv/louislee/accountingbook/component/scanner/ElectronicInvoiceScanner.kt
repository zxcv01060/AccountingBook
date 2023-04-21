package tw.idv.louislee.accountingbook.component.scanner

import android.Manifest
import android.content.Context
import android.view.View
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ElectronicInvoiceScanner(
    logger: Logger,
    onCameraPermissionDenied: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
        Content(modifier = modifier, logger = logger)
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
private fun Content(modifier: Modifier, logger: Logger) {
    AndroidView(
        modifier = Modifier.size(width = 200.dp, height = 200.dp) then modifier,
        factory = { createCameraView(logger, it) }
    )
}

private fun createCameraView(logger: Logger, context: Context): View {
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
            println()
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
            ElectronicInvoiceScanner(logger = AndroidLogger(), onCameraPermissionDenied = {})
        }
    }
}
