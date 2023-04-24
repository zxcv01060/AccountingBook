package tw.idv.louislee.accountingbook.domain.entity

data class InvoiceWithProduct(
    val invoice: Invoice,
    val product: List<InvoiceProduct>
)
