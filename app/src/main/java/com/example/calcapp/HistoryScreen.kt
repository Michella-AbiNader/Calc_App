package com.example.calcapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit){
Column(modifier = Modifier
    .fillMaxSize()
    .background(Color.White)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Column(verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .fillMaxHeight()
                .padding(8.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Back Arrow",
                modifier = Modifier.clickable {
                    onEvent(HistoryEvent.HideHistory)
                }
            )

        }
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            Button(
                onClick = {
                    onEvent(HistoryEvent.ClearHistory)
                },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text(text = "Clear History", fontSize = 22.sp)
            }

        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.history) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {

                Column(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.75f)
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = it.equation,
                        fontSize = 16.sp,
                        style = TextStyle(textAlign = TextAlign.Center)

                    )
                    Text(text = it.answer,
                        fontSize = 22.sp,
                        style = TextStyle(textAlign = TextAlign.Center),
                        modifier = Modifier.padding(4.dp)

                    )
                }
                Column(modifier = Modifier.fillMaxHeight().fillMaxWidth(1f)) {
                    IconButton(onClick = {
                        onEvent(HistoryEvent.DeleteHistory(it))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete History"
                        )
                    }
                }
                }

            Divider()
        }
    }

}
}

