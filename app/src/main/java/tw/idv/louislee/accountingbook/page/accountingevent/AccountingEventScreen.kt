package tw.idv.louislee.accountingbook.page.accountingevent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.getViewModel
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.AppToolbarLayout
import tw.idv.louislee.accountingbook.component.PriceText
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.extension.startActivity
import tw.idv.louislee.accountingbook.extension.textId
import tw.idv.louislee.accountingbook.page.accountingevent.add.AddActivity
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

@Composable
fun AccountingEventScreen(viewModel: AccountingEventViewModel = getViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val events by viewModel.findAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            context = coroutineScope.coroutineContext
        )

    AccountingEventScreen(events = events)
}

@Composable
fun AccountingEventScreen(events: List<AccountingEventDto>) {
    AccountingBookTheme {
        AppToolbarLayout(
            title = R.string.accounting_event_list_title,
            actions = {
                val context = LocalContext.current

                IconButton(onClick = { context.startActivity<AddActivity>() }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.common_add)
                    )
                }
            }
        ) {
            AccountingEventList(events = events)
        }
    }
}

@Composable
private fun AccountingEventList(events: List<AccountingEventDto>) {
    LazyColumn {
        items(events) {
            AccountingEventRow(event = it)
        }
    }
}

@Composable
private fun AccountingEventRow(event: AccountingEventDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = event.type.textId))

            PriceText(price = event.price)
        }

        if (event.note?.isNotBlank() == true) {
            Row {
                Text(text = event.note ?: "")
            }
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    val events = listOf(
        AccountingEventDto(
            id = 1,
            price = -500,
            type = AccountingEventType.FOOD_OR_DRINK
        ),
        AccountingEventDto(
            id = 2,
            price = 2000,
            type = AccountingEventType.BILL,
            note = "2023年2、3月的發票中一張"
        )
    )

    AccountingEventScreen(events = events)
}
