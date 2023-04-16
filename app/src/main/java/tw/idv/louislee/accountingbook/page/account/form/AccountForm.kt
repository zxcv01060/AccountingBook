package tw.idv.louislee.accountingbook.page.account.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.domain.entity.AccountType
import tw.idv.louislee.accountingbook.extension.titleId
import tw.idv.louislee.accountingbook.state.AccountFormState
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

@Composable
fun AccountForm(state: AccountFormState, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.account_name)) },
            value = state.name,
            onValueChange = { state.name = it }
        )

        AccountTypeDropdown(type = state.type, onTypeChange = { state.type = it })

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.account_balance)) },
            value = state.balance.toString(),
            onValueChange = { state.balance = it.toUIntOrNull() ?: 0u },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountTypeDropdown(type: AccountType, onTypeChange: (type: AccountType) -> Unit) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            label = { Text(text = stringResource(id = R.string.account_type)) },
            value = stringResource(id = type.titleId),
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
        )

        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            for (accountType in AccountType.values()) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = accountType.titleId)) },
                    onClick = {
                        onTypeChange(accountType)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    AccountingBookTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 8.dp)
        ) {
            AccountForm(
                state = AccountFormState(
                    name = "中國信託",
                    balance = 29281u,
                    type = AccountType.BANK
                )
            )
        }
    }
}
