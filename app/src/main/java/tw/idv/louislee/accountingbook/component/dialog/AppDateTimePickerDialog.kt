package tw.idv.louislee.accountingbook.component.dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDateTimePickerDialog(
    isShowing: Boolean,
    dismissRequest: () -> Unit,
    dateTime: ZonedDateTime?,
    onDateTimeChange: (dateTime: ZonedDateTime) -> Unit
) {
    var date by remember(key1 = dateTime) {
        mutableStateOf(dateTime?.toLocalDate())
    }
    var isPickDate by remember(key1 = isShowing) {
        mutableStateOf(true)
    }

    val calendarState = rememberUseCaseState()
    if (isShowing) {
        calendarState.show()
    } else {
        calendarState.hide()
        return
    }

    if (isPickDate) {
        CalendarDialog(
            state = calendarState,
            selection = CalendarSelection.Date(
                selectedDate = date,
                onNegativeClick = dismissRequest
            ) {
                date = it
                isPickDate = false
            }
        )
        return
    }

    ClockDialog(
        state = calendarState,
        config = ClockConfig(
            defaultTime = dateTime?.toLocalTime()
        ),
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            onDateTimeChange(
                ZonedDateTime.of(
                    date,
                    LocalTime.of(hours, minutes),
                    ZoneId.systemDefault()
                )
            )
            dismissRequest()
        }
    )
}

@AppPreview
@Composable
private fun Preview() {
    AccountingBookTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var isShowing by remember {
                mutableStateOf(true)
            }
            var dateTime by remember {
                mutableStateOf(
                    ZonedDateTime.of(
                        2023, 4, 18,
                        20, 2, 51, 0,
                        ZoneId.systemDefault()
                    )
                )
            }

            Text(text = dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))

            AppDateTimePickerDialog(
                isShowing = isShowing,
                dateTime = dateTime,
                dismissRequest = { isShowing = false },
                onDateTimeChange = { dateTime = it }
            )
        }
    }
}
