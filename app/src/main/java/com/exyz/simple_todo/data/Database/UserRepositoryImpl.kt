package com.exyz.simple_todo.data.Database


class UserRepositoryImpl(
    private val userDao: UserDao,
    private val cachedDao: CachedDao
) : UserRepository {

    override fun getUsersFromRoom() = userDao.getUser()

    override suspend fun getUserFromRoom(id: Int) = userDao.getUserByID(id)

    override suspend fun addUserToRoom(user: User) = userDao.addUser(user)

    override suspend fun updateUserInRoom(user: User) = userDao.updateUser(user)

    override suspend fun deleteUserFromRoom(user: User) = userDao.deleteUser(user)

    override suspend fun deleteUserFromId(userId: Int) = userDao.deleteUserById(userId)

    // Cached Dao
    override fun getCachedUserFromRoom() = cachedDao.getUser()

    override suspend fun getCachedUserById(id: Int) = cachedDao.getUserByID(id)

    override suspend fun addCachedUser(user: CachedUser) = cachedDao.addUser(user)

    override suspend fun updateCachedUser(user: CachedUser) = cachedDao.updateUser(user)

    override suspend fun deleteCachedUserFromRoom(user: CachedUser) = cachedDao.deleteUser(user)

    override suspend fun deleteCachedUserById(userId: Int) = cachedDao.deleteUserById(userId)
}