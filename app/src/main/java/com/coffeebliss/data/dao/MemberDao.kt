package com.coffeebliss.data.dao

import androidx.room.*
import com.coffeebliss.data.entity.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member): Long

    @Update
    suspend fun updateMember(member: Member)

    @Delete
    suspend fun deleteMember(member: Member)

    @Query("SELECT * FROM members ORDER BY name ASC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getMemberById(id: Int): Member?

    @Query("SELECT * FROM members WHERE email = :email LIMIT 1")
    suspend fun getMemberByEmail(email: String): Member?

    @Query("UPDATE members SET points = points + :points WHERE id = :memberId")
    suspend fun addPoints(memberId: Int, points: Int)

    @Query("UPDATE members SET points = points - :points WHERE id = :memberId")
    suspend fun deductPoints(memberId: Int, points: Int)

    @Query("SELECT COUNT(*) FROM members")
    suspend fun getMemberCount(): Int
}
