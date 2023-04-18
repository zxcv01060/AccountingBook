package tw.idv.louislee.accountingbook.component.form

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.component.dialog.AppDateTimePickerDialog
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AppDateTimeTextField(
    dateTime: ZonedDateTime?,
    onDateTimeChange: (dateTime: ZonedDateTime) -> Unit,
    modifier: Modifier = Modifier,
    format: String = stringResource(id = R.string.common_date_time_format),
    label: String? = null,
    leadingIcon: @Composable () -> Unit = {
        Icon(imageVector = Icons.Filled.DateRange, contentDescription = label)
    }
) {
    var isShowing by remember {
        mutableStateOf(false)
    }
    val interactionSource by remember {
        mutableStateOf(MutableInteractionSource())
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    if (isPressed) {
        isShowing = true
    }

    OutlinedTextField(
        modifier = modifier,
        readOnly = true,
        interactionSource = interactionSource,
        label = label?.let {
            { Text(text = it) }
        },
        leadingIcon = leadingIcon,
        value = dateTime?.format(DateTimeFormatter.ofPattern(format)) ?: "",
        onValueChange = {}
    )

    Log.d("AccountingBook", isShowing.toString())

    AppDateTimePickerDialog(
        isShowing = isShowing,
        dismissRequest = { isShowing = false },
        dateTime = dateTime,
        onDateTimeChange = onDateTimeChange
    )
}

@AppPreview
@Composable
private fun Preview() {
    AccountingBookTheme {
        Surface {
            var dateTime by remember {
                mutableStateOf(
                    ZonedDateTime.of(
                        2023, 4, 18,
                        20, 2, 51, 0,
                        ZoneId.systemDefault()
                    )
                )
            }

            AppDateTimeTextField(
                modifier = Modifier.fillMaxWidth(),
                dateTime = dateTime,
                onDateTimeChange = { dateTime = it }
            )
        }
    }
}
