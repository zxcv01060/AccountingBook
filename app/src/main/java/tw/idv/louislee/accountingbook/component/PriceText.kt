package tw.idv.louislee.accountingbook.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview
import tw.idv.louislee.accountingbook.theme.negativePrice
import tw.idv.louislee.accountingbook.theme.positivePrice
import kotlin.math.absoluteValue

@Composable
fun PriceText(price: Long, modifier: Modifier = Modifier, align: TextAlign = TextAlign.Start) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.common_price, price.absoluteValue),
        color = if (price > 0) {
            MaterialTheme.colorScheme.positivePrice()
        } else if (price < 0) {
            MaterialTheme.colorScheme.negativePrice()
        } else {
            Color.Unspecified
        },
        textAlign = align
    )
}

@AppPreview
@Composable
private fun PreviewPositive() {
    AccountingBookTheme {
        Surface {
            PriceText(price = 1000)
        }
    }
}

@AppPreview
@Composable
private fun PreviewNegative() {
    AccountingBookTheme {
        Surface {
            PriceText(price = -2500)
        }
    }
}
