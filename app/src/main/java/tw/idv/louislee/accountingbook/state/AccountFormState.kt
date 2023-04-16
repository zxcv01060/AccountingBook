package tw.idv.louislee.accountingbook.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import tw.idv.louislee.accountingbook.domain.dto.account.AccountFormDto
import tw.idv.louislee.accountingbook.domain.entity.AccountType

class AccountFormState(
    name: String = "",
    type: AccountType = AccountType.CASH,
    balance: UInt = 0u
) {
    var name by mutableStateOf(name)
    var type by mutableStateOf(type)
    var balance by mutableStateOf(balance)

    val form get() = AccountFormDto(name = name, type = type, balance = balance.toLong())
}