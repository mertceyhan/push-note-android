package com.penguenlabs.pushnote.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.penguenlabs.pushnote.features.history.ui.HistoryScreen
import com.penguenlabs.pushnote.features.home.ui.HomeScreen
import com.penguenlabs.pushnote.features.settings.ui.SettingsScreen
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
                    val systemUiController = rememberSystemUiController()
                    val darkIcons = MaterialTheme.colors.isLight

                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            Color.Transparent,
                            darkIcons = darkIcons
                        )
                    }

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Destination.Home.route
                    ) {
                        composable(route = Destination.Home.route) {
                            HomeScreen(
                                onDialogDismissRequest = {
                                    finish()
                                },
                                onSettingsButtonClick = {
                                    navController.navigate(route = Destination.Settings.route)
                                }
                            )
                        }
                        composable(route = Destination.Settings.route) {
                            SettingsScreen(
                                onDarkModeChange = {
                                    mainViewModel.onDarkModeChanged(it)
                                },
                                onHistoryClick = {
                                    navController.navigate(route = Destination.History.route)
                                },
                                onBackPressClick = {
                                    navController.navigateUp()
                                })
                        }
                        composable(route = Destination.History.route) {
                            HistoryScreen(
                                onBackPressClick = {
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}