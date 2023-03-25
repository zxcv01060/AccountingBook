package tw.idv.louislee.accountingbook.page.accountingevent.add

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme

class AddActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

@Composable
private fun Content() {
    AccountingBookTheme {

    }
}

@Composable
private fun Preview() {
    Content()
}
