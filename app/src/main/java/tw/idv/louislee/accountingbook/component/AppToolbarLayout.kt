package tw.idv.louislee.accountingbook.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbarLayout(
    title: String,
    modifier: Modifier = Modifier,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier,
                navigationIcon = {
                    if (onNavigateBack == null) {
                        return@TopAppBar
                    }

                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.common_back)
                        )
                    }
                },
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

@AppPreview
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
