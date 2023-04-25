package tw.idv.louislee.accountingbook.domain.dto.invoice

import java.time.LocalDate

data class ElectronicInvoiceDto(
    /**
     * 左邊條碼的文字內容，編碼固定為UTF-8
     */
    val leftBarcode: String,
    /**
     * 右邊條碼的文字內容，編碼固定為UTF-8
     */
    val rightBarcode: String,
    /**
     * 發票號碼
     */
    val invoiceNumber: String,
    /**
     * 發票開立日期
     */
    val date: LocalDate,
    /**
     * 隨機碼
     */
    val randomCode: String,
    /**
     * 未稅的銷售額，如果"營業人銷售系統無法順利將稅項分離計算"時為null
     */
    val untaxedPrice: Long?,
    /**
     * 銷售額
     */
    val price: Long,
    /**
     * 買方的統一編號，一般消費者為null
     */
    val buyerUnifiedBusinessNumber: String?,
    /**
     * 賣方的統一編號
     */
    val sellerUnifiedBusinessNumber: String,
    /**
     * 加密驗證資訊，把[invoiceNumber]與[randomCode]拼接後以AES加密並以Base64編碼
     */
    val verificationInformation: String,
    /**
     * 營業人自行使用區
     */
    val sellerCustomInformation: String?,
    /**
     * 左右條碼上面記載的商品數
     */
    val qrCodeProductCount: Int,
    /**
     * 發票上的商品數
     */
    val invoiceProductCount: Int,
    /**
     * 商品資訊的編碼
     */
    val encoding: ElectronicInvoiceBarcodeEncoding,
    /**
     * 商品清單
     */
    val products: List<ElectronicInvoiceProductDto>,
    /**
     * 營業人的補充資訊
     */
    val additionalInformation: String?
)

enum class ElectronicInvoiceBarcodeEncoding {
    BIG_5,
    UTF_8,
    BASE_64;

    internal class ColumnAdapter :
        com.squareup.sqldelight.ColumnAdapter<ElectronicInvoiceBarcodeEncoding, Long> {
        override fun decode(databaseValue: Long): ElectronicInvoiceBarcodeEncoding =
            values()[databaseValue.toInt()]

        override fun encode(value: ElectronicInvoiceBarcodeEncoding): Long = value.ordinal.toLong()
    }
}

data class ElectronicInvoiceProductDto(
    val name: String,
    /**
     * 數量，有可能會有小數點，比如加油加0.98公升
     */
    val count: Double,
    /**
     * 單價，有可能會有小數點，比如說加油一公升29.8元
     */
    val unitPrice: Double
) {
    val totalPrice = unitPrice * count
}
