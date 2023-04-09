package tw.idv.louislee.accountingbook.page.accountingevent.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.AppToolbarLayout
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventDetailDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.extension.finish
import tw.idv.louislee.accountingbook.extension.textId
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview
import tw.idv.louislee.accountingbook.utils.AppDateFormatter
import tw.idv.louislee.accountingbook.utils.AppDateFormatterImpl
import java.time.ZoneId
import java.time.ZonedDateTime

class AccountingEventDetailActivity : ComponentActivity() {
    companion object {
        const val INTENT_ID = "accounting_event_detail.id"
        const val ID_NOT_FOUND = -1L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getLongExtra(INTENT_ID, ID_NOT_FOUND)
        if (id == ID_NOT_FOUND) {
            throw IllegalStateException("記帳詳細記錄頁面需要'INTENT_ID'參數")
        }
        setContent {
            val viewModel = getViewModel<AccountingEventDetailViewModel>()
            val event by viewModel.findById(id = id)
                .collectAsStateWithLifecycle(initialValue = AccountingEventDetailDto.placeholder)

            Content(id = id, event = event)
        }
    }
}

@Composable
private fun Content(
    id: Long,
    event: AccountingEventDetailDto?,
    dateFormatter: AppDateFormatter = get()
) {
    val context = LocalContext.current

    AccountingBookTheme {
        AppToolbarLayout(
            onNavigateBack = context::finish,
            title = R.string.accounting_event_detail_title
        ) {
            AccountingEventDetail(id = id, event = event, dateFormatter = dateFormatter)
        }
    }
}

@Composable
private fun AccountingEventDetail(
    id: Long,
    event: AccountingEventDetailDto?,
    dateFormatter: AppDateFormatter
) {
    if (event == null) {
        EventNotFoundDialog(id = id)
        return
    }

    data class DetailRow(val label: String, val content: String)

    val rows = arrayOf(
        DetailRow(
            label = stringResource(id = R.string.accounting_event_price),
            content = stringResource(id = R.string.common_price, event.price)
        ),
        DetailRow(
            label = stringResource(id = R.string.accounting_event_type),
            content = stringResource(id = event.type.textId)
        ),
        DetailRow(
            label = stringResource(id = R.string.common_create_date),
            content = dateFormatter.formatDateTime(event.createDate)
        ),
        DetailRow(
            label = stringResource(id = R.string.common_last_update_date),
            content = dateFormatter.formatDateTime(event.lastUpdateDate)
        ),
        DetailRow(
            label = stringResource(id = R.string.accounting_event_note),
            content = event.note ?: ""
        )
    )
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in rows) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.fillMaxWidth(), text = row.label)

                Text(modifier = Modifier.fillMaxWidth(), text = row.content)
            }
        }
    }
}

@Composable
private fun EventNotFoundDialog(id: Long) {
    val context = LocalContext.current
    val onDismiss: () -> Unit = context::finish

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.common_error_title))
        },
        text = {
            Text(text = stringResource(id = R.string.accounting_event_detail_event_not_found, id))
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.common_ok))
            }
        }
    )
}

@AppPreview
@Composable
private fun Preview() {
    Content(
        id = 1,
        event = AccountingEventDetailDto(
            id = 1,
            type = AccountingEventType.FOOD_OR_DRINK,
            price = 35,
            note = "早餐 巧克力吐司 + 奶茶",
            createDate = ZonedDateTime.of(
                2023, 5, 29,
                7, 29, 43, 0,
                ZoneId.of("Asia/Taipei")
            ),
            lastUpdateDate = ZonedDateTime.of(
                2023, 5, 29,
                19, 0, 19, 0,
                ZoneId.of("Asia/Taipei")
            )
        ),
        dateFormatter = AppDateFormatterImpl(context = LocalContext.current)
    )
}
