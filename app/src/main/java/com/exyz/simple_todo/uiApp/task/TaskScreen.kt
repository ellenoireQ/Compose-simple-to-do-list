package com.exyz.simple_todo.uiApp.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exyz.simple_todo.Poppins
import com.exyz.simple_todo.R
import com.exyz.simple_todo.Roboto
import com.exyz.simple_todo.RobotoFlex
import com.exyz.simple_todo.data.Database.CachedUser
import com.exyz.simple_todo.data.Task
import com.exyz.simple_todo.uiApp.beautifulCard.themePicker

@Composable
fun TaskScreen(task: Task, user: CachedUser, eventId: Int, changeRoute: MutableState<Int>) {
    TestBeautifulTask(task, user, eventId, changeRoute)
}

@Composable
fun TopAppHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(20.dp, top = 80.dp)
    ) {
        Text(
            text = "Task",
            fontFamily = Roboto,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestBeautifulTask(
    taskViewModel: Task,
    event: CachedUser,
    eventId: Int,
    changeRoute: MutableState<Int>,
) {
    var imageSelected: ImageVector
    var colorBackground: Color

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val tasks by taskViewModel.cachedUsers.collectAsState(emptyList())

    changeRoute.value = event.sectionMenu
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Task",
                        fontFamily = Roboto,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(top = 53.dp, bottom = 53.dp)
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        val filteredTasks = remember(tasks) {
            listOf(
                tasks.filter { it.category == 0 },
                tasks.filter { it.category == 1 },
                tasks
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
        ) {

            itemsIndexed(filteredTasks, span = { _, _ -> GridItemSpan(1) }) {index, t ->
                val icon = when (index) {
                    0 -> painterResource(R.drawable.partly_cloudy_filled)
                    1 -> painterResource(R.drawable.light_mode_filled)
                    2 -> painterResource(R.drawable.inbox_filled)
                    else -> painterResource(R.drawable.partly_cloudy_filled)
                }
                val title = when (index){
                    0 -> "Today"
                    1 -> "Tomorrow"
                    2 -> "All"
                    else -> "null"
                }

                TaskList(icon, title, t.size)
            }
            item(span = { GridItemSpan(2) }) {
                Spacer(Modifier.padding(top = 25.dp))
            }
            items(tasks, span = { GridItemSpan(2) }) { t ->
                imageSelected = when (t.isDone) {
                    true -> Icons.Filled.Check
                    false -> ImageVector.vectorResource(R.drawable.pending_filled)
                }
                colorBackground = when (t.isDone) {
                    true -> Color(0xFFA5D6A7)
                    false -> Color(0xFF42A5F5)

                }
                PendingTask(
                    taskViewModel,
                    event,
                    t.tittle,
                    t.times,
                    t.category,
                    imageSelected,
                    colorBackground
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeautifulTaskOnlyHeader(
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Task",
                        fontFamily = Roboto,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(top = 53.dp, bottom = 53.dp)
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
        ) {

            items(3, span = { GridItemSpan(1) }) {t ->
                val icon = when (t) {
                    0 -> painterResource(R.drawable.partly_cloudy_filled)
                    1 -> painterResource(R.drawable.light_mode_filled)
                    2 -> painterResource(R.drawable.inbox_filled)
                    else -> painterResource(R.drawable.partly_cloudy_filled)
                }
                val title = when (t){
                    0 -> "Today"
                    1 -> "Tomorrow"
                    2 -> "All"
                    else -> "null"
                }

                TaskList(icon, title, 0)
            }
            item(span = { GridItemSpan(2) }) {
                Spacer(Modifier.padding(top = 25.dp))
            }
        }
    }
}

@Composable
fun TaskList(imagePainter: Painter, tittle: String, valueSummed: Int) {
    val dark = isSystemInDarkTheme()
    val darkenMode = remember { mutableStateOf(false) }

    var themedColor: Color = MaterialTheme.colorScheme.surfaceContainer
    var tittleColor: Color = MaterialTheme.colorScheme.onSurface
    var bodyColor: Color = MaterialTheme.colorScheme.onSurface
    var iconThemes: Painter
    if (dark) {
        iconThemes = painterResource(R.drawable.dark_mode_filled)
        if (darkenMode.value) {
            iconThemes = painterResource(R.drawable.dark_mode_outline)
        }
    } else {
        iconThemes = painterResource(R.drawable.light_mode_filled)
        if (darkenMode.value) {
            iconThemes = painterResource(R.drawable.light_mode_outline)
        }
    }
    var iconColor: Color = MaterialTheme.colorScheme.primary
    if (darkenMode.value) {
        val colored = listOf(
            themePicker(
                themeColor = MaterialTheme.colorScheme.inverseSurface,
                tittleColor = MaterialTheme.colorScheme.inverseOnSurface,
                bodyColor = MaterialTheme.colorScheme.inverseOnSurface,
                iconThemed = iconThemes,
                iconColors = MaterialTheme.colorScheme.primary
            )
        )

        colored.forEach {
            themedColor = it.themeColor
            tittleColor = it.tittleColor
            bodyColor = it.bodyColor
            iconThemes = it.iconThemed
            iconColor = it.iconColors
        }
    }


    val rf = remember { mutableStateOf(false) }
    val state = remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = themedColor,
        ),
        modifier = Modifier
            .clickable {
                state.value = !state.value
            }
    ) {
            Column(
                Modifier.height(120.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(50.dp)
                        .padding(start = 12.dp, top = 12.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(painter = imagePainter, contentDescription = null, Modifier.size(23.dp))
                }
                    Text(valueSummed.toString(), modifier = Modifier.padding(end = 10.dp))
                }
                Text(
                    text = if (tittle.length < 25)
                        tittle
                    else
                        "${tittle}...",
                    color = tittleColor,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 15.dp, end = 12.dp, top = 12.dp)
                )
            }


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (state.value)
            //FloatingBeautifulCardReadOnly(event, state)
                null
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 3.dp, end = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
        }
    }
}

@Composable
fun PendingTask(
    taskViewModel: Task,
    event: CachedUser,
    title: String,
    times: String,
    category: Int,
    imageVector: ImageVector,
    colorBackground: Color
) {
    val dark = isSystemInDarkTheme()
    var categorySelected by remember { mutableStateOf("") }
    when (category) {
        0 -> categorySelected = "#Today"
        1 -> categorySelected = "#Tomorrow"
        else -> {}
    }
    Surface(modifier = Modifier.height(100.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.padding(start = 12.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    Modifier
                        .clickable { },
                    colors = CardDefaults.cardColors(
                        containerColor = colorBackground
                    )
                ) {
                    Icon(
                        imageVector,
                        contentDescription = null,
                        Modifier
                            .size(23.dp)
                            .padding(4.dp),
                        tint = if (dark) Color.Black else Color.Black
                    )

                }
                VerticalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
            }
            Column(Modifier.padding(12.dp)) {
                Text(title)
                Spacer(Modifier.padding(bottom = 12.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = categorySelected,
                        fontFamily = Poppins,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.padding(start = 12.dp))
                    Text(
                        text = times,
                        fontFamily = Poppins,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Thin,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.padding(start = 12.dp))
                    IconButton(
                        onClick = { taskViewModel.deleteCachedUser(event) },
                        colors = IconButtonDefaults.iconButtonColors(
                            MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.size(25.dp)
                    ) {
                        Icon(Icons.Filled.Delete, null, Modifier.padding(5.dp))
                    }
                }
            }
        }
    }
}