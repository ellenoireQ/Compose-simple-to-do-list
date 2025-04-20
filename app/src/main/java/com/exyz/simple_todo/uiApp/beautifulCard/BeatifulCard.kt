package com.exyz.simple_todo.uiApp.beautifulCard;

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.exyz.simple_todo.R
import com.exyz.simple_todo.Roboto
import com.exyz.simple_todo.RobotoFlex
import com.exyz.simple_todo.data.Database.CachedUser
import com.exyz.simple_todo.data.Database.User
import com.exyz.simple_todo.data.Task
import com.exyz.simple_todo.uiApp.BeautifulDialogTest


data class themePicker(
    val themeColor: Color,
    val tittleColor: Color,
    val bodyColor: Color,
    val iconThemed: Painter,
    val iconColors: Color
)

@Composable
fun AmazingCard(
    category: Int,
    context: Context,
    viewModel: Task,
    event: User,
    userId: Int
) {
    val tasks by viewModel.cachedUsers.collectAsState(emptyList())
    val dark = isSystemInDarkTheme()
    val darkenMode = remember { mutableStateOf(false) }
    val rf = remember { mutableStateOf(false) }
    val state = remember { mutableStateOf(false) }
    val rowState = remember { mutableStateOf(false) }
    var themedColor: Color = MaterialTheme.colorScheme.surfaceContainer
    var tittleColor: Color = MaterialTheme.colorScheme.onSurface
    var bodyColor: Color = MaterialTheme.colorScheme.onSurface
    var iconThemes: Painter
    if (dark){
        iconThemes = painterResource(R.drawable.dark_mode_filled)
        if (event.darkenMode) {
            iconThemes = painterResource(R.drawable.dark_mode_outline)
        }
    } else {
        iconThemes = painterResource(R.drawable.light_mode_filled)
        if (event.darkenMode) {
            iconThemes = painterResource(R.drawable.light_mode_outline)
        }
    }
    var iconColor: Color = MaterialTheme.colorScheme.primary
    if (event.darkenMode) {
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

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (event.isDone) Color(0xFFA5D6A7) else themedColor,
        ),
        modifier = Modifier
            .clickable {
                state.value = !state.value
                val user = User(
                    id = event.id,
                    tittle = event.tittle,
                    desc = event.desc,
                    limitTittle = event.limitTittle,
                    times = event.times,
                    darkenMode = darkenMode.value,
                    category = event.category,
                    sectionMenu = event.sectionMenu,
                    isDone = state.value
                )
                val cachedUser = CachedUser(
                    id = event.id,
                    tittle = event.tittle,
                    desc = event.desc,
                    limitTittle = event.limitTittle,
                    times = event.times,
                    darkenMode = event.darkenMode,
                    category = event.category,
                    sectionMenu = event.sectionMenu,
                    isDone = state.value
                )

                viewModel.updateUser(user)
                viewModel.addCachedUser(cachedUser)

                if (!event.isDone)
                    viewModel.deleteUser(user)
            }
    ) {
        Column(
            Modifier.height(120.dp)
        ) {
            TextButton(onClick = {}) {
                Card(shape = CircleShape) {
                    Text("${event.id}", Modifier.padding(10.dp))
                }
            }
            Text(
                text = if (event.tittle.length < 25)
                    event.tittle
                else
                    "${event.tittle}...",
                color = tittleColor,
                fontFamily = RobotoFlex,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp)
            )
            Row() {
                Text(
                    text = event.times,
                    color = bodyColor,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 3.dp, end = 3.dp).horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Card(
                Modifier
                    .padding(start = 6.dp, bottom = 12.dp, top = 6.dp)
                    .clickable { rowState.value = !rowState.value }) {
                if (rowState.value) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
                } else {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }
            if (rowState.value) {
                Card(
                    Modifier
                        .padding(start = 6.dp, bottom = 12.dp)
                        .clickable {
                            darkenMode.value = !darkenMode.value
                            val user = User(
                                id = event.id,
                                tittle = event.tittle,
                                desc = event.desc,
                                limitTittle = event.limitTittle,
                                times = event.times,
                                darkenMode = darkenMode.value,
                                category = event.category,
                                sectionMenu = event.sectionMenu,
                                isDone = event.isDone
                            )
                            viewModel.updateUser(user)
                        }) {
                    Icon(
                        painter = iconThemes,
                        contentDescription = null,
                        Modifier
                            .size(35.dp)
                            .padding(5.dp),
                        tint = iconColor
                    )
                }
                Card(
                    Modifier
                        .padding(start = 6.dp, bottom = 12.dp)
                        .clickable { rf.value = !rf.value }) {
                    Icon(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = null,
                        Modifier
                            .size(35.dp)
                            .padding(5.dp)
                    )
                }
                Card(
                    Modifier
                        .padding(start = 6.dp, bottom = 12.dp)
                        .clickable { viewModel.deleteUser(event) }) {
                    Icon(
                        painter = painterResource(R.drawable.delete),
                        contentDescription = null,
                        Modifier
                            .size(35.dp)
                            .padding(5.dp)
                    )
                }
            }
            if (rf.value)
                BeautifulDialogTest(title = event.tittle, time = event.times, limitTitle = event.limitTittle, category = category, context = context, viewModel = viewModel, event = event, onDismissRequest = rf, index = userId)
        }
    }
}
