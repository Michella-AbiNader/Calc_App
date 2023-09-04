package com.example.calcapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign.Companion.Right
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Dao
import androidx.room.Room
import com.example.calcapp.ui.theme.CalcAppTheme

class MainActivity : ComponentActivity() {
    private val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            HistoryDatabase::class.java,
            "contacts.db"
        ).allowMainThreadQueries().build()
    }

    private val viewModel by viewModels<HistoryViewModel>(
        factoryProducer ={
            object: ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HistoryViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsState()
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT){
                Greeting(modifier = Modifier, state = state, onEvent = viewModel::onEvent)
            }else{
                Greeting2(modifier = Modifier)

            }
        }
    }

}


@Composable
fun Greeting(modifier: Modifier, state: HistoryState, onEvent: (HistoryEvent) -> Unit) {
    val charSequence : CharSequence = "+/*-"
    var textValue by remember { mutableStateOf("") }
    var resultValue by remember{ mutableStateOf("")}

    if(state.isHistory){
        HistoryScreen(state = state, onEvent = onEvent)
    }

    Column(
        modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(8.dp)
            .padding(top = 30.dp)
    ) {
        if(state.isHistory){
            HistoryScreen(state = state, onEvent = onEvent)
        }
        Text(
            text = textValue,
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            style = TextStyle(fontSize = 40.sp, textAlign = Right, color = Color.White),
            maxLines = 2
            )
        Spacer(Modifier.height(20.dp))
        Text(
            text = resultValue,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            style = TextStyle(fontSize = 30.sp, textAlign = Right),
            color = Color.Gray
        )
        Spacer(Modifier.height(20.dp))
        Divider()
        Spacer(Modifier.height(20.dp))

//Row 1
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.20f)
        ){

            Button(onClick = {
                onEvent(HistoryEvent.ShowHistory)
            },
                colors = ButtonDefaults.buttonColors(Color.Gray, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()

                ){
                Icon(painter = painterResource(id = R.drawable.ic_history), contentDescription = "See history",
                modifier = Modifier.fillMaxSize())
            }



            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())
            Button(onClick = {
                textValue = ""
                resultValue = ""
            },
                colors = ButtonDefaults.buttonColors(Color.Gray, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .fillMaxHeight()

            ){
                Text(text = "C",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())

            Button(onClick = {
                if(textValue.length == 1){
                    resultValue = ""
                    textValue = ""
                }else if(textValue.isNotBlank()) {
                    textValue = textValue.dropLast(1)
                }
            },
                colors = ButtonDefaults.buttonColors(Color.Gray, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()

            ){
                Icon(painter = painterResource(R.drawable.ic_delete), contentDescription = "Remove one item",
                    modifier = Modifier.fillMaxSize())
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())

            Button(onClick = {
                if(charSequence.any{ textValue.endsWith(it, true) }){
                    textValue = textValue.dropLast(1)
                    textValue+="/"
                    }else if(textValue == ""){
                        //do nothing
                    }
                    else{
                        textValue += "/"
                }
            },
                colors = ButtonDefaults.buttonColors(Color(1f, 0.7f, 0.12f, 1f), Color.White),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()

            ){
                Text(text = "/",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
        }
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(10.dp))

//        Row 2
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
        ){
            Button(onClick = {
                textValue += "7"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()

            ){
                Text(text = "7",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())
            Button(onClick = {
                textValue += "8"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .fillMaxHeight()

            ){
                Text(text = "8",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())

            Button(onClick = {
                textValue += "9"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()

            ){
                Text(text = "9",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())

            Button(onClick = {
                if(charSequence.any{ textValue.endsWith(it, true) }){
                    textValue = textValue.dropLast(1)
                    textValue+="*"
                }else if(textValue == ""){//do nothing
                     }
                else{
                    textValue += "*"
                }
            },
                colors = ButtonDefaults.buttonColors(Color(1f, 0.7f, 0.12f, 1f), Color.White),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()

            ){
                Text(text = "*",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
        }
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(10.dp))

//        Row 3
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.33f)
        ){
            Button(onClick = {
                textValue += "4"
                resultValue = Model().result(textValue)
            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()

            ){
                Text(text = "4",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())
            Button(onClick = {
                textValue += "5"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .fillMaxHeight()

            ){
                Text(text = "5",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())

            Button(onClick = {
                textValue += "6"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()

            ){
                Text(text = "6",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())

            Button(onClick = {
                if(textValue.endsWith("*-") or textValue.endsWith("/-")){
                    //do nothing
                }
                else if(textValue.endsWith("+", true)){
                    textValue = textValue.dropLast(1)
                    textValue+="-"
                }else if(textValue.endsWith("-")){
//                    do noting
                }
                else{
                    textValue += "-"
                }

            },
                colors = ButtonDefaults.buttonColors(Color(1f, 0.7f, 0.12f, 1f), Color.White),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()

            ){
                Text(text = "-",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
        }
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(10.dp))

//        Row 4
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
        ){
            Button(onClick = {
                textValue += "1"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()

            ){
                Text(text = "1",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())
            Button(onClick = {
                textValue += "2"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .fillMaxHeight()

            ){
                Text(text = "2",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())

            Button(onClick = {
                textValue += "3"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()

            ){
                Text(text = "3",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())

            Button(onClick = {
                if(charSequence.any{ textValue.endsWith(it, true) }){
                    textValue = textValue.dropLast(1)
                    textValue+="+"
                }else if(textValue == ""){
                    //do nothing
                }
                else{
                    textValue += "+"
                }

            },
                colors = ButtonDefaults.buttonColors(Color(1f, 0.7f, 0.12f, 1f), Color.White),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()

            ){
                Text(text = "+",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
        }
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(10.dp))

//        Row 5
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(01f)
        ){
            Button(onClick = {
                textValue += "."
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()

            ){
                Text(text = ".",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())
            Button(onClick = {
                textValue += "0"
                resultValue = Model().result(textValue)


            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .fillMaxHeight()

            ){
                Text(text = "0",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(10.dp)
                    .fillMaxHeight())

            Button(onClick = {
                if(resultValue.isNotBlank()) {
                    resultValue = Model().result(textValue)
                    onEvent(HistoryEvent.SetAnswer(resultValue))
                    onEvent(HistoryEvent.SetEquation(textValue))
                    onEvent(HistoryEvent.SaveHistory)
                    textValue = resultValue
                    resultValue = ""
                }
            },
                colors = ButtonDefaults.buttonColors(Color(1f, 0.7f, 0.12f, 1f), Color.White),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()

            ){
                Text(text = "=",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
        }
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(10.dp))
    }
}

@Composable
fun Greeting2(modifier: Modifier) {
    val charSequence : CharSequence = "+/*-"
    var textValue by remember { mutableStateOf("") }
    var resultValue by remember{ mutableStateOf("")}
    Column(
        modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(8.dp)
//            .padding(top = 30.dp)
    ) {
        Text(
            text = textValue,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            style = TextStyle(fontSize = 30.sp, textAlign = Right, color = Color.White),
            maxLines = 2
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = resultValue,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            style = TextStyle(fontSize = 20.sp, textAlign = Right),
            color = Color.Gray
        )
        Spacer(Modifier.height(5.dp))
        Divider()
        Spacer(Modifier.height(5.dp))

//Row 1
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
        ){
            Button(onClick = { },
                colors = ButtonDefaults.buttonColors(Color.Gray, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.20f)
                    .fillMaxHeight()

            ){
                Icon(painter = painterResource(id = R.drawable.ic_history), contentDescription = "See history",
                    modifier = Modifier.fillMaxSize())
            }
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                textValue += "7"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()

            ){
                Text(text = "7",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())
            Button(onClick = {
                textValue += "8"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .fillMaxHeight()

            ){
                Text(text = "8",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                textValue += "9"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()

            ){
                Text(text = "9",
                    style = TextStyle(fontSize = 20.sp)
                )
            }

            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                if(charSequence.any{ textValue.endsWith(it, true) }){
                    textValue = textValue.dropLast(1)
                    textValue+="/"
                }else if(textValue == ""){
                    //do nothing
                }
                else{
                    textValue += "/"
                }
            },
                colors = ButtonDefaults.buttonColors(Color(1f, 0.7f, 0.12f, 1f), Color.White),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()

            ){
                Text(text = "/",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
        }
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(5.dp))

//        Row 2
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.33f)
        ){
            Button(onClick = {
                textValue = ""
                resultValue = ""
            },
                colors = ButtonDefaults.buttonColors(Color.Gray, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.20f)
                    .fillMaxHeight()

            ){
                Text(text = "C",
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                textValue += "4"
                resultValue = Model().result(textValue)
            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()

            ){
                Text(text = "4",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                textValue += "5"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .fillMaxHeight()

            ){
                Text(text = "5",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                textValue += "6"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()

            ){
                Text(text = "6",
                    style = TextStyle(fontSize = 20.sp)
                )
            }

            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                if(charSequence.any{ textValue.endsWith(it, true) }){
                    textValue = textValue.dropLast(1)
                    textValue+="*"
                }else if(textValue == ""){//do nothing
                }
                else{
                    textValue += "*"
                }
            },
                colors = ButtonDefaults.buttonColors(Color(1f, 0.7f, 0.12f, 1f), Color.White),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()

            ){
                Text(text = "*",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
        }
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(5.dp))

//        Row 3
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
        ){
            Button(onClick = {
                if(textValue.length == 1){
                    resultValue = ""
                    textValue = ""
                }else if(textValue.isNotBlank()) {
                    textValue = textValue.dropLast(1)
                }
            },
                colors = ButtonDefaults.buttonColors(Color.Gray, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .fillMaxHeight()

            ){
                Icon(painter = painterResource(R.drawable.ic_delete), contentDescription = "Remove one item",
                    modifier = Modifier.fillMaxSize())
            }
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())


            Button(onClick = {
                textValue += "1"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()

            ){
                Text(text = "1",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())
            Button(onClick = {
                textValue += "2"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .fillMaxHeight()

            ){
                Text(text = "2",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                textValue += "3"
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()

            ){
                Text(text = "3",
                    style = TextStyle(fontSize = 20.sp)
                )
            }

            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                if(textValue.endsWith("*-") or textValue.endsWith("/-")){
                    //do nothing
                }
                else if(textValue.endsWith("+", true)){
                    textValue = textValue.dropLast(1)
                    textValue+="-"
                }else if(textValue.endsWith("-")){
//                    do noting
                }
                else{
                    textValue += "-"
                }

            },
                colors = ButtonDefaults.buttonColors(Color(1f, 0.7f, 0.12f, 1f), Color.White),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()

            ){
                Text(text = "-",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
        }
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(5.dp))

//        Row 4
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
        ){
            Spacer(modifier = (Modifier.fillMaxSize(0.20f)))
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())
            Button(onClick = {
                textValue += "."
                resultValue = Model().result(textValue)

            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight()

            ){
                Text(text = ".",
                    style = TextStyle(fontSize = 20.sp, baselineShift = BaselineShift.Superscript),

                    )
            }
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                textValue += "0"
                resultValue = Model().result(textValue)


            },
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.33f)
                    .fillMaxHeight()

            ){
                Text(text = "0",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                if(resultValue.isNotBlank()) {
                    resultValue = Model().result(textValue)
                    textValue = resultValue
                    resultValue = ""
                }
            },
                colors = ButtonDefaults.buttonColors(Color(1f, 0.7f, 0.12f, 1f), Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()

            ){
                Text(text = "=",
                    style = TextStyle(fontSize = 20.sp)
                )
            }

            Spacer(
                Modifier
                    .width(20.dp)
                    .fillMaxHeight())

            Button(onClick = {
                if(charSequence.any{ textValue.endsWith(it, true) }){
                    textValue = textValue.dropLast(1)
                    textValue+="+"
                }else if(textValue == ""){
                    //do nothing
                }
                else{
                    textValue += "+"
                }

            },
                colors = ButtonDefaults.buttonColors(Color(1f, 0.7f, 0.12f, 1f), Color.White),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()

            ){
                Text(text = "+",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
        }
    }
}

class InfixToPostFix {
    private fun notNumeric(ch: Char): Boolean = when (ch) {
        '+', '-', '*', '/'-> true
        else -> false
    }

    private fun operatorPrecedence(ch: Char): Int = when (ch) {
        '+', '-' -> 1
        '*', '/' -> 2
        else -> -1
    }

    fun postFixConversion(string: String): String {
        var result = ""

        val st = ArrayDeque<Char>()

        for (s in string) {

            if (!notNumeric(s)) {
                result += s
            } else {
                while (
                    !st.isEmpty()
                    && operatorPrecedence(s) <= operatorPrecedence(st.peek()!!)
                ) {
                    result += " ${st.pop()} "
                }
                st.push(s)
                result += " "
            }
        }
        result += " "
        while (!st.isEmpty()) {
            result += st.pop()!! + " "
        }
        return result.trim()
    }
}

class Model {

    private fun replaceN(string: String): String {
        val array = StringBuffer(string)

        if (array[0] == '-') {
            array.setCharAt(0, 'n')
        }

        var i = 0
        while (i < array.length) {

            if (array[i] == '-') {
                if (
                    array[i - 1] == '+' ||
                    array[i - 1] == '-' ||
                    array[i - 1] == '/' ||
                    array[i - 1] == '*'
                ) {
                    array.setCharAt(i, 'n')
                }
            }
            i++
        }
        return array.toString()
    }

    fun result(string: String): String {
        val stringN = replaceN(string)
        val postFix = InfixToPostFix().postFixConversion(stringN)

        if (postFix == "Error") {
            return postFix
        }
        return try {
            val evaluation = ArithmeticEvaluation().evaluation(postFix)
            evaluation.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "Error"
        }
    }
}

class ArithmeticEvaluation {
    private fun notOperator(ch: Char): Boolean = when (ch) {
        '+', '-', '*', '/'-> false
        else -> true
    }

    fun evaluation(string: String): Double {
        var str = ""
        val stack = ArrayDeque<Double>()
        for (ch in string) {
            if (notOperator(ch) && ch != ' ') {
                str += ch
            } else if (ch == ' ' && str != "") {
                stack.push(str.replace('n', '-').toDouble())
                str = ""
            } else if (!notOperator(ch)) {
                val val1 = stack.pop()
                val val2 = stack.pop()

                when (ch) {
                    '+' -> stack.push(val2!! + val1!!)
                    '-' -> stack.push(val2!! - val1!!)
                    '/' -> stack.push(val2!! / val1!!)
                    '*' -> stack.push(val2!! * val1!!)
                }
            }
        }
        return stack.pop()!!
    }
}

fun <T> ArrayDeque<T>.push(element: T) = addLast(element)
fun <T> ArrayDeque<T>.pop() = removeLastOrNull()
fun <T> ArrayDeque<T>.peek() = lastOrNull()


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalcAppTheme {
        Greeting2(modifier = Modifier)
    }
}