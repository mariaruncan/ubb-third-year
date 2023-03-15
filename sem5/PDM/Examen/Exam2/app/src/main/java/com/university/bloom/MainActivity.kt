package com.university.bloom

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.university.bloom.data.item.ItemRepository
import com.university.bloom.ui.authentication.LoginDestination
import com.university.bloom.theming.BloomTheme
import com.university.bloom.ui.home.HomeDestination
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

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
                                navHostController.navigate(HomeRoute.route) {
                                    popUpTo(LoginRoute.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    composable(route = HomeRoute.route) {
                        Log.d("NAVIGATION", "HOME")
                        HomeDestination()
                    }
                }
            }
        }
    }
}