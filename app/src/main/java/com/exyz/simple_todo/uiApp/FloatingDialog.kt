package com.exyz.simple_todo.uiApp

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.exyz.simple_todo.Roboto
import com.exyz.simple_todo.RobotoFlex
import com.exyz.simple_todo.data.Database.CachedUser
import com.exyz.simple_todo.data.Database.User
import com.exyz.simple_todo.data.Task
import com.exyz.simple_todo.data.hourTime
import com.exyz.simple_todo.data.listTime
import com.exyz.simple_todo.data.sectionMenus
import com.exyz.simple_todo.uiApp.notificationUI.scheduleExactAlarm
import java.util.Calendar


//@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BeautifulDialog(
    context: Context,
    viewModel: Task,
    event: User,
    onDismissRequest: MutableState<Boolean>,
    index: Int
) {
    //viewModel: Task,
    //onDismissRequest: MutableState<Boolean>
    var categoryIndex by remember { mutableIntStateOf(0) }
    val st = remember { mutableStateOf(false) }
    var state = remember { mutableStateOf(false) }
    var text = remember { mutableStateOf(TextFieldValue("")) }
    var headerText by remember { mutableStateOf("") }
    var limitTittle by remember { mutableStateOf("") }

    //val onDismissRequest = remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()
    val categoryList = listOf(
        "Today",
        "Tomorrow",
    )

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    var thatText by remember { mutableStateOf("") }
    var selectedHour by remember { mutableIntStateOf(0) }
    var selectedMinute by remember { mutableIntStateOf(0) }
    var placeholderText by remember {
        mutableStateOf(String.format("%02d:%02d", selectedHour, selectedMinute))
    }
    val isError = headerText.isBlank()
    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
        onDismissRequest = {
            onDismissRequest.value = false
        }) {
        Scaffold(
            modifier = Modifier.fillMaxWidth(),
            topBar = {
                HeaderApp(
                    viewModel = viewModel,
                    event = event,
                    onDismissListener = onDismissRequest,
                    text = headerText,
                    index = index,
                    desc = text.value.text,
                    limitTittle = limitTittle,
                    time = placeholderText,
                    darkenTheme = st,
                    category = categoryIndex,
                    sectionMenu = sectionMenus,
                    state = isError,
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) { innerPadding ->
            Column(
                Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.padding(10.dp))
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                ) {
                    Text(
                        text = "Event",
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 5.dp, start = 20.dp)
                    )
                }
                OutlinedTextField(
                    value = headerText,
                    onValueChange = {
                        headerText = it
                        if (headerText.length <= 30) {
                            limitTittle = it
                        }
                    },
                    isError = isError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    label = { Text("Event name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                )
                OutlinedTextField(
                    value = text.value,
                    onValueChange = { it: TextFieldValue ->
                        text.value = it.copy(text = it.text.replace("\n", "\n"))
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    label = { Text("Enter Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(20.dp)
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                ) {
                    Text(
                        text = "Times & category",
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }
                OutlinedTextField(
                    value = thatText,
                    onValueChange = {},
                    singleLine = true,
                    label = {
                        Text("Pick time")
                    },
                    trailingIcon = {
                        Icon(
                            if (state.value) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },
                    placeholder = { Text(placeholderText) },
                    interactionSource = remember { MutableInteractionSource() }
                        .also { mutableInteractionSource ->
                            LaunchedEffect(mutableInteractionSource) {
                                mutableInteractionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        state.value = !state.value

                                    }
                                }
                            }
                        },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                )
                if (state.value) {

                    DialWithDialogExample(
                        onConfirm = {
                            selectedHour = timePickerState.hour
                            selectedMinute = timePickerState.minute
                            thatText = String.format("%02d:%02d", selectedHour, selectedMinute)
                            placeholderText = thatText
                            hourTime.add(selectedHour)
                            listTime.add(placeholderText)
                            currentTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                            currentTime.set(Calendar.MINUTE, selectedMinute)
                            Log.i("Hallo", "Hour: $selectedHour, Minute: $selectedMinute")
                            scheduleExactAlarm(context, selectedHour, selectedMinute, headerText, categoryIndex)
                        },
                        timePickerState = timePickerState,
                        showDialog = state.value,
                        onShowDialogChange = { state.value = !state.value }
                    )
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 20.dp, end = 12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    FlowRow(
                        maxItemsInEachRow = 3,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        categoryList.forEachIndexed { indexed, t ->
                            var buttonChecked by remember { mutableStateOf(false) }
                            FilterChip(
                                onClick = {
                                    buttonChecked = !buttonChecked
                                    categoryIndex = indexed
                                },
                                selected = buttonChecked,
                                leadingIcon = {
                                    if (buttonChecked) {
                                        Icon(Icons.Rounded.Done, contentDescription = null)
                                    } else {
                                        Icon(Icons.Rounded.Add, contentDescription = null)
                                    }
                                },
                                label = { Text(t) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BeautifulDialogTest(
    title: String,
    time: String,
    limitTitle: String,
    category: Int,
    context: Context,
    viewModel: Task,
    event: User,
    onDismissRequest: MutableState<Boolean>,
    index: Int
) {
    //viewModel: Task,
    //onDismissRequest: MutableState<Boolean>
    var categoryIndex by remember { mutableIntStateOf(0) }
    val st = remember { mutableStateOf(false) }
    var state = remember { mutableStateOf(false) }
    var text = remember { mutableStateOf(TextFieldValue("")) }
    var headerText by remember { mutableStateOf(title) }
    var limitTittle by remember { mutableStateOf(limitTitle) }

    //val onDismissRequest = remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    var thatText by remember { mutableStateOf(time) }
    var selectedHour by remember { mutableIntStateOf(0) }
    var selectedMinute by remember { mutableIntStateOf(0) }
    var placeholderText by remember {
        mutableStateOf(String.format("%02d:%02d", selectedHour, selectedMinute))
    }
    val isError = headerText.isBlank()
    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
        onDismissRequest = {
            onDismissRequest.value = false
        }) {
        Scaffold(
            modifier = Modifier.fillMaxWidth(),
            topBar = {
                HeaderAppUpdate(
                    viewModel = viewModel,
                    event = event,
                    onDismissListener = onDismissRequest,
                    text = headerText,
                    index = index,
                    desc = text.value.text,
                    limitTittle = limitTittle,
                    time = placeholderText,
                    darkenTheme = st,
                    category = categoryIndex,
                    sectionMenu = sectionMenus,
                    state = isError,
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) { innerPadding ->
            Column(
                Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.padding(10.dp))
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                ) {
                    Text(
                        text = "Event",
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 5.dp, start = 20.dp)
                    )
                }
                OutlinedTextField(
                    value = headerText,
                    onValueChange = {
                        headerText = it
                        if (headerText.length <= 30) {
                            limitTittle = it
                        }
                    },
                    isError = isError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    label = { Text("Event name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                )
                OutlinedTextField(
                    value = text.value,
                    onValueChange = { it: TextFieldValue ->
                        text.value = it.copy(text = it.text.replace("\n", "\n"))
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    label = { Text("Enter Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(20.dp)
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                ) {
                    Text(
                        text = "Times & category",
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }
                OutlinedTextField(
                    value = thatText,
                    onValueChange = {},
                    singleLine = true,
                    label = {
                        Text("Pick time")
                    },
                    trailingIcon = {
                        Icon(
                            if (state.value) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },
                    placeholder = { Text(placeholderText) },
                    interactionSource = remember { MutableInteractionSource() }
                        .also { mutableInteractionSource ->
                            LaunchedEffect(mutableInteractionSource) {
                                mutableInteractionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        state.value = !state.value

                                    }
                                }
                            }
                        },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                )
                if (state.value) {

                    DialWithDialogExample(
                        onConfirm = {
                            selectedHour = timePickerState.hour
                            selectedMinute = timePickerState.minute
                            thatText = String.format("%02d:%02d", selectedHour, selectedMinute)
                            placeholderText = thatText
                            hourTime.add(selectedHour)
                            listTime.add(placeholderText)
                            currentTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                            currentTime.set(Calendar.MINUTE, selectedMinute)
                            Log.i("Hallo", "Hour: $selectedHour, Minute: $selectedMinute, index: $categoryIndex")
                            scheduleExactAlarm(context, selectedHour, selectedMinute, headerText, categoryIndex)
                        },
                        timePickerState = timePickerState,
                        showDialog = state.value,
                        onShowDialogChange = { state.value = !state.value }
                    )
                }
                var buttonChecked1 by remember { mutableStateOf(event.category == 0) }
                var buttonChecked2 by remember { mutableStateOf(event.category == 1) }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 20.dp, end = 12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                            FilterChip(
                                onClick = {
                                    buttonChecked1 = !buttonChecked1
                                    buttonChecked2 = false
                                    val user = User(
                                        id = event.id,
                                        tittle = event.tittle,
                                        desc = event.desc,
                                        limitTittle = event.limitTittle,
                                        times = event.times,
                                        darkenMode = event.darkenMode,
                                        category = 0,
                                        sectionMenu = event.sectionMenu,
                                        isDone = event.isDone
                                    )
                                    val cachedUser = CachedUser(
                                        id = event.id,
                                        tittle = event.tittle,
                                        desc = event.desc,
                                        limitTittle = event.limitTittle,
                                        times = event.times,
                                        darkenMode = event.darkenMode,
                                        category = 0,
                                        sectionMenu = event.sectionMenu,
                                        isDone = event.isDone
                                    )
                                    viewModel.updateCachedUser(cachedUser)
                                    viewModel.updateUser(user)

                                    Log.i("SDSDSDSDSDSDSDSDSD", event.category.toString())
                                },
                                selected = buttonChecked1,
                                leadingIcon = {
                                    if (buttonChecked1) {
                                        Icon(Icons.Rounded.Done, contentDescription = null)
                                    } else {
                                        Icon(Icons.Rounded.Add, contentDescription = null)
                                    }
                                },
                                label = { Text("Today") },
                            )
                        FilterChip(
                            onClick = {
                                buttonChecked2 = !buttonChecked2
                                buttonChecked1 = false
                                val user = User(
                                    id = event.id,
                                    tittle = event.tittle,
                                    desc = event.desc,
                                    limitTittle = event.limitTittle,
                                    times = event.times,
                                    darkenMode = event.darkenMode,
                                    category = 1,
                                    sectionMenu = event.sectionMenu,
                                    isDone = event.isDone
                                )
                                val cachedUser = CachedUser(
                                    id = event.id,
                                    tittle = event.tittle,
                                    desc = event.desc,
                                    limitTittle = event.limitTittle,
                                    times = event.times,
                                    darkenMode = event.darkenMode,
                                    category = 0,
                                    sectionMenu = event.sectionMenu,
                                    isDone = event.isDone
                                )
                                viewModel.updateCachedUser(cachedUser)
                                viewModel.updateUser(user)


                                Log.i("SDSDSDSDSDSDSDSDSD", event.category.toString())
                            },
                            selected = buttonChecked2,
                            leadingIcon = {
                                if (buttonChecked2) {
                                    Icon(Icons.Rounded.Done, contentDescription = null)
                                } else {
                                    Icon(Icons.Rounded.Add, contentDescription = null)
                                }
                            },
                            label = { Text("Tomorrow") },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithDialogExample(
    onConfirm: () -> Unit,
    timePickerState: TimePickerState,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit
) {
    val currentTime = Calendar.getInstance()

    AlertDialog(
        onDismissRequest = { onShowDialogChange(false) },
        title = { Text("Select time") },
        text = { TimePicker(state = timePickerState) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onShowDialogChange(false)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onShowDialogChange(false) }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun HeaderApp(
    viewModel: Task,
    onDismissListener: MutableState<Boolean>,
    event: User,
    text: String,
    index: Int,
    desc: String,
    limitTittle: String,
    time: String,
    darkenTheme: MutableState<Boolean>,
    category: Int,
    sectionMenu: Int,
    state: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.padding(horizontal = 5.dp))
            Icon(
                Icons.Filled.Close,
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp)
                    .clickable {
                        onDismissListener.value = false
                    }
            )
            Spacer(Modifier.padding(horizontal = 5.dp))
            Text(
                text = "New event",
                fontFamily = Roboto,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            )
        }
        TextButton(
            onClick = {
                val user = User(
                    tittle = text,
                    desc = desc,
                    limitTittle = limitTittle,
                    times = time,
                    darkenMode = darkenTheme.value,
                    category = category,
                    sectionMenu = sectionMenu,
                    isDone = state
                )
                viewModel.addUser(user)
                onDismissListener.value = false
            },
            enabled = !state
        ) {
            Text(
                text = "Save",
                fontFamily = Roboto,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(horizontal = 15.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun HeaderAppUpdate(
    viewModel: Task,
    onDismissListener: MutableState<Boolean>,
    event: User,
    text: String,
    index: Int,
    desc: String,
    limitTittle: String,
    time: String,
    darkenTheme: MutableState<Boolean>,
    category: Int,
    sectionMenu: Int,
    state: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.padding(horizontal = 5.dp))
            Icon(
                Icons.Filled.Close,
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp)
                    .clickable {
                        onDismissListener.value = false
                    }
            )
            Spacer(Modifier.padding(horizontal = 5.dp))
            Text(
                text = "New event",
                fontFamily = Roboto,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            )
        }
        TextButton(
            onClick = {
                val user = User(
                    id = event.id,
                    tittle = text,
                    desc = desc,
                    limitTittle = limitTittle,
                    times = time,
                    darkenMode = darkenTheme.value,
                    category = event.category,
                    sectionMenu = sectionMenu,
                    isDone = state
                )
                val cachedUser = CachedUser(
                    id = event.id,
                    tittle = text,
                    desc = desc,
                    limitTittle = limitTittle,
                    times = time,
                    darkenMode = darkenTheme.value,
                    category = event.category,
                    sectionMenu = sectionMenu,
                    isDone = state
                )
                viewModel.updateCachedUser(cachedUser)
                viewModel.updateUser(user)
                onDismissListener.value = false
            },
            enabled = !state
        ) {
            Text(
                text = "Save",
                fontFamily = Roboto,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(horizontal = 15.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}