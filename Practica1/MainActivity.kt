package com.example.practica1

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practica1.ui.component.NewMenu
import com.example.practica1.ui.theme.Practica1Theme
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practica1Theme {
                var currentScreen by remember { mutableStateOf("menu") }

                // Levantar el estado para UISuma
                var cadTxtOp1 by remember { mutableStateOf("") }
                var cadTxtOp2 by remember { mutableStateOf("") }
                var cadTxtOp3 by remember { mutableStateOf("") }
                var cadTxtRes by remember { mutableStateOf("") }

                val activity = (LocalContext.current as? Activity)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text(text = "Menu") }, actions = {
                            var isMenuOpened by remember { mutableStateOf(false) }

                            IconButton(onClick = { isMenuOpened = true }) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = "Opciones de menú"
                                )
                            }

                            NewMenu(
                                isExpanded = isMenuOpened,
                                onItemClick = { option ->
                                    when (option) {
                                        "Suma" -> currentScreen = "suma"
                                        "Nombre" -> currentScreen = "nombre"
                                        "Fecha" -> currentScreen = "fecha"
                                        "Salir" -> activity?.finish()
                                    }
                                },
                                onDismiss = { isMenuOpened = false }
                            )
                        })
                    }
                ) { innerPadding ->
                    when (currentScreen) {
                        "suma" -> UISuma(
                            cadTxtOp1, { cadTxtOp1 = it },
                            cadTxtOp2, { cadTxtOp2 = it },
                            cadTxtOp3, { cadTxtOp3 = it },
                            cadTxtRes, { cadTxtRes = it }
                        )
                        "nombre" -> UINombre()

                        "fecha" -> UITiempo()
                    }
                }
            }
        }
    }
}

@Composable
fun UITiempo() {
    var fechaNacimiento by remember { mutableStateOf("") }
    var resultadoTiempo by remember { mutableStateOf("") }

    fun calcularTiempo() {
        try {
            val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val fechaNac = LocalDate.parse(fechaNacimiento, formato)
            val fechaActual = LocalDate.now()

            val periodo = Period.between(fechaNac, fechaActual)
            val diasTotales = ChronoUnit.DAYS.between(fechaNac, fechaActual)
            val horasTotales = diasTotales * 24
            val minutosTotales = horasTotales * 60
            val segundosTotales = minutosTotales * 60

            resultadoTiempo = """
                🗓 Has vivido:
                - ${periodo.years * 12 + periodo.months} meses
                - ${diasTotales / 7} semanas
                - $diasTotales días
                - $horasTotales horas
                - $minutosTotales minutos
                - $segundosTotales segundos
            """.trimIndent()
        } catch (e: Exception) {
            resultadoTiempo = "⚠️ Formato incorrecto. Usa: dd/MM/yyyy"
        }
    }

    Column(
        Modifier.fillMaxSize().padding(50.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ingrese su fecha de nacimiento (dd/MM/yyyy)")

        TextField(
            value = fechaNacimiento,
            onValueChange = { fechaNacimiento = it },
            placeholder = { Text("Ejemplo: 15/08/2000") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { calcularTiempo() }) {
            Text(text = "Calcular tiempo vivido")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = resultadoTiempo, textAlign = TextAlign.Center)
    }
}

@Composable
fun UINombre() {
    var cadNombre by remember { mutableStateOf("") }
    var cadApellidoPaterno by remember { mutableStateOf("") }
    var cadApellidoMaterno by remember { mutableStateOf("")}

    var cadFullName by remember { mutableStateOf("") }

    fun btn_Full_Name() {
        cadFullName = "$cadNombre " + "$cadApellidoPaterno" + " $cadApellidoMaterno"
    }

    Column(Modifier.fillMaxSize().padding(top = 100.dp).padding(50.dp), Arrangement.Top, Alignment.CenterHorizontally) {

        Row(Modifier.fillMaxWidth()) {
            TextField(value = cadNombre, onValueChange = {cadNombre = it},
                placeholder = { Text("Nombre")})
        }
        Row(Modifier.fillMaxWidth()) {
            TextField(value = cadApellidoPaterno, onValueChange = {cadApellidoPaterno = it},
                placeholder = { Text("Apellido Paterno")})
        }
        Row(Modifier.fillMaxWidth()) {
            TextField(value = cadApellidoMaterno , onValueChange = {cadApellidoMaterno = it},
                placeholder = { Text("Apellido Materno")})
        }
        Row(Modifier.fillMaxWidth()) {
            TextField(value = cadFullName, onValueChange = {cadFullName = it},
                placeholder = { Text("Tu nombre completo es:")})
        }

        Row(Modifier.fillMaxWidth().padding(20.dp)) {
            Button(onClick = {btn_Full_Name()} , Modifier.weight(1f)) {
                Text(text = "Nombre completo")
            }
        }


    }
}

@Composable
fun UISuma(
    cadTxtOp1: String, onOp1Change: (String) -> Unit,
    cadTxtOp2: String, onOp2Change: (String) -> Unit,
    cadTxtOp3: String, onOp3Change: (String) -> Unit,
    cadTxtRes: String, onResChange: (String) -> Unit
) {
    fun btn_limpiar_click() {
        onOp1Change("")
        onOp2Change("")
        onOp3Change("")
        onResChange("")
    }

    fun btn_sumar_click() {
        val op1 = cadTxtOp1.toIntOrNull() ?: 0
        val op2 = cadTxtOp2.toIntOrNull() ?: 0
        val op3 = cadTxtOp3.toIntOrNull() ?: 0
        onResChange((op1 + op2 + op3).toString())
    }

    Column(
        Modifier.fillMaxSize().padding(top = 90.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Text("OP1", Modifier.weight(1f))
            Text("OP2", Modifier.weight(1f))
            Text("OP3", Modifier.weight(1f))
            Text("RES", Modifier.weight(1f))
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            TextField(value = cadTxtOp1, onValueChange = onOp1Change, Modifier.weight(1f))
            Text(text = "+", Modifier.padding(1.dp))
            TextField(value = cadTxtOp2, onValueChange = onOp2Change, Modifier.weight(1f))
            Text(text = "+", Modifier.padding(1.dp))
            TextField(value = cadTxtOp3, onValueChange = onOp3Change, Modifier.weight(1f))
            Text(text = "=", Modifier.padding(1.dp))
            TextField(value = cadTxtRes, onValueChange = {}, Modifier.weight(1f), readOnly = true)
        }

        Row(Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = { btn_sumar_click() }, Modifier.weight(1f)) {
                Text(text = "Sumar")
            }

            Button(onClick = { btn_limpiar_click() }, Modifier.weight(1f)) {
                Text(text = "Limpiar")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Practica1Theme {
        Greeting("Android")
    }
}