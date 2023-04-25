package tw.idv.louislee.accountingbook.domain.utils

import tw.idv.louislee.accountingbook.domain.dto.invoice.ElectronicInvoiceBarcodeEncoding
import tw.idv.louislee.accountingbook.domain.dto.invoice.ElectronicInvoiceDto
import tw.idv.louislee.accountingbook.domain.dto.invoice.ElectronicInvoiceProductDto
import java.time.LocalDate

object ElectronicInvoiceBarcodeParser {
    fun parseEncoding(leftBarcode: String): ElectronicInvoiceBarcodeEncoding {
        val encodingColumn = leftBarcode.split(':')[4]

        return ElectronicInvoiceBarcodeEncoding.values()[encodingColumn.toInt()]
    }

    fun parse(
        leftBarcode: String,
        rightBarcode: String,
        encoding: ElectronicInvoiceBarcodeEncoding
    ): ElectronicInvoiceDto {
        val barcode = leftBarcode + rightBarcode.substring(2)

        val invoiceNumber = barcode.substring(0, 10)
        val date = parseTaiwaneseDate(barcode.substring(10, 17))
        val randomCode = barcode.substring(17, 21)
        val untaxedPrice = barcode.substring(21, 29)
            .takeIf { it != "00000000" }
            ?.toLong(radix = 16)
        val price = barcode.substring(29, 37).toLong(radix = 16)
        val buyerUnifiedBusinessNumber = barcode.substring(37, 45)
            .takeIf { it != "00000000" }
        val sellerUnifiedBusinessNumber = barcode.substring(45, 53)
        val verificationInformation = barcode.substring(53, 77)
        // 這兩個中間會卡一個冒號，要跳過
        val sellerCustomInformation = barcode.substring(78, 88).takeIf { it != "**********" }
        // 這兩個中間會卡一個冒號，要跳過
        val productColumns = barcode.substring(89).split(":")
        val qrCodeProductCount = productColumns[0].toInt()
        val invoiceProductCount = productColumns[1].toInt()
        val products = parseProducts(productColumns)
        val additionalInformation = productColumns.last()

        return ElectronicInvoiceDto(
            leftBarcode = leftBarcode,
            rightBarcode = rightBarcode,
            invoiceNumber = invoiceNumber,
            date = date,
            randomCode = randomCode,
            untaxedPrice = untaxedPrice,
            price = price,
            buyerUnifiedBusinessNumber = buyerUnifiedBusinessNumber,
            sellerUnifiedBusinessNumber = sellerUnifiedBusinessNumber,
            verificationInformation = verificationInformation,
            sellerCustomInformation = sellerCustomInformation,
            qrCodeProductCount = qrCodeProductCount,
            invoiceProductCount = invoiceProductCount,
            encoding = encoding,
            products = products,
            additionalInformation = additionalInformation,
        )
    }

    private fun parseProducts(barcodeColumns: List<String>): List<ElectronicInvoiceProductDto> {
        // 略過前面三個，分別是qr code產品數、發票產品數、編碼
        // 之後才是商品資訊
        val productFirstIndex = 3
        val result = mutableListOf<ElectronicInvoiceProductDto>()

        for (i in productFirstIndex until barcodeColumns.size step 3) {
            if (barcodeColumns.size - i < 3) {
                break
            }

            result.add(
                ElectronicInvoiceProductDto(
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