package com.coffeebliss.data.repository

import com.coffeebliss.data.dao.MemberDao
import com.coffeebliss.data.dao.TransactionDao
import com.coffeebliss.data.entity.Member
import com.coffeebliss.data.entity.Transaction
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class CoffeeBlissRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao
) {
    // ==================== MEMBER ====================

    val allMembers: Flow<List<Member>> = memberDao.getAllMembers()

    suspend fun insertMember(member: Member): Long {
        val count = memberDao.getMemberCount()
        val memberWithNumber = member.copy(
            memberNumber = "MBR${(count + 1).toString().padStart(5, '0')}"
        )
        return memberDao.insertMember(memberWithNumber)
    }

    suspend fun updateMember(member: Member) = memberDao.updateMember(member)

    suspend fun getMemberById(id: Int): Member? = memberDao.getMemberById(id)

    suspend fun getMemberByEmail(email: String): Member? = memberDao.getMemberByEmail(email)

    suspend fun addPoints(memberId: Int, points: Int) = memberDao.addPoints(memberId, points)

    suspend fun deductPoints(memberId: Int, points: Int) = memberDao.deductPoints(memberId, points)

    // ==================== TRANSACTION ====================

    fun getTransactionsByMember(memberId: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsByMember(memberId)

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun addTransaction(memberId: Int, amount: Double, description: String = ""): Transaction {
        val pointEarned = (amount / 10000).toInt()
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        val dateStr = dateFormat.format(Date())

        val transaction = Transaction(
            memberId = memberId,
            amount = amount,
            pointEarned = pointEarned,
            date = dateStr,
            description = description
        )
        transactionDao.insertTransaction(transaction)
        memberDao.addPoints(memberId, pointEarned)
        return transaction
    }

    // ==================== REWARD ====================

    data class Reward(val points: Int, val name: String, val description: String)

    val rewards = listOf(
        Reward(50, "Espresso", "Espresso gratis 1 cup"),
        Reward(100, "Cappuccino", "Cappuccino gratis 1 cup"),
        Reward(150, "Latte Gratis", "Cafe Latte gratis 1 cup")
    )

    suspend fun redeemReward(member: Member, reward: Reward): Boolean {
        if (member.points < reward.points) return false
        memberDao.deductPoints(member.id, reward.points)
        // Record redeem as a transaction with negative points
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        val transaction = Transaction(
            memberId = member.id,
            amount = 0.0,
            pointEarned = -reward.points,
            date = dateFormat.format(Date()),
            description = "Redeem: ${reward.name}"
        )
        transactionDao.insertTransaction(transaction)
        return true
    }
}
