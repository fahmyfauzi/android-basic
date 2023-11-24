package com.example.androidbasics

import android.app.Instrumentation.ActivityResult
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.net.toUri
import com.example.androidbasics.ui.theme.AndroidBasicsTheme
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Membuat URI dari drawable resource di dalam aplikasi Android
        val uri = Uri.parse("android.resource://$packageName/drawable/myimage")

        // Membaca byte data dari InputStream menggunakan URI
        val myimageByte = contentResolver.openInputStream(uri)?.use {
            it.readBytes()
        }

        // Menampilkan ukuran file gambar dalam logcat
        println("MyImage size: ${myimageByte?.size}")

        // Menyimpan byte data ke file lokal (contoh: myimage.jpg)
        val file = File(filesDir,"myimage.jpg")
        FileOutputStream(file).use {
            it.write(myimageByte)
        }
        // Menampilkan URI file lokal dalam logcat
        println(file.toUri())

        setContent {
            AndroidBasicsTheme {
                // Membuat launcher untuk memilih gambar dari galeri
                val pickImage = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = {contentUri->
                        // Menampilkan URI dari gambar yang dipilih dari galeri dalam logcat
                        println(contentUri)
                    }
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        pickImage.launch("image/*")
                    },

                        ) {
                        Text(text = "Pick Image")
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