package com.example.androidbasics

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.UUID

/**
 * [ViewModel] untuk menyimpan dan mengelola data terkait pemampatan gambar.
 */
class PhotoViewModel : ViewModel() {
    // URI gambar sebelum dikompresi.
    var uncompressedUri:Uri? by mutableStateOf(null)
        private  set

    // Bitmap gambar setelah dikompresi.
    var compressedBitmap:Bitmap? by mutableStateOf(null)
        private set

    // UUID yang merepresentasikan ID pekerjaan pemampatan gambar.
    var workId:UUID? by mutableStateOf(null)
        private set

    /**
     * Memperbarui [uncompressedUri] dengan URI gambar sebelum dikompresi.
     *
     * @param uri URI gambar sebelum dikompresi.
     */
    fun updateUncrompressUri(uri:Uri?){
        uncompressedUri = uri
    }

    /**
     * Memperbarui [compressedBitmap] dengan Bitmap gambar setelah dikompresi.
     *
     * @param bmp Bitmap gambar setelah dikompresi.
     */
    fun updateCompreesedBitmap(bmp:Bitmap?){
        compressedBitmap = bmp
    }

    /**
     * Memperbarui [workId] dengan UUID yang merepresentasikan ID pekerjaan pemampatan gambar.
     *
     * @param uuid UUID yang merepresentasikan ID pekerjaan pemampatan gambar.
     */
    fun updateWorkId(uuid:UUID?){
        workId = uuid
    }
}