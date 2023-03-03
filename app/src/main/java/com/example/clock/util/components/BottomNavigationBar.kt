package com.example.clock.util.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.clock.ui.Screen
import com.example.clock.ui.theme.ClockTheme

@Preview
@Composable
private fun BottomNavigationPreview() {
    ClockTheme {
        BottomNavigationBar(
            listBottomBarItems = listBottomBarItems,
            navController = rememberNavController(),
        )
    }
}

@Preview
@Composable
private fun BottomNavigationPreviewDark() {
    ClockTheme(darkTheme = true) {
        BottomNavigationBar(
            listBottomBarItems = listBottomBarItems,
            navController = rememberNavController(),
        )
    }
}

data class BottomBarItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
)

@Composable
fun BottomNavigationBar(
    listBottomBarItems: List<BottomBarItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(modifier = modifier) {
        listBottomBarItems.forEach { item ->
            val isItemSelected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = isItemSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(text = item.name)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.name,
                    )
                },
            )
        }
    }
}

val listBottomBarItems = listOf(
    BottomBarItem(
        name = "Alarm",
        route = Screen.AlarmsList.route,
        icon = Icons.Default.Alarm,
    ),
    BottomBarItem(
        name = "Clock",
        route = Screen.Clock.route,
        icon = Icons.Default.Language,
    ),
    BottomBarItem(
        name = "Stopwatch",
        route = Screen.Stopwatch.route,
        icon = Icons.Default.Timer,
    ),
    BottomBarItem(
        name = "Timer",
        route = Screen.Timer.route,
        icon = Icons.Default.HourglassEmpty,
    ),
)
