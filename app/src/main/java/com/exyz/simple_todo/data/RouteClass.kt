package com.exyz.simple_todo.data


data class TopLevelRoutes<T: Any>(
    val route: T
)

val screenRoute = listOf(
    TopLevelRoutes("Main"),
    TopLevelRoutes("Task"),
    TopLevelRoutes("About")
)