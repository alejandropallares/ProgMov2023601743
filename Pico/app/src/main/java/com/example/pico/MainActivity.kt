package com.example.pico

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.media.MediaPlayer
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UIPrincipal()
        }
    }
}

@Composable
fun BotonSonido(soundRe: Int, txtName: String, isInRange: Boolean) {
    val context = LocalContext.current
    val mediaPlayer by remember { mutableStateOf(MediaPlayer.create(context, soundRe)) }
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

    // Controlar la reproducción del sonido y la vibración
    LaunchedEffect(isInRange) {
        if (isInRange) {
            // Reproducir sonido
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
            // Iniciar vibración
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }
        } else {
            // Parar sonido
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()

            }
            // Cancelar vibración
            vibrator.cancel()
        }
    }

    Box(
        modifier = Modifier
            .size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = txtName)
    }

    // Limpiar recursos
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
            vibrator.cancel()
        }
    }
}

@Composable
fun UIPrincipal() {
    val multimedios = listOf(
        Triple(
            R.drawable.pico_vertical, // Imagen original
            R.drawable.pico_diamantes, // Imagen cuando está en rango
            R.raw.minecraft_breaking_mining_stone_sound
        )
    )

    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    var isInRange by remember { mutableStateOf(false) }

    // Rangos definidos
    val xRange = -0.0f..8.5f
    val yRange = 5.0f..10.0f
    val zRange = -1.0f..2.8f

    // Listener del acelerómetro
    val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                val x = it.values[0] // Coordenada X
                val y = it.values[1] // Coordenada Y
                val z = it.values[2] // Coordenada Z

                // Verificar si las coordenadas están dentro de los rangos
                isInRange = x in xRange && y in yRange && z in zRange
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // No necesitamos manejar esto por ahora
        }
    }

    // Registrar y desregistrar el listener del sensor
    DisposableEffect(Unit) {
        sensorManager.registerListener(
            sensorListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isInRange) Color(red = 74, green = 159, blue = 55) else Color.Transparent)
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Cambiar la imagen según el estado
        Image(
            painter = painterResource(
                id = if (isInRange) multimedios[0].second else multimedios[0].first
            ),
            contentDescription = "Imagen del pico",
            modifier = Modifier
                .size(800.dp)
                .padding(bottom = 16.dp)
        )

        BotonSonido(
            soundRe = multimedios[0].third,
            txtName = "",
            isInRange = isInRange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    UIPrincipal()
}