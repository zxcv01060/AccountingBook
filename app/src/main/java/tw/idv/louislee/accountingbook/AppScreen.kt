package tw.idv.louislee.accountingbook

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

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
    );
}