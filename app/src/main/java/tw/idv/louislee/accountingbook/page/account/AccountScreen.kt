package tw.idv.louislee.accountingbook.page.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.getViewModel
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.AppToolbarLayout
import tw.idv.louislee.accountingbook.component.PriceText
import tw.idv.louislee.accountingbook.domain.dto.account.AccountDto
import tw.idv.louislee.accountingbook.domain.entity.AccountType
import tw.idv.louislee.accountingbook.extension.startActivity
import tw.idv.louislee.accountingbook.extension.titleId
import tw.idv.louislee.accountingbook.page.account.add.AccountAddActivity
import tw.idv.louislee.accountingbook.page.account.detail.AccountDetailActivity
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

@Composable
fun AccountScreen(viewModel: AccountViewModel = getViewModel()) {
    val scope = rememberCoroutineScope()
    val coroutineContext = scope.coroutineContext
    val accounts by viewModel.findAll()
        .collectAsStateWithLifecycle(initialValue = emptyList(), context = coroutineContext)
    val context = LocalContext.current

    AccountScreen(
        accounts = accounts,
        onAddClick = { context.startActivity<AccountAddActivity>() },
        onRowClick = {
            context.startActivity<AccountDetailActivity> {
                putExtra(
                    AccountDetailActivity.INTENT_ID,
                    it.id
                )
            }
        })
}

@Composable
fun AccountScreen(
    accounts: List<AccountDto>,
    onAddClick: () -> Unit = {},
    onRowClick: (account: AccountDto) -> Unit = {}
) {
    AccountingBookTheme {
        AppToolbarLayout(
            title = R.string.account_list_title,
            actions = {
                IconButton(onClick = onAddClick) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.common_add)
                    )
                }
            }
        ) {
            AccountList(accounts = accounts, onRowClick = onRowClick)
        }
    }
}

@Composable
private fun AccountList(accounts: List<AccountDto>, onRowClick: (account: AccountDto) -> Unit) {
    LazyColumn {
        itemsIndexed(accounts) { index, account ->
            val padding = 8.dp

            Row(
                modifier = Modifier
                    .clickable(onClick = { onRowClick(account) })
                    .fillMaxWidth()
                    .padding(all = padding)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = account.name)

                    Text(text = stringResource(id = account.type.titleId))
                }

                Column(modifier = Modifier.weight(1f)) {
                    PriceText(
                        modifier = Modifier.fillMaxWidth(),
                        price = account.balance,
                        align = TextAlign.End
                    )
                }
            }

            if (index != accounts.lastIndex) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = padding)
                )
            }
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    AccountScreen(
        accounts = listOf(
            AccountDto(id = 1, name = "現金", type = AccountType.CASH, balance = 5028),
            AccountDto(id = 2, name = "中國信託", type = AccountType.BANK, balance = 57156),
            AccountDto(id = 3, name = "國泰世華", type = AccountType.BANK, balance = 24008)
        )
    )
}
