package tw.idv.louislee.accountingbook.page.accountingevent.add

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.AppToolbarLayout
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventFormDto
import tw.idv.louislee.accountingbook.extension.finish
import tw.idv.louislee.accountingbook.page.accountingevent.form.AccountingEventForm
import tw.idv.louislee.accountingbook.state.AccountingEventFormState
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

class AddActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = getViewModel<AddViewModel>()
            Content(onSubmitClick = {
                viewModel.add(1, it)
                finish()
            })
        }
    }
}

@Composable
private fun Content(onSubmitClick: (form: AccountingEventFormDto) -> Unit) {
    AccountingBookTheme {
        AppToolbarLayout(title = R.string.accounting_event_add_title) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val state = remember { AccountingEventFormState() }
                AccountingEventForm(modifier = Modifier.padding(horizontal = 8.dp), state = state)

                ButtonGroup(onSubmitClick = { onSubmitClick(state.form) })
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
    Content(onSubmitClick = {})
}
