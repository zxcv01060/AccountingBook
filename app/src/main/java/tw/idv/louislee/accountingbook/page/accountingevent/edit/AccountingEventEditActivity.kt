package tw.idv.louislee.accountingbook.page.accountingevent.edit

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
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.dto.AccountingEventParcelableDto
import tw.idv.louislee.accountingbook.extension.finish
import tw.idv.louislee.accountingbook.extension.getParcelableExtraCompat
import tw.idv.louislee.accountingbook.page.accountingevent.form.AccountingEventForm
import tw.idv.louislee.accountingbook.state.AccountingEventFormState
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview
import kotlin.math.absoluteValue

class AccountingEventEditActivity : ComponentActivity() {
    companion object {
        const val INTENT_ID = "accounting_event_edit.id"
        const val ID_NOT_FOUND = -1L
        const val INTENT_EVENT = "accounting_event_edit.event"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getLongExtra(INTENT_ID, ID_NOT_FOUND)
        if (id == ID_NOT_FOUND) {
            throw IllegalStateException("需傳入'INTENT_ID'參數")
        }

        setContent {
            val viewModel = getViewModel<AccountingEventEditViewModel>(
                parameters = { parametersOf(getFormState(), id) }
            )
            val accounts by viewModel.accounts.collectAsStateWithLifecycle(initialValue = emptyList())

            Content(
                state = viewModel.state,
                accounts = accounts,
                onNavigationBack = this::finish,
                onSubmitClick = {
                    viewModel.update()
                    finish()
                }
            )
        }
    }

    private fun getFormState(): AccountingEventFormState {
        val event = intent.getParcelableExtraCompat<AccountingEventParcelableDto>(INTENT_EVENT)
            ?: throw IllegalStateException("記帳紀錄編輯頁面需要'INTENT_EVENT'參數，對應AccountingEventParcelableDto物件")

        return AccountingEventFormState(
            accountId = event.accountId,
            type = event.type,
            price = event.price.absoluteValue.toUInt(),
            recordDate = event.recordDate,
            note = event.note
        )
    }
}

@Composable
private fun Content(
    state: AccountingEventFormState,
    accounts: List<AccountDto> = emptyList(),
    onNavigationBack: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    AccountingBookTheme {
        AppToolbarLayout(
            onNavigateBack = onNavigationBack,
            title = stringResource(id = R.string.accounting_event_edit_title)
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccountingEventForm(
                    modifier = Modifier.padding(all = 8.dp),
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
            price = 2500u,
            type = AccountingEventType.TRAFFIC,
            accountId = 1,
            note = "交通費，高鐵，宜蘭 -> 台南"
        )
    )
}
