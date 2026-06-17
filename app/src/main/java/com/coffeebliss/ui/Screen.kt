package com.coffeebliss.ui

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object AddMember : Screen("add_member")
    object MemberCard : Screen("member_card/{memberId}") {
        fun createRoute(memberId: Int) = "member_card/$memberId"
    }
    object AddTransaction : Screen("add_transaction/{memberId}") {
        fun createRoute(memberId: Int) = "add_transaction/$memberId"
    }
    object RedeemReward : Screen("redeem_reward/{memberId}") {
        fun createRoute(memberId: Int) = "redeem_reward/$memberId"
    }
    object TransactionHistory : Screen("transaction_history/{memberId}") {
        fun createRoute(memberId: Int) = "transaction_history/$memberId"
    }
}
