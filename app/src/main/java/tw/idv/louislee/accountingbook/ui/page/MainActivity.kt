package tw.idv.louislee.accountingbook.ui.page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import tw.idv.louislee.DarkModePreview
import tw.idv.louislee.accountingbook.domain.dto.AccountingEventFormDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

@Composable
private fun Content(viewModel: MainViewModel = getViewModel()) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { onAddClick(viewModel) }) {
            Text(text = "新增")
        }

        EventList(viewModel = viewModel)
    }
}

private fun onAddClick(viewModel: MainViewModel) {
    viewModel.add(
        AccountingEventFormDto(
            type = AccountingEventType.FOOD_OR_DRINK,
            price = 252,
            note = "小七純喫茶 + 御飯糰"
        )
    )
}

@Composable
private fun EventList(viewModel: MainViewModel) {
    val events by viewModel.findAll()
        .collectAsState(initial = emptyList())

    LazyColumn {
        items(events) {
            Row {
                Column {
                    Text(text = "id = ${it.id}")
                    Text(text = "備註：${it.note}")
                }

                Column {
                    Text(text = "金額：${it.price}")
                    Text(text = it.createDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                }
            }
        }
    }
}

@DarkModePreview
@Composable
fun DefaultPreview() {
    Content()
}