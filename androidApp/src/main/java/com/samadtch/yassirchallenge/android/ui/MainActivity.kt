package com.samadtch.yassirchallenge.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.samadtch.yassirchallenge.android.ui.screens.home.HomeScreen
import com.samadtch.yassirchallenge.android.ui.screens.movie.MovieScreen
import com.samadtch.yassirchallenge.android.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Splash Screen
        installSplashScreen()

        //UI
        setContent {
            //------------------------------- Declarations
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()

            //------------------------------- UI
            AppTheme {
                Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
                    NavHost(
                        modifier = Modifier.padding(it),
                        navController = navController, startDestination = "home"
                    ) {

                        //Home Screen
                        composable("home") {
                            HomeScreen(
                                onShowSnackbar = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = it,
                                            actionLabel = null,
                                            duration = SnackbarDuration.Short,
                                        )
                                    }
                                },
                                goMovie = { id -> navController.navigate("movie/$id") }
                            )
                        }

                        //Movie Screen
                        composable(
                            "movie/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { backStackEntry ->
                            MovieScreen(
                                id = backStackEntry.arguments?.getInt("id")!!,
                                onBackPressed = { navController.popBackStack() }
                            )
                        }

                    }
                }
            }
        }
    }
}