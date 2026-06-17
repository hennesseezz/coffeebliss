package com.coffeebliss.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.coffeebliss.ui.screens.*
import com.coffeebliss.viewmodel.CoffeeBlissViewModel

@Composable
fun CoffeeBlissNavGraph() {
    val navController = rememberNavController()
    val viewModel: CoffeeBlissViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAddMember = { navController.navigate(Screen.AddMember.route) },
                onMemberClick = { member ->
                    viewModel.selectMember(member)
                    navController.navigate(Screen.MemberCard.createRoute(member.id))
                }
            )
        }

        composable(Screen.AddMember.route) {
            AddMemberScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.MemberCard.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            MemberCardScreen(
                memberId = memberId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onAddTransaction = { navController.navigate(Screen.AddTransaction.createRoute(memberId)) },
                onRedeemReward = { navController.navigate(Screen.RedeemReward.createRoute(memberId)) },
                onViewHistory = { navController.navigate(Screen.TransactionHistory.createRoute(memberId)) }
            )
        }

        composable(
            route = Screen.AddTransaction.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            AddTransactionScreen(
                memberId = memberId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.RedeemReward.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            RedeemRewardScreen(
                memberId = memberId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TransactionHistory.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            TransactionHistoryScreen(
                memberId = memberId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
