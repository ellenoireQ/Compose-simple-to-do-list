package com.exyz.simple_todo.uiApp.beautifulCard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.exyz.simple_todo.R
import com.exyz.simple_todo.data.Database.User
import com.exyz.simple_todo.data.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingBeautifulCard(
    taskViewModel: Task,
    event: User,
    onDismissRequest: MutableState<Boolean>,
    darkTheme: MutableState<Boolean>
) {
    var eventTittle by remember { mutableStateOf(event.tittle) }
    var eventDesc by remember { mutableStateOf(event.desc) }
    var eventTime by remember { mutableStateOf(event.times) }
    BasicAlertDialog(
        onDismissRequest = { onDismissRequest.value = false },
        modifier = Modifier.clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.info_about),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 16.dp)
            )
            Text(text = "Information")
            OutlinedTextField(
                value = eventTittle,
                maxLines = 5,
                onValueChange = {
                    if (eventTittle.length <= 30) {
                        eventTittle = it
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                label = { Text("Event name") },
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
            )
            OutlinedTextField(
                value = eventDesc,
                onValueChange = {
                    eventDesc = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                label = { Text("Description") },
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
            )
            OutlinedTextField(
                value = eventTime,
                onValueChange = {
                    eventTime = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                label = { Text("Enter Times") },
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
            )
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.padding(30.dp),
                    horizontalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    TextButton(
                        onClick = { onDismissRequest.value = false}
                    ){
                        Text(
                            text = "Dismiss",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    TextButton(
                        onClick = {
                                val user = User(
                                    tittle = eventTittle,
                                    desc = eventDesc,
                                    limitTittle = eventTittle,
                                    times = eventTime,
                                    darkenMode = darkTheme.value,
                                    category = event.category,
                                    sectionMenu = event.sectionMenu,
                                    isDone = event.isDone
                                )

                                taskViewModel.updateUser(user)
                                onDismissRequest.value = false
                        }
                    ){
                        Text(
                            text = "Update",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingBeautifulCardReadOnly(
    event: User,
    onDismissRequest: MutableState<Boolean>
) {
    var headerText by remember { mutableStateOf("") }
    BasicAlertDialog(
        onDismissRequest = { onDismissRequest.value = false },
        modifier = Modifier.clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.info_about),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 16.dp)
            )
            Text(text = "Information")
            OutlinedTextField(
                value = event.tittle,
                readOnly = true,
                onValueChange = {
                    headerText = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                label = { Text("Event name") },
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
            )
            OutlinedTextField(
                value = event.desc,
                readOnly = true,
                onValueChange = {

                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                label = { Text("Description") },
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
            )
            OutlinedTextField(
                value = headerText,
                readOnly = true,
                onValueChange = {
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                label = { Text("Enter Times") },
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
            )
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.padding(30.dp),
                    horizontalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    TextButton(
                        onClick = { onDismissRequest.value = false}
                    ){
                        Text(
                            text = "Dismiss",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    TextButton(
                        onClick = {}
                    ){
                        Text(
                            text = "Update",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}