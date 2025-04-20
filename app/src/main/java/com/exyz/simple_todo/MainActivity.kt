package com.exyz.simple_todo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.exyz.simple_todo.data.Database.AppDatabase
import com.exyz.simple_todo.data.Database.UserRepository
import com.exyz.simple_todo.data.Task
import com.exyz.simple_todo.data.listTask
import com.exyz.simple_todo.data.sectionMenus
import com.exyz.simple_todo.data.stateFloating
import com.exyz.simple_todo.ui.theme.AperfTheme
import com.exyz.simple_todo.uiApp.beautifulCard.AmazingCard
import com.exyz.simple_todo.uiApp.BeautifulDialog
import com.exyz.simple_todo.uiApp.task.AboutInfo
import com.exyz.simple_todo.uiApp.task.BeautifulTaskOnlyHeader
import com.exyz.simple_todo.uiApp.task.TaskScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.Manifest
import androidx.annotation.RequiresApi

val Poppins = FontFamily(
    Font(R.font.poppins_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.poppins_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.poppins_light, FontWeight.Normal, FontStyle.Normal)
)

val Roboto = FontFamily(
    Font(R.font.roboto_black, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.robotoflex_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.roboto_semicondensed_medium, FontWeight.Medium, FontStyle.Normal)
)

