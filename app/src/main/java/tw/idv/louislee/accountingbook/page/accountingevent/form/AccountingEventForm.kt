package tw.idv.louislee.accountingbook.page.accountingevent.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.form.AppDateTimeTextField
import tw.idv.louislee.accountingbook.domain.DomainConstant
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDto
import tw.idv.louislee.accountingbook.domain.entity.AccountType
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.domain.entity.filter
import tw.idv.louislee.accountingbook.state.AccountingEventFormState
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

@Composable
fun AccountingEventForm(
    state: AccountingEventFormState,
    accounts: Iterable<AccountDto>,
    modifier: Modifier = Modifier
) {
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

        AccountDropdown(
            id = state.accountId,
            accounts = accounts,
            onAccountSelect = { state.accountId = it.id }
        )

        AppDateTimeTextField(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.accounting_event_record_date),
            dateTime = state.recordDate,
            onDateTimeChange = { state.recordDate = it }
        )

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
    AccountingEventTypeDropdown(
        type = type,
        types = AccountingEventType.values()
            .filter(isIncome = isIncome),
        onTypeSelect = onTypeSelect
    )
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

@AppPreview
@Composable
private fun Preview() {
    AccountingBookTheme {
        Surface {
            AccountingEventForm(
                modifier = Modifier.padding(all = 8.dp),
                state = AccountingEventFormState(),
                accounts = listOf(
                    AccountDto(
                        id = DomainConstant.CASH_ACCOUNT_ID,
                        name = "現金",
                        type = AccountType.CASH,
                        balance = 500
                    )
                )
            )
        }
    }
}
