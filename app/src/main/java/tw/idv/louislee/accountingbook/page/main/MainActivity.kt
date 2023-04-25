package tw.idv.louislee.accountingbook.page.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import tw.idv.louislee.accountingbook.AppScreen
import tw.idv.louislee.accountingbook.domain.dto.accountingevent.AccountingEventDto
import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import tw.idv.louislee.accountingbook.page.account.AccountScreen
import tw.idv.louislee.accountingbook.page.accountingevent.AccountingEventScreen
import tw.idv.louislee.accountingbook.theme.AccountingBookTheme
import tw.idv.louislee.accountingbook.theme.AppPreview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content(
                accountingEventScreen = { AccountingEventScreen() },
                accountScreen = { AccountScreen() }
            )
        }
    }
}

@Composable
private fun Content(
    accountingEventScreen: @Composable () -> Unit,
    accountScreen: @Composable () -> Unit
) {
    AccountingBookTheme {
        Surface {
            val controller = rememberNavController()

            Scaffold(bottomBar = { AppBottomBar(controller = controller) }) {
                NavigationContainer(
                    modifier = Modifier.padding(it),
                    controller = controller,
                    accountingEventScreen = accountingEventScreen,
                    accountScreen = accountScreen
                )
            }
        }
    }
}

@Composable
private fun AppBottomBar(controller: NavHostController) {
    val entry by controller.currentBackStackEntryAsState()
    val destination = entry?.destination

    NavigationBar {
        for (screen in AppScreen.values()) {
            NavigationBarItem(
                selected = destination?.hierarchy?.any { it.route == screen.route } == true,
                label = { Text(text = stringResource(id = screen.labelId)) },
                icon = screen.icon,
                onClick = {
                    controller.navigate(route = screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(controller.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun NavigationContainer(
    modifier: Modifier,
    controller: NavHostController,
    accountingEventScreen: @Composable () -> Unit,
    accountScreen: @Composable () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = controller,
        startDestination = AppScreen.ACCOUNTING_EVENT.route
    ) {
        composable(route = AppScreen.ACCOUNTING_EVENT.route) { accountingEventScreen() }
        composable(route = AppScreen.ACCOUNT.route) { accountScreen() }
    }
}

@AppPreview
@Composable
private fun Preview() {
    Content(
        accountingEventScreen = {
            AccountingEventScreen(
                events = listOf(
                    AccountingEventDto(
                        id = 1,
                        price = 35,
                        type = AccountingEventType.FOOD_OR_DRINK
                    ),
                    AccountingEventDto(
                        id = 2,
                        price = 500,
                        type = AccountingEventType.INVOICE,
                        note = "發票雲端獎"
                    ),
                    AccountingEventDto(
                        id = 3,
                        price = 100,
                        type = AccountingEventType.FOOD_OR_DRINK,
                        note = "早餐(原味雞腿堡 + 奶茶)"
                    )
                )
            )
        },
        accountScreen = {}
    )
}
