package tw.idv.louislee.accountingbook.domain.extension

import tw.idv.louislee.accountingbook.domain.dto.ElectronicInvoiceProductDto
import tw.idv.louislee.accountingbook.domain.entity.InvoiceProduct

val InvoiceProduct.dto
    get() = ElectronicInvoiceProductDto(
        name = name,
        unitPrice = unitPrice,
        count = count
    )
