package tw.idv.louislee.accountingbook.page.accountingevent.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDto
import tw.idv.louislee.accountingbook.domain.entity.AccountType
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

@Composable
fun AccountDropdown(
    id: Long?,
    accounts: Iterable<AccountDto>,
    onAccountSelect: (id: AccountDto) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Column {
        val account = accounts.firstOrNull { it.id == id } ?: accounts.firstOrNull()

        AccountDropdownBox(
            isExpanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            accounts = accounts,
            account = account,
            onAccountSelect = onAccountSelect
        )

        if (account != null) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(
                    id = R.string.accounting_event_balance,
                    stringResource(id = R.string.common_price, account.balance)
                ),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AccountDropdownBox(
    isExpanded: Boolean,
    onExpandedChange: (isExpanded: Boolean) -> Unit,
    accounts: Iterable<AccountDto>,
    account: AccountDto?,
    onAccountSelect: (id: AccountDto) -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = isExpanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            label = { Text(text = stringResource(id = R.string.accounting_event_account)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            value = account?.name ?: "",
            onValueChange = {}
        )

        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { onExpandedChange(false) }) {
            accounts.forEach {
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = it.name)

                            Text(text = stringResource(id = R.string.common_price, it.balance))
                        }
                    },
                    onClick = { onAccountSelect(it) }
                )
            }
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    AccountingBookTheme {
        Surface {
            AccountDropdown(
                id = 1,
                accounts = listOf(AccountDto(id = 1, name = "現金", AccountType.CASH, balance = 500)),
                onAccountSelect = {}
            )
        }
    }
}
