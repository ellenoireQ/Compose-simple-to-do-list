package com.exyz.simple_todo.data.Database

import kotlinx.coroutines.flow.Flow

typealias Users = List<User>

interface UserRepository{
    fun getUsersFromRoom(): Flow<Users>

    suspend fun getUserFromRoom(id: Int): User

    suspend fun addUserToRoom(user: User)

    suspend fun updateUserInRoom(user: User)

    suspend fun deleteUserFromRoom(user: User)

    suspend fun deleteUserFromId(userId: Int)

    fun getCachedUserFromRoom(): Flow<List<CachedUser>>

    suspend fun getCachedUserById(id: Int): CachedUser

    suspend fun addCachedUser(user: CachedUser)

    suspend fun updateCachedUser(user: CachedUser)

    suspend fun deleteCachedUserFromRoom(user: CachedUser)

    suspend fun deleteCachedUserById(userId: Int)
}