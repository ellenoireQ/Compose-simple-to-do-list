package com.exyz.simple_todo.uiApp.task

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.exyz.simple_todo.R
import com.exyz.simple_todo.Roboto


@Composable
fun AboutInfo(navController: NavController, onDismissRequest: () -> Unit) {
    BackHandler {
        navController.navigate("home") {
            popUpTo("home") { inclusive = true }
        }
        onDismissRequest()
    }
    Dialog(onDismissRequest = {
        navController.navigate("home") {
            popUpTo("home") { inclusive = true }
        }
        onDismissRequest()
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(top = 12.dp, start = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )) {
                    Icon(
                        painter = painterResource(R.drawable.ic_launcher),
                        null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(12.dp)
                    )
                }
                Column(Modifier.padding(start = 10.dp)) {
                    Text(
                        "Simple Todo-List",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium
                    )
                    Text("v1.0")
                }
            }
            Text(
                buildAnnotatedString {
                    append("Find full source code at ")
                    withLink(
                        LinkAnnotation.Url(
                            "https://github.com/ellenoireQ/Compose-simple-to-do-list",
                            TextLinkStyles(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        )
                    ) {
                        append("Github")
                    }
                },
                modifier = Modifier.padding(23.dp)
            )
        }
    }
}


