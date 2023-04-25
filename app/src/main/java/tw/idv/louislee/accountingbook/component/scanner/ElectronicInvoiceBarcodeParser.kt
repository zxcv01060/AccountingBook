package tw.idv.louislee.accountingbook.component.scanner

import com.google.mlkit.vision.barcode.common.Barcode
import tw.idv.louislee.accountingbook.domain.dto.invoice.ElectronicInvoiceBarcodeEncoding
import tw.idv.louislee.accountingbook.dto.ElectronicInvoiceBarcodeDto
import tw.idv.louislee.accountingbook.dto.ElectronicInvoiceProductParcelableDto
import java.nio.charset.Charset
import java.time.LocalDate

object ElectronicInvoiceBarcodeParser {
    fun parse(barcodes: List<Barcode>): ElectronicInvoiceBarcodeDto? {
        if (barcodes.size != 2) {
            return null
        }

        var leftBarcode: Barcode? = null
        var rightBarcode: Barcode? = null
        for (barcode in barcodes) {
            if (barcode.rawValue?.startsWith("**") == true) {
                rightBarcode = barcode
            } else {
                leftBarcode = barcode
            }
        }

        if (leftBarcode == null || rightBarcode == null) {
            return null
        }

        val encoding = parseEncoding(barcode = leftBarcode.displayValue ?: "")
        val leftBarcodeRawText: String
        val rightBarcodeRawText: String
        when (encoding) {
            ElectronicInvoiceBarcodeEncoding.BIG_5 -> {
                leftBarcodeRawText = String(
                    leftBarcode.rawBytes ?: byteArrayOf(),
                    Charset.forName("BIG5")
                )
                rightBarcodeRawText = String(
                    rightBarcode.rawBytes ?: byteArrayOf(),
                    Charset.forName("BIG5")
                )
            }

            else -> {
                leftBarcodeRawText = leftBarcode.displayValue ?: ""
                rightBarcodeRawText = rightBarcode.displayValue ?: ""
            }
        }
        return parse(
            leftBarcode = leftBarcodeRawText,
            rightBarcode = rightBarcodeRawText,
            encoding = encoding
        )
    }

    private fun parseEncoding(barcode: String): ElectronicInvoiceBarcodeEncoding {
        val encodingColumn = barcode.split(':')[4]

        return ElectronicInvoiceBarcodeEncoding.values()[encodingColumn.toInt()]
    }

    private fun parse(
        leftBarcode: String,
        rightBarcode: String,
        encoding: ElectronicInvoiceBarcodeEncoding
    ): ElectronicInvoiceBarcodeDto {
        val barcode = leftBarcode + rightBarcode.substring(2)
        val secondAreaInformation = parseSecondAreaInformation(barcode.substring(89))

        return ElectronicInvoiceBarcodeDto(
            leftBarcode = leftBarcode,
            rightBarcode = rightBarcode,
            invoiceNumber = barcode.substring(0, 10),
            date = parseTaiwaneseDate(barcode.substring(10, 17)),
            randomCode = barcode.substring(17, 21),
            untaxedPrice = barcode.substring(21, 29)
                .takeIf { it != "00000000" }
                ?.toLong(radix = 16),
            price = barcode.substring(29, 37).toLong(radix = 16),
            buyerUnifiedBusinessNumber = barcode.substring(37, 45)
                .takeIf { it != "00000000" },
            sellerUnifiedBusinessNumber = barcode.substring(45, 53),
            verificationInformation = barcode.substring(53, 77),
            // 這兩個中間會卡一個冒號，要跳過
            sellerCustomInformation = barcode.substring(78, 88).takeIf { it != "**********" },
            // 這兩個中間會卡一個冒號，要跳過
            qrCodeProductCount = secondAreaInformation.qrCodeProductCount,
            invoiceProductCount = secondAreaInformation.invoiceProductCount,
            encoding = encoding,
            products = secondAreaInformation.products,
            additionalInformation = secondAreaInformation.additionalInformation,
        )
    }

    private fun parseSecondAreaInformation(barcode: String): ElectronicInvoiceBarcodeSecondArea {
        val barcodeColumns = barcode.split(":")

        // 略過編碼解析，因為一開始就已經處理了
        return ElectronicInvoiceBarcodeSecondArea(
            qrCodeProductCount = barcodeColumns[0].toInt(),
            invoiceProductCount = barcodeColumns[1].toInt(),
            products = parseProducts(barcodeColumns),
            additionalInformation = barcodeColumns.last()
        )
    }

    private fun parseProducts(barcodeColumns: List<String>): List<ElectronicInvoiceProductParcelableDto> {
        // 略過前面三個，分別是qr code產品數、發票產品數、編碼
        // 之後才是商品資訊
        val productFirstIndex = 3
        val result = mutableListOf<ElectronicInvoiceProductParcelableDto>()

        for (i in productFirstIndex until barcodeColumns.size step 3) {
            if (barcodeColumns.size - i < 3) {
                break
            }

            result.add(
                ElectronicInvoiceProductParcelableDto(
                    name = barcodeColumns[i],
                    count = barcodeColumns[i + 1].toDouble(),
                    unitPrice = barcodeColumns[i + 2].toDouble()
                )
            )
        }

        return result
    }

    private fun parseTaiwaneseDate(taiwaneseDate: String): LocalDate {
        val year = taiwaneseDate.substring(0, 3).toInt() + 1911
        val month = taiwaneseDate.substring(3, 5).toInt()
        val day = taiwaneseDate.substring(5).toInt()

        return LocalDate.of(year, month, day)
    }
}

private data class ElectronicInvoiceBarcodeSecondArea(
    /**
     * 左右條碼上面記載的商品數
     */
    val qrCodeProductCount: Int,
    /**
     * 發票上的商品數
     */
    val invoiceProductCount: Int,
    /**
     * 商品清單
     */
    val products: List<ElectronicInvoiceProductParcelableDto>,
    /**
     * 營業人的補充資訊
     */
    val additionalInformation: String
)