val RobotoFlex = FontFamily(
    Font(R.font.robotoflex_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.robotoflex_regular, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.robotoflex_regular, FontWeight.Bold, FontStyle.Normal)
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var userRepository: UserRepository


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AperfTheme {
                val context = LocalContext.current
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        if (!isGranted) {
                            coroutineScope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Notification permission is required.",
                                    actionLabel = "Settings"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    // Go to notification settings
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                        }
                                        context.startActivity(intent)
                                    }
                                }
                            }
                        }
                    }
                )

                val taskViewModel: Task = viewModel()
                val navController = rememberNavController()
                var bstate by remember { mutableStateOf(false) }
                val listBottomBar = listOf(
                    BottBar(
                        "Home",
                        "home",
                        painterResource(R.drawable.home),
                        painterResource(R.drawable.home_filled),
                        24.dp,
                        1
                    ),
                    BottBar(
                        "Task",
                        "taskscreen",
                        painterResource(R.drawable.task),
                        painterResource(R.drawable.task_filled),
                        24.dp,
                        2
                    ),
                    BottBar(
                        "About",
                        "about",
                        painterResource(R.drawable.info_about),
                        painterResource(R.drawable.info_filled),
                        24.dp,
                        3
                    ),
                )
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route
                val tasks by taskViewModel.users.collectAsState(emptyList())
                val tasksCached by taskViewModel.cachedUsers.collectAsState(emptyList())
                val user = taskViewModel.user
                var idNum by remember { mutableStateOf(0) }
                val isDoneAlarm = remember { mutableStateOf(false) }
                LaunchedEffect(tasksCached){
                    tasksCached.forEach {
                        isDoneAlarm.value = it.isDone
                    }
                }
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    bottomBar = {
                        BottomAppBar(
                            modifier = Modifier.height(90.dp),
                            contentPadding = PaddingValues(top = 12.dp)
                        ) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            listBottomBar.forEachIndexed { index, app ->
                                NavigationBarItem(
                                    onClick = {
                                        bstate = !bstate
                                        navController.navigate(app.routes) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true

                                        }
                                    },
                                    label = { Text(app.tittle) },
                                    icon = {
                                        Icon(
                                            if (currentRoute == app.routes)
                                                app.filledIconImgPainter
                                            else
                                                app.iconImagePainter,
                                            contentDescription = null,
                                            Modifier.size(app.iconSize),
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    selected = currentRoute == app.routes,
                                    alwaysShowLabel = true,
                                )
                            }
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            onClick = { stateFloating.value = true }
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.surface
                ) { innerPadding ->

                    Column(Modifier.padding(innerPadding)) {
                        var valueOfIndex by remember { mutableStateOf(0) }
                        var aboutWindowIsOpened = remember {mutableStateOf(true)}

                        NavHost(
                            navController = navController, startDestination = "home"
                        ) {
                            composable("home") {
                                Scaffold(topBar = {
                                    TopAppBar(
                                        title = {
                                            Text(
                                                text = "Todolist.",
                                                fontFamily = Roboto,
                                                fontStyle = FontStyle.Normal,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                style = MaterialTheme.typography.headlineLarge
                                            )
                                        },
                                    )
                                }) { innerPadding ->
                                    Column(Modifier.padding(innerPadding)) {
                                        if (tasks.isEmpty()) {
                                            Column(
                                                Modifier
                                                    .fillMaxSize()
                                                    .background(MaterialTheme.colorScheme.surface),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "Task is empty, to creating Task, Tap to Button below",
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                FloatingActionButton(
                                                    modifier = Modifier.padding(top = 1.dp),
                                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                                    onClick = { stateFloating.value = true }
                                                ) {
                                                    Icon(
                                                        Icons.Filled.Add,
                                                        contentDescription = null
                                                    )
                                                }
                                            }
                                        } else {
                                            LazyVerticalGrid(
                                                columns = GridCells.Fixed(2),
                                                contentPadding = PaddingValues(
                                                    horizontal = 10.dp,
                                                    vertical = 10.dp
                                                ),
                                                verticalArrangement = Arrangement.spacedBy(15.dp),
                                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                                modifier = Modifier.padding(top = 23.dp)
                                            ) {
                                                itemsIndexed(tasks) { index, fl ->
                                                    AmazingCard(
                                                        fl.category,
                                                        this@MainActivity,
                                                        taskViewModel,
                                                        event = fl,
                                                        index
                                                    )
                                                    listTask.add(tasks.size)
                                                    taskViewModel.updateUser(taskViewModel.user)
                                                }
                                            }
                                        }
                                        if (stateFloating.value)
                                            BeautifulDialog(
                                                context = this@MainActivity,
                                                event = user,
                                                viewModel = taskViewModel,
                                                onDismissRequest = stateFloating,
                                                index = valueOfIndex
                                            )
                                    }
                                }
                            }
                            composable("taskscreen") {
                                val routeSectionMenus = remember { mutableStateOf(sectionMenus) }
                                if (tasksCached.isNotEmpty()) {
                                    tasksCached.forEach {
                                        TaskScreen(
                                            taskViewModel,
                                            it,
                                            valueOfIndex,
                                            routeSectionMenus
                                        )
                                    }
                                } else if (tasksCached.isEmpty()) {
                                    BeautifulTaskOnlyHeader()
                                }



                                if (stateFloating.value)
                                    BeautifulDialog(
                                        context = this@MainActivity,
                                        event = user,
                                        viewModel = taskViewModel,
                                        onDismissRequest = stateFloating,
                                        index = 0
                                    )
                            }
                            composable("about") {
                                AboutInfo(navController = navController ,onDismissRequest = { aboutWindowIsOpened.value = false })
                            }
                        }
                    }
                }
            }
        }
    }
}


data class BottBar(
    val tittle: String,
    val routes: String,
    val iconImagePainter: Painter,
    val filledIconImgPainter: Painter,
    val iconSize: Dp,
    var count: Int
)

data class Main(val id: String)

@Composable
@Preview
fun taskText() {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Task is empty, to creating Task, Tap to Button below",
            style = MaterialTheme.typography.bodyLarge
        )
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.tertiary,
            onClick = { true }
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
        }
    }
}

@Composable
fun TopAppHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(20.dp, top = 90.dp)
    ) {
        Text(
            text = "TodoList",
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(
                fontFamily = Roboto,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Thin,
                fontSize = 30.sp
            )
        )
    }
}

@Composable
fun BottomApp(
    navController: NavController,
) {

}
