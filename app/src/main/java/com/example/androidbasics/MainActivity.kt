package com.example.androidbasics

import android.content.Intent
import android.content.Intent.EXTRA_STREAM
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.AsyncImage
import com.example.androidbasics.ui.theme.AndroidBasicsTheme

/**
 * Kelas utama (MainActivity) yang merupakan komponen aktivitas dalam aplikasi.
 */
class MainActivity : ComponentActivity() {

    // Instance dari WorkManager untuk mengelola pekerjaan (jobs).
    private lateinit var workManager:WorkManager

    // Instance dari ViewModel yang digunakan untuk menyimpan dan mengelola data pemampatan gambar.
    private val viewModel by viewModels<PhotoViewModel>()

    /**
     * Metode yang dipanggil ketika aktivitas dibuat.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi instance WorkManager.
        workManager = WorkManager.getInstance(applicationContext)

        setContent {
            AndroidBasicsTheme {
                // Mendapatkan status pekerjaan dari ViewModel.
                 val workResult = viewModel.workId?.let { id->
                     workManager.getWorkInfoByIdLiveData(id).observeAsState().value
                 }
                // Melakukan efek terbang (LaunchedEffect) ketika ada perubahan pada outputData pekerjaan.
                LaunchedEffect(key1 = workResult?.outputData){
                    if(workResult?.outputData !== null){
                        // Mendapatkan jalur file hasil kompresi dari outputData.
                        val filePath = workResult.outputData.getString(
                            PhotoCompressionWorker.KEY_RESULT_PATH
                        )
                        filePath?.let {
                            // Membaca file hasil kompresi menjadi Bitmap.
                            val bitmap = BitmapFactory.decodeFile(it)
                            // Memperbarui ViewModel dengan Bitmap hasil kompresi.
                            viewModel.updateCompreesedBitmap(bitmap)
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Menampilkan foto sebelum dikompresi jika ada.
                    viewModel.uncompressedUri?.let{
                        Text(text = "Uncompressed photo:")
                        AsyncImage(model = it, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                    // Menampilkan foto setelah dikompresi jika ada.
                    viewModel.compressedBitmap?.let {
                        Text(text = "Compressed photo:")
                        Image(bitmap = it.asImageBitmap() , contentDescription = null)
                    }

                }

            }
        }
    }

    /**
     * Metode yang dipanggil ketika ada intent baru, seperti ketika aplikasi dimulai melalui Share menu.
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Mendapatkan URI gambar dari intent.
        val uri = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent?.getParcelableExtra(Intent.EXTRA_STREAM,Uri::class.java)
        }else{
          intent?.getParcelableExtra(Intent.EXTRA_STREAM)
        } ?: return
        // Memperbarui ViewModel dengan URI gambar sebelum dikompresi.
        viewModel.updateUncrompressUri(uri)

        // Membuat pekerjaan OneTimeWorkRequest untuk melakukan kompresi gambar.
        val request = OneTimeWorkRequestBuilder<PhotoCompressionWorker>().setInputData(
            workDataOf(
                PhotoCompressionWorker.KEY_CONTENT_URI to uri.toString(),
                PhotoCompressionWorker.KEY_COMPRESSION_THRESHOLD to 1024 * 20L
            )
        )
            .setConstraints(Constraints(
                requiresStorageNotLow = true
            ))
            .build()
        // Memperbarui ViewModel dengan ID pekerjaan yang telah dibuat.
        viewModel.updateWorkId(request.id)

        // Menjalankan pekerjaan menggunakan WorkManager.
        workManager.enqueue(request  )
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