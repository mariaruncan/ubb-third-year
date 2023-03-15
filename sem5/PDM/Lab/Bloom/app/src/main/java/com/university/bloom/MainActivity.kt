package com.university.bloom

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.university.bloom.ui.add.ScooterDestination
import com.university.bloom.ui.authentication.LoginDestination
import com.university.bloom.ui.home.HomeDestination
import com.university.bloom.theming.BloomTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BloomTheme {
                val navHostController: NavHostController = rememberNavController()
                NavHost(navController = navHostController, startDestination = LoginRoute.route) {
                    composable(route = LoginRoute.route) {
                        Log.d("NAVIGATION", "LOGIN_ROUTE")
                        LoginDestination(
                            onLoginSuccessful = {
                                navHostController.navigate(HomeRoute.route)
                            }
                        )
                    }

                    composable(route = HomeRoute.route) {
                        Log.d("NAVIGATION", "HOME")
                        HomeDestination(
                            onItemClick = {
                                navHostController.navigate(ItemRoute.createNavigationLink(it))
                            },
                            onAddClick = {
                                navHostController.navigate(ItemRoute.createNavigationLink(null))
                            },
                            onLogOutClick = {
                                navHostController.navigate(LoginRoute.route) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }

                    composable(
                        route = ItemRoute.route,
                        arguments = ItemRoute.arguments
                    ) {
                        Log.d("NAVIGATION", "ITEM_DETAILS")
                        ScooterDestination(
                            onSaveSuccessful = {
                                navHostController.navigate(HomeRoute.route) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}