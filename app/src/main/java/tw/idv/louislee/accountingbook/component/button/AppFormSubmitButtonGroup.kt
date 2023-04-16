package tw.idv.louislee.accountingbook.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.extension.finish
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

@Composable
fun AppFormSubmitButtonGroup(onSubmit: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp) then modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onSubmit) {
            Text(text = stringResource(id = R.string.common_submit))
        }

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
    AccountingBookTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppFormSubmitButtonGroup(onSubmit = {})
        }
    }
}
