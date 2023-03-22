package tw.idv.louislee.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tw.idv.louislee.DarkModePreview
import tw.idv.louislee.theme.AccountingBookTheme

@Composable
fun AppToolbarLayout(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit
) {
    AppToolbarLayout(
        content = content,
        title = stringResource(id = title),
        modifier = modifier,
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppToolbarLayout(
    content: @Composable () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier,
                title = {
                    Text(text = title)
                },
                actions = actions
            )
        },
        content = {
            Surface(modifier = Modifier.padding(it)) {
                content()
            }
        }
    )
}

@DarkModePreview
@Composable
private fun Preview() {
    AccountingBookTheme {
        Surface {
            AppToolbarLayout(
                content = {
                    Text(text = "內文")
                },
                title = "標題文字"
            )
        }
    }
}
