package tw.idv.louislee.accountingbook.page.account.add

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.AppToolbarLayout
import tw.idv.louislee.accountingbook.component.button.AppFormSubmitButtonGroup
import tw.idv.louislee.accountingbook.domain.entity.AccountType
import tw.idv.louislee.accountingbook.page.account.form.AccountForm
import tw.idv.louislee.accountingbook.state.AccountFormState
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

class AccountAddActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = getViewModel<AccountAddViewModel>()

            Content(
                state = viewModel.state,
                onNavigationBack = ::finish,
                onSubmit = {
                    viewModel.add()
                    finish()
                }
            )
        }
    }
}

@Composable
private fun Content(
    state: AccountFormState,
    onNavigationBack: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    AccountingBookTheme {
        AppToolbarLayout(
            title = stringResource(id = R.string.account_add_title),
            onNavigateBack = onNavigationBack
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccountForm(
                    modifier = Modifier.fillMaxWidth(),
                    state = state
                )

                AppFormSubmitButtonGroup(onSubmit = onSubmit)
            }
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    Content(
        state = AccountFormState(
            name = "中國信託",
            type = AccountType.BANK,
            balance = 39482u
        )
    )
}
