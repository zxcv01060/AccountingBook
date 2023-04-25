package tw.idv.louislee.accountingbook.page.accountingevent.add

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.AppToolbarLayout
import tw.idv.louislee.accountingbook.domain.DomainConstant
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDto
import tw.idv.louislee.accountingbook.domain.entity.AccountType
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.dto.ElectronicInvoiceParcelableDto
import tw.idv.louislee.accountingbook.extension.finish
import tw.idv.louislee.accountingbook.extension.getParcelableExtraCompat
import tw.idv.louislee.accountingbook.page.accountingevent.form.AccountingEventForm
import tw.idv.louislee.accountingbook.state.AccountingEventFormState
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

class AccountingEventAddActivity : ComponentActivity() {
    companion object {
        const val INTENT_ELECTRONIC_INVOICE_BARCODE =
            "AccountingEventAddActivity.electronicInvoiceBarcode"
    }

    private val barcode: ElectronicInvoiceParcelableDto? by lazy {
        intent.getParcelableExtraCompat(INTENT_ELECTRONIC_INVOICE_BARCODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel =
                getViewModel<AccountingEventAddViewModel>(parameters = { parametersOf(barcode) })
            val accounts by viewModel.findAllAccount()
                .collectAsStateWithLifecycle(initialValue = emptyList())

            Content(
                state = viewModel.state,
                accounts = accounts,
                onSubmitClick = {
                    viewModel.add()
                    finish()
                }
            )
        }
    }
}

@Composable
private fun Content(
    state: AccountingEventFormState,
    accounts: Iterable<AccountDto>,
    onSubmitClick: () -> Unit
) {
    AccountingBookTheme {
        val context = LocalContext.current

        AppToolbarLayout(
            title = stringResource(id = R.string.accounting_event_add_title),
            onNavigateBack = context::finish
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccountingEventForm(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    state = state,
                    accounts = accounts
                )

                ButtonGroup(onSubmitClick = onSubmitClick)
            }
        }
    }
}

@Composable
private fun ButtonGroup(onSubmitClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onSubmitClick) {
            Text(text = stringResource(id = R.string.common_submit))
        }

        val context = LocalContext.current
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            onClick = context::finish
        ) {
            Text(
                text = stringResource(id = R.string.common_cancel),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    Content(
        state = AccountingEventFormState(
            price = 35u,
            type = AccountingEventType.FOOD_OR_DRINK,
            note = "早餐"
        ),
        accounts = listOf(
            AccountDto(
                id = DomainConstant.CASH_ACCOUNT_ID,
                name = "現金",
                type = AccountType.CASH,
                balance = 0
            )
        ),
        onSubmitClick = {}
    )
}
