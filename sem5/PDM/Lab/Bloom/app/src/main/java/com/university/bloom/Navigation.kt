package com.university.bloom

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object LoginRoute {
    const val route = "login"
}

object HomeRoute {
    const val route = "home"
}

object ItemRoute {
    private const val ITEM_KEY = "itemId"
    const val route = "item_details/{${ITEM_KEY}}"

    val arguments: List<NamedNavArgument> = listOf(
        navArgument(ITEM_KEY) { type = NavType.StringType }
    )

    fun createNavigationLink(itemId: String?): String {
        return if (itemId == null) {
            "item_details/+"
        } else
            "item_details/$itemId"
    }
}