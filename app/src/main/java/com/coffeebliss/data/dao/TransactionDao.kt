package com.coffeebliss.data.dao

import androidx.room.*
import com.coffeebliss.data.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE memberId = :memberId ORDER BY date DESC")
    fun getTransactionsByMember(memberId: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT SUM(pointEarned) FROM transactions WHERE memberId = :memberId")
    suspend fun getTotalPointsEarned(memberId: Int): Int?

    @Query("SELECT COUNT(*) FROM transactions WHERE memberId = :memberId")
    suspend fun getTransactionCount(memberId: Int): Int
}
