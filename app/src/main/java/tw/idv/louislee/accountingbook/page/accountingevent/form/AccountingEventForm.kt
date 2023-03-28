package tw.idv.louislee.accountingbook.page.accountingevent.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.domain.entity.filter
import tw.idv.louislee.accountingbook.extension.textId
import tw.idv.louislee.accountingbook.state.AccountingEventFormState
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

@Composable
fun AccountingEventForm(state: AccountingEventFormState, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxWidth() then modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = stringResource(id = R.string.accounting_event_price)) },
            value = state.price.toString(),
            onValueChange = { state.price = it.toUIntOrNull() ?: 0u }
        )

        TypeDropdown(type = state.type, onTypeSelect = { state.type = it })

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.accounting_event_note)) },
            value = state.note,
            onValueChange = { state.note = it }
        )
    }
}

@Composable
private fun TypeDropdown(
    type: AccountingEventType,
    onTypeSelect: (type: AccountingEventType) -> Unit
) {
    var isIncome by remember {
        mutableStateOf(type.isIncome)
    }

    TypeRadioGroup(
        isIncome = isIncome,
        onIsIncomeChange = {
            isIncome = it
            onTypeSelect(
                if (it) {
                    AccountingEventType.UNKNOWN_INCOME
                } else {
                    AccountingEventType.UNKNOWN_EXPENSES
                }
            )
        }
    )
    TypeDropdownMenu(type = type, isIncome = isIncome, onTypeSelect = onTypeSelect)
}

@Composable
private fun TypeRadioGroup(isIncome: Boolean, onIsIncomeChange: (isIncome: Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = !isIncome,
                onClick = {
                    onIsIncomeChange(false)
                }
            )
            Text(text = stringResource(id = R.string.accounting_event_expenses_type))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = isIncome,
                onClick = {
                    onIsIncomeChange(true)
                }
            )
            Text(text = stringResource(id = R.string.accounting_event_income_type))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TypeDropdownMenu(
    type: AccountingEventType,
    isIncome: Boolean,
    onTypeSelect: (type: AccountingEventType) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = it
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            label = { Text(text = stringResource(id = R.string.accounting_event_type)) },
            value = stringResource(id = type.textId),
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
        )

        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            val types = AccountingEventType.values()
                .filter(isIncome = isIncome)
            types.forEach {
                println(it)
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = it.textId)) },
                    onClick = {
                        onTypeSelect(it)
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
        Surface {
            AccountingEventForm(
                modifier = Modifier.padding(all = 8.dp),
                state = AccountingEventFormState()
            )
        }
    }
}
