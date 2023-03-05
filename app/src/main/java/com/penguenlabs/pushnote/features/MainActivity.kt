package com.penguenlabs.pushnote.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.penguenlabs.pushnote.features.color.ColorScreen
import com.penguenlabs.pushnote.features.history.ui.HistoryScreen
import com.penguenlabs.pushnote.features.home.ui.HomeScreen
import com.penguenlabs.pushnote.features.permission.NotificationPermissionScreen
import com.penguenlabs.pushnote.features.settings.ui.SettingsScreen
import com.penguenlabs.pushnote.features.theme.ThemeScreen
import com.penguenlabs.pushnote.features.typography.TypographyScreen
import com.penguenlabs.pushnote.navigation.Destination
import com.penguenlabs.pushnote.theme.PushNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val darkMode = mainViewModel.darkModeState
            PushNoteTheme(darkTheme = darkMode) {
                ProvideWindowInsets {
                    val navController = rememberNavController()
                    val systemUiController = rememberSystemUiController()

                    NavHost(
                        navController = navController, startDestination = Destination.Home.route
                    ) {
                        composable(route = Destination.Home.route) {
                            HomeScreen(onDialogDismissRequest = {
                                finish()
                            }, onSettingsButtonClick = {
                                navController.navigate(route = Destination.Settings.route)
                            }, onNotificationPermissionNeed = { pushNotificationText ->
                                if (pushNotificationText.isNotEmpty() and pushNotificationText.isNotBlank()) {
                                    navController.navigate(route = "${Destination.NotificationPermission.route}/$pushNotificationText")
                                } else {
                                    navController.navigate(route = Destination.NotificationPermission.route)
                                }
                            })
                        }
                        composable(route = "${Destination.Home.route}/{${Destination.Home.PARAM_PUSH_NOTIFICATION_TEXT}}") { navBackStackEntry ->
                            HomeScreen(pushNotificationText = navBackStackEntry.arguments?.getString(
                                Destination.Home.PARAM_PUSH_NOTIFICATION_TEXT, ""
                            ).orEmpty(), onDialogDismissRequest = {
                                finish()
                            }, onSettingsButtonClick = {
                                navController.navigate(route = Destination.Settings.route)
                            }, onNotificationPermissionNeed = {
                                navController.navigate(route = Destination.NotificationPermission.route)
                            })
                        }
                        composable(route = Destination.Settings.route) {
                            SettingsScreen(onDarkModeChange = {
                                mainViewModel.onDarkModeChanged(it)
                            }, onHistoryClick = {
                                navController.navigate(route = Destination.History.route)
                            }, onBackPressClick = {
                                navController.navigateUp()
                            },
                                onThemeClick = { navController.navigate(Destination.Theme.route) })
                        }
                        composable(route = Destination.History.route) {
                            HistoryScreen(onBackPressClick = {
                                navController.navigateUp()
                            })
                        }
                        composable(route = Destination.Theme.route) {
                            ThemeScreen()
                        }
                        composable(route = Destination.Color.route) {
                            ColorScreen()
                        }
                        composable(route = Destination.Typography.route) {
                            TypographyScreen()
                        }
                        composable(route = Destination.NotificationPermission.route) {
                            NotificationPermissionScreen(onBackPressClick = {
                                navController.navigateUp()
                            }, onPermissionGranted = {
                                navController.navigate(Destination.Home.route) {
                                    launchSingleTop = true
                                }
                            })
                        }
                        composable(route = "${Destination.NotificationPermission.route}/{${Destination.NotificationPermission.PARAM_PUSH_NOTIFICATION_TEXT}}") { navBackStackEntry ->
                            NotificationPermissionScreen(pushNotificationText = navBackStackEntry.arguments?.getString(
                                Destination.NotificationPermission.PARAM_PUSH_NOTIFICATION_TEXT,
                                ""
                            ).orEmpty(), onBackPressClick = {
                                navController.navigateUp()
                            }, onPermissionGranted = { pushNotificationText ->
                                if (pushNotificationText.isNotEmpty() and pushNotificationText.isNotBlank()) {
                                    navController.navigate("${Destination.Home.route}/$pushNotificationText") {
                                        launchSingleTop = true
                                    }
                                } else {
                                    navController.navigate(Destination.Home.route) {
                                        launchSingleTop = true
                                    }
                                }

                            })
                        }
                    }
                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = Color.Transparent, darkIcons = darkMode.not()
                        )
                    }
                }
            }
        }
    }
}