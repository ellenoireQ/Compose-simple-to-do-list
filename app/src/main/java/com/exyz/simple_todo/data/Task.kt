package com.exyz.simple_todo.data

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exyz.simple_todo.data.Database.CachedUser
import com.exyz.simple_todo.data.Database.User
import com.exyz.simple_todo.data.Database.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import javax.inject.Inject

/*                              *
*                               *
*       Data Stored At Here     *
*                               *
*                               */

/*  @Serializable   */
@Serializable
object Home         // Home  -> MainActivity.kt

@Serializable
object TaskScreen   // Task -> MainActivity.kt

@Serializable
object About        // About -> MainActivity.kt
/*  End Of Serializable */

// --
var listTime = mutableListOf("")
var hourTime = mutableListOf(0)
var listTask = mutableListOf(0)
var category1 = mutableListOf(0)
var category2 = mutableListOf(0)
var category3 = mutableListOf(0)
var sectionMenus = 0


val stateFloating = mutableStateOf(false)
//

/*                      *
*                       *
*       Task Function   *
*                       *
*                       */
@HiltViewModel
class Task @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<User>>(emptyList())
    val tasks: StateFlow<List<User>> = _tasks

    private val _sharedFloating = mutableStateOf(false)
    val sharedFloating: State<Boolean> get() = _sharedFloating

    private val _categoryOne = mutableStateOf<List<CategoryOne>>(emptyList())
    val categoryOne: State<List<CategoryOne>> = _categoryOne

    private val _categoryTwo = mutableStateOf<List<CategoryTwo>>(emptyList())
    val categoryTwo: State<List<CategoryTwo>> = _categoryTwo

    private val _categoryThree = mutableStateOf<List<CategoryThree>>(emptyList())
    val categoryThree: State<List<CategoryThree>> = _categoryThree


    var user by mutableStateOf(User(0, "","" ,"", "", false, 0, 0, false))
    var userd by mutableStateOf(User(0, "","" ,"", "", false, 0, 0, false))
    val users = repo.getUsersFromRoom()
    val cachedUsers = repo.getCachedUserFromRoom()


    init {
        viewModelScope.launch {
            users.collect { taskList ->
                val filteredCategory = taskList.filter { it.category == 0 }
                val filteredCategory1 = taskList.filter { it.category == 1 }
                val filteredCategory2 = taskList.filter { it.category == 2 }

                _categoryOne.value = filteredCategory.map {
                    CategoryOne(
                        tittle = it.tittle,
                        desc = it.desc,
                        limitTittle = it.limitTittle,
                        times = it.times,
                        darkenMode = it.darkenMode,
                        category = it.category
                    )
                }

                _categoryTwo.value = filteredCategory1.map {
                    CategoryTwo(
                        tittle = it.tittle,
                        desc = it.desc,
                        limitTittle = it.limitTittle,
                        times = it.times,
                        darkenMode = it.darkenMode,
                        category = it.category
                    )
                }

                _categoryThree.value = filteredCategory2.map {
                    CategoryThree(
                        tittle = it.tittle,
                        desc = it.desc,
                        limitTittle = it.limitTittle,
                        times = it.times,
                        darkenMode = it.darkenMode,
                        category = it.category
                    )
                }
            }
        }
    }

    fun addCachedUser(user: CachedUser) = viewModelScope.launch {
        repo.addCachedUser(user)

        Log.i("LOG TRY", user.toString())
    }

    fun updateCachedUser(user: CachedUser) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repo.updateCachedUser(user)
        }
    }
    fun deleteCachedUser(user: CachedUser) = viewModelScope.launch {
        delay(500)

        repo.deleteCachedUserFromRoom(user)
        repo.deleteCachedUserById(user.id)

        Log.i("LOGG", "${user.id}: with idx $user")
    }

    fun addUser(user: User?) = viewModelScope.launch {
         if (user != null) {
             repo.addUserToRoom(user)

             val cachedUser = CachedUser(
                 id = user.id,
                 tittle = user.tittle,
                 desc = user.desc,
                 limitTittle = user.limitTittle,
                 times = user.times,
                 darkenMode = user.darkenMode,
                 category = user.category,
                 sectionMenu = user.sectionMenu,
                 isDone = user.isDone
             )
             addCachedUser(cachedUser)
             Log.i("LOGG", "${user.id}: with idx $user")

         }
    }

    fun updateUser(user: User) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repo.updateUserInRoom(user)
        }
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        delay(500)

        repo.deleteUserFromRoom(user)
        repo.deleteUserFromId(user.id)

        Log.i("LOGG", "${user.id}: with idx $user")
    }
    fun deleteUserById(id: Int) = viewModelScope.launch {
        repo.deleteUserFromId(id)
    }
}