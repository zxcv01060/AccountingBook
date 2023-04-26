package tw.idv.louislee.accountingbook.domain.dto.invoice

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class ElectronicInvoiceBarcodeDto(
    val leftBarcode: ByteArray,
    val rightBarcode: ByteArray
) {
    private companion object {
        private const val ENCODING_TEXT_INDEX = 4
    }

    val encoding by lazy {
        val leftBarcodeText = String(leftBarcode)
        val encodingColumn = leftBarcodeText.split(':')[ENCODING_TEXT_INDEX]

        ElectronicInvoiceBarcodeEncoding.values()[encodingColumn.toInt()]
    }
    val leftBarcodeText by lazy {
        toBarcodeText(leftBarcode)
    }
    val rightBarcodeText by lazy { toBarcodeText(rightBarcode) }
    val barcodeText by lazy { leftBarcodeText + rightBarcodeText.substring(2) }

    private fun toBarcodeText(barcode: ByteArray) = String(
        barcode,
        if (encoding == ElectronicInvoiceBarcodeEncoding.BIG_5) {
            Charset.forName("BIG5")
        } else {
            StandardCharsets.UTF_8
        }
    )
}
