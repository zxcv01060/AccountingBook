package tw.idv.louislee.accountingbook.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import tw.idv.louislee.accountingbook.domain.dto.accountingevent.AccountingEventDetailDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import java.time.ZonedDateTime

val AccountingEventDetailDto.parcelable
    get() = AccountingEventParcelableDto(
        id = id,
        accountId = accountId,
        type = type,
        price = price,
        recordDate = recordDate,
        note = note
    )

@Parcelize
data class AccountingEventParcelableDto(
    val id: Long,
    val accountId: Long,
    val type: AccountingEventType,
    val price: Long,
    val recordDate: ZonedDateTime,
    val note: String?
) : Parcelable
