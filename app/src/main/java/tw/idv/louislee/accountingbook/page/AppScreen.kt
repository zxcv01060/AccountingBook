package tw.idv.louislee.accountingbook.page

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import tw.idv.louislee.accountingbook.R

enum class AppScreen(
    val route: String,
    val icon: @Composable () -> Unit,
    @field:StringRes val labelId: Int
) {
    ACCOUNTING_EVENT(
        route = "AccountingEvent",
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_attach_money_24),
                contentDescription = stringResource(id = R.string.accounting_event_screen_title)
            )
        },
        labelId = R.string.accounting_event_screen_title
    ),
    ACCOUNT(
        route = "Account",
        icon = {
            Icon(
                imageVector = Icons.Filled.AccountBox,
                contentDescription = stringResource(id = R.string.account_screen_title)
            )
        },
        labelId = R.string.account_screen_title
    );
}