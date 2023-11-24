package com.example.androidbasics

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.androidbasics.ui.theme.AndroidBasicsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Memeriksa apakah versi SDK Android saat ini lebih besar atau sama dengan Tiramisu (versi 30).
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            // Jika ya, meminta izin kepada pengguna untuk menggunakan fitur POST_NOTIFICATIONS.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(POST_NOTIFICATIONS),
                0
            )
        }
        setContent {
            AndroidBasicsTheme {
               Column(
                   modifier = Modifier.fillMaxSize(),
                   horizontalAlignment = Alignment.CenterHorizontally,
                   verticalArrangement = Arrangement.Center
               ) {
                   Button(onClick = {
                       // Membuat Intent untuk memulai RunningService.
                       Intent(applicationContext,RunningService::class.java).also {

                           // Menetapkan aksi yang diinginkan ke Intent (memulai layanan)
                           it.action =RunningService.Action.START.toString()

                           // Memulai layanan menggunakan Intent yang telah dikonfigurasi.
                           startService(it)
                       }

                   }) {
                        Text(text = "Start run")
                   }
                   Button(onClick = {
                       Intent(applicationContext,RunningService::class.java).also {
                           it.action =RunningService.Action.STOP.toString()
                           startService(it)
                       }

                   }) {
                       Text(text = "Stop run")
                   }
               }
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
    AndroidBasicsTheme {
        Greeting("Android")
    }
}