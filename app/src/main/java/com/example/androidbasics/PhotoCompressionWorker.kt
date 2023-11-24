package com.example.androidbasics

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.roundToInt

/**
 * [CoroutineWorker] yang mengompresi gambar dari URI yang diberikan dengan menggunakan algoritma JPEG.
 *
 * @param appContext Konteks aplikasi.
 * @param params Parameter pekerjaan, seperti URI konten dan ambang batas kompresi.
 */
class PhotoCompressionWorker(
    private val appContext: Context,
    private val params:WorkerParameters
) : CoroutineWorker(appContext,params){

    /**
     * Metode utama yang dilaksanakan saat pekerjaan dimulai. Ini akan mengompresi gambar dan
     * menghasilkan hasil pekerjaan yang sesuai.
     *
     * @return [Result] yang menunjukkan hasil pekerjaan, apakah berhasil atau gagal.
     */
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO){
            // Mendapatkan URI gambar dan ambang batas kompresi dari parameter input.
            val stringUri = params.inputData.getString(KEY_CONTENT_URI)
            val compressionTresholdInBytes = params.inputData.getLong(
                KEY_COMPRESSION_THRESHOLD,
                0L
            )
            val uri = Uri.parse(stringUri)

            // Membaca byte dari URI menggunakan InputStream.
            val bytes= appContext.contentResolver.openInputStream(uri)?.use{
                it.readBytes()
            } ?: return@withContext Result.failure()

            // Membuat objek bitmap dari byte yang dibaca.
            val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)

            var outputBytes: ByteArray
            var quality = 100

            // Melakukan kompresi berulang dengan mengurangi kualitas hingga batas ambang tertentu.
            do {
                val outputStream = ByteArrayOutputStream()
                outputStream.use { outputStream->
                    bitmap.compress(Bitmap.CompressFormat.JPEG,quality,outputStream)
                    outputBytes= outputStream.toByteArray()
                        quality -= (quality * 0.1).roundToInt()
                }
            }while (outputBytes.size > compressionTresholdInBytes && quality > 5)
            // Menyimpan byte hasil kompresi ke dalam file cache.
            val file = File(appContext.cacheDir, "${params.id}.jpg")
            file.writeBytes(outputBytes)

            // Memberikan hasil pekerjaan yang sukses beserta path file hasil kompresi.
            Result.success(
                workDataOf(
                    KEY_RESULT_PATH to file.absolutePath
                )
            )


        }

    }

    companion object{
        // Konstanta-konstanta untuk mengidentifikasi data input dan output.
        const val KEY_CONTENT_URI = "KEY_CONTENT_URI"
        const val KEY_COMPRESSION_THRESHOLD = "KEY_COMPRESSION_THRESHOLD"
        const val KEY_RESULT_PATH = "KEY_RESULT_PATH"
    }
}