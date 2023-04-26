package tw.idv.louislee.accountingbook.domain.extension

import tw.idv.louislee.accountingbook.domain.dto.invoice.ElectronicInvoiceDto
import tw.idv.louislee.accountingbook.domain.entity.Invoice
import tw.idv.louislee.accountingbook.domain.entity.InvoiceProduct

fun Invoice.toDto(products: List<InvoiceProduct>) = ElectronicInvoiceDto(
    leftBarcode = leftBarcode,
    rightBarcode = rightBarcode,
    invoiceNumber = id,
    date = date,
    randomCode = randomCode,
    untaxedPrice = untaxedPrice,
    price = price,
    buyerUnifiedBusinessNumber = buyerUnifiedBusinessNumber,
    sellerUnifiedBusinessNumber = sellerUnifiedBusinessNumber,
    verificationInformation = verificationInformation,
    sellerCustomInformation = sellerCustomInformation,
    qrCodeProductCount = qrCodeProductCount.toInt(),
    invoiceProductCount = invoiceProductCount.toInt(),
    encoding = encoding,
    products = products.map { it.dto },
    additionalInformation = additionalInformation
)
