package tw.idv.louislee.accountingbook.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventDetailDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType

val AccountingEventDetailDto.parcelable
    get() = AccountingEventParcelableDto(
        id = id,
        accountId = accountId,
        type = type,
        price = price,
        note = note
    )

@Parcelize
data class AccountingEventParcelableDto(
    val id: Long,
    val accountId: Long,
    val type: AccountingEventType,
    val price: Long,
    val note: String?
) : Parcelable
