package tw.idv.louislee.accountingbook.page.account.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.AppToolbarLayout
import tw.idv.louislee.accountingbook.domain.dto.account.AccountAccountingEventItemDto
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDetailDto
import tw.idv.louislee.accountingbook.domain.entity.AccountType
import tw.idv.louislee.accountingbook.extension.finish
import tw.idv.louislee.accountingbook.extension.titleId
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview
import tw.idv.louislee.accountingbook.utils.AppDateFormatter
import tw.idv.louislee.accountingbook.utils.AppDateFormatterImpl
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.absoluteValue

class AccountDetailActivity : ComponentActivity() {
    companion object {
        const val INTENT_ID = "account_detail.id"
        const val ID_NOT_FOUND = -1L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getLongExtra(INTENT_ID, ID_NOT_FOUND)
        if (id == ID_NOT_FOUND) {
            throw IllegalStateException("需要'INTENT_ID'參數")
        }

        setContent {
            val viewModel = getViewModel<AccountDetailViewModel>(parameters = { parametersOf(id) })
            val account by viewModel.account.collectAsStateWithLifecycle(initialValue = null)

            Content(id = id, account = account, dateFormatter = get(), onNavigationBack = ::finish)
        }
    }
}

@Composable
private fun Content(
    id: Long,
    account: AccountDetailDto?,
    dateFormatter: AppDateFormatter,
    onNavigationBack: () -> Unit = {}
) {
    AccountingBookTheme {
        AppToolbarLayout(
            title = stringResource(id = R.string.account_detail_title),
            onNavigateBack = onNavigationBack
        ) {
            AccountDetail(id = id, account = account, dateFormatter = dateFormatter)
        }
    }
}

@Composable
private fun AccountDetail(id: Long, account: AccountDetailDto?, dateFormatter: AppDateFormatter) {
    if (account == null) {
        AccountNotFoundDialog(id = id)
        return
    }

    Column(
        modifier = Modifier.padding(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AccountDetailBlock(account = account, dateFormatter = dateFormatter)

        Divider(modifier = Modifier.height(1.dp))

        AccountingEventColumn(
            accountingEvents = account.accountingEvents,
            dateFormatter = dateFormatter
        )
    }
}

@Composable
private fun AccountNotFoundDialog(id: Long) {
    val context = LocalContext.current
    val onDismissRequest = context::finish

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(id = R.string.common_error_title)) },
        text = { Text(text = stringResource(id = R.string.account_detail_account_not_found, id)) },
        confirmButton = {
            Button(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.common_ok))
            }
        }
    )
}

@Composable
private fun AccountDetailBlock(account: AccountDetailDto, dateFormatter: AppDateFormatter) {
    data class DetailRow(val label: String, val content: String)

    val rows = arrayOf(
        DetailRow(label = stringResource(id = R.string.account_name), content = account.name),
        DetailRow(
            label = stringResource(id = R.string.account_type),
            content = stringResource(id = account.type.titleId)
        ),
        DetailRow(
            label = stringResource(id = R.string.account_balance),
            content = stringResource(id = R.string.common_price, account.balance)
        ),
        DetailRow(
            label = stringResource(id = R.string.common_create_date),
            content = dateFormatter.formatDateTime(account.createDate)
        ),
        DetailRow(
            label = stringResource(id = R.string.common_last_update_date),
            content = dateFormatter.formatDateTime(account.lastUpdateDate)
        ),
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (row in rows) {
            Column {
                Text(text = row.label)

                Text(text = row.content)
            }
        }
    }
}

@Composable
private fun AccountingEventColumn(
    accountingEvents: List<AccountAccountingEventItemDto>,
    dateFormatter: AppDateFormatter
) {
    Text(text = stringResource(id = R.string.accounting_event))

    LazyColumn(contentPadding = PaddingValues(all = 8.dp)) {
        itemsIndexed(accountingEvents) { index, accountingEvent ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(
                            id = R.string.common_price,
                            accountingEvent.price.absoluteValue
                        )
                    )
                    Text(text = accountingEvent.note ?: "")
                }

                Text(text = dateFormatter.formatDateTime(accountingEvent.recordDate))
            }

            if (index != accountingEvents.lastIndex) {
                Divider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    Content(
        id = 1,
        account = AccountDetailDto(
            id = 1,
            type = AccountType.CASH,
            name = "現金",
            balance = 2319,
            accountingEvents = listOf(
                AccountAccountingEventItemDto(
                    id = 2,
                    price = -500,
                    note = "早餐",
                    recordDate = ZonedDateTime.of(
                        2023, 4, 16,
                        7, 5, 41, 0,
                        ZoneId.systemDefault()
                    )
                ),
                AccountAccountingEventItemDto(
                    id = 1,
                    price = 2819,
                    note = "現有金額",
                    recordDate = ZonedDateTime.of(
                        2023, 4, 15,
                        10, 6, 29, 0,
                        ZoneId.systemDefault()
                    )
                )
            ),
            createDate = ZonedDateTime.of(
                2023, 4, 15,
                10, 6, 29, 0,
                ZoneId.systemDefault()
            ),
            lastUpdateDate = ZonedDateTime.of(
                2023, 4, 15,
                18, 41, 52, 0,
                ZoneId.systemDefault()
            )
        ),
        dateFormatter = AppDateFormatterImpl(LocalContext.current)
    )
}
