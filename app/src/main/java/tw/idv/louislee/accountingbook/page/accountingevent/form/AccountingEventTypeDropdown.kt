package tw.idv.louislee.accountingbook.page.accountingevent.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.domain.entity.filter
import tw.idv.louislee.accountingbook.extension.textId
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountingEventTypeDropdown(
    type: AccountingEventType,
    types: Iterable<AccountingEventType>,
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
            AccountingEventTypeDropdown(
                type = AccountingEventType.FOOD_OR_DRINK,
                types = AccountingEventType.values()
                    .filter(isIncome = false),
                onTypeSelect = {}
            )
        }
    }
}
