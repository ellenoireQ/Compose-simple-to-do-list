package com.exyz.simple_todo.data.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tittle: String,
    val desc: String,
    val limitTittle: String,
    val times: String,
    val darkenMode: Boolean,
    val category: Int,
    var sectionMenu: Int,
    val isDone: Boolean,
)

@Entity(tableName = "cached_table")
data class CachedUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tittle: String,
    val desc: String,
    val limitTittle: String,
    val times: String,
    val darkenMode: Boolean,
    val category: Int,
    var sectionMenu: Int,
    val isDone: Boolean
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getUser(): Flow<Users>

    @Query("SELECT * FROM user_table WHERE id = :id")
    suspend fun getUserByID(id: Int): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user_table WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)
}

@Dao
interface CachedDao {
    @Query("SELECT * FROM cached_table ORDER BY id ASC")
    fun getUser(): Flow<List<CachedUser>>

    @Query("SELECT * FROM cached_table WHERE id = :id")
    suspend fun getUserByID(id: Int): CachedUser

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: CachedUser)

    @Update
    suspend fun updateUser(user: CachedUser)

    @Delete
    suspend fun deleteUser(user: CachedUser)

    @Query("DELETE FROM cached_table WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)
}