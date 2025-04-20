package com.exyz.simple_todo.data.Database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent



@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideUserDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "user_table"
    ).build()

    @Provides
    fun provideBookDao(
        appDb: AppDatabase
    ) = appDb.userDao()

    @Provides
    fun provideCachedDao(
        appDb: AppDatabase
    ) = appDb.cachedDao()

    @Provides
    fun provideBookRepository(
        userDao: UserDao,
        cachedDao: CachedDao
    ): UserRepository = UserRepositoryImpl(
        userDao = userDao,
        cachedDao = cachedDao
    )
}