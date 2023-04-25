package tw.idv.louislee.accountingbook.domain.dto.invoice

data class ElectronicInvoiceBarcodeDto(
    val leftBarcode: String,
    val rightBarcode: String,
    val encoding: ElectronicInvoiceBarcodeEncoding
) {
    val rawText = leftBarcode + rightBarcode.substring(2)
}
