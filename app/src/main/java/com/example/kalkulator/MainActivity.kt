package com.example.kalkulator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kalkulator.ui.theme.KalkulatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KalkulatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KalkulatorUI()
                }
            }
        }
    }
}

@Composable
fun KalkulatorUI() {
    val context = LocalContext.current
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }
    var lastOperation by remember { mutableStateOf("") }

    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer

    val addButtonColor = Color(0xFF4CAF50)
    val subtractButtonColor = Color(0xFFFFA726)
    val multiplyButtonColor = Color(0xFFE91E63)
    val divideButtonColor = Color(0xFF2196F3)

    fun validateAndCalculate(
        operation: String,
        isDivision: Boolean = false,
        calculate: (Double, Double) -> Double
    ) {
        if (number1.isBlank() || number2.isBlank()) {
            Toast.makeText(context, "Wprowadź obie liczby", Toast.LENGTH_SHORT).show()
            return
        }

        val num1 = number1.toDoubleOrNull()
        val num2 = number2.toDoubleOrNull()

        if (num1 == null || num2 == null) {
            Toast.makeText(context, "Niepoprawne liczby", Toast.LENGTH_SHORT).show()
            return
        }

        if (isDivision && num2 == 0.0) {
            Toast.makeText(context, "Nie można dzielić przez 0", Toast.LENGTH_SHORT).show()
            return
        }

        lastOperation = operation
        val calculatedResult = calculate(num1, num2)

        result = if (calculatedResult % 1 == 0.0) {
            calculatedResult.toInt().toString()
        } else {
            calculatedResult.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Kalkulator",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(primaryContainer)
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = result ?: "0",
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                color = onPrimaryContainer,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = number1,
            onValueChange = { number1 = it },
            label = { Text("Pierwsza liczba") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = number2,
            onValueChange = { number2 = it },
            label = { Text("Druga liczba") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            OperationButton(
                symbol = "+",
                color = addButtonColor,
                onClick = { validateAndCalculate("Dodawanie") { a, b -> a + b } }
            )

            OperationButton(
                symbol = "-",
                color = subtractButtonColor,
                onClick = { validateAndCalculate("Odejmowanie") { a, b -> a - b } }
            )

            OperationButton(
                symbol = "×",
                color = multiplyButtonColor,
                onClick = { validateAndCalculate("Mnożenie") { a, b -> a * b } }
            )

            OperationButton(
                symbol = "÷",
                color = divideButtonColor,
                onClick = { validateAndCalculate("Dzielenie", isDivision = true) { a, b -> a / b } }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun OperationButton(symbol: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(
            text = symbol,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}