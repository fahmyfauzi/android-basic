package com.example.androidbasics

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class RunningService : Service() {
    //Metode yang dipanggil ketika komponen pemanggil ingin terikat dengan layanan ini.
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Metode yang dipanggil setiap kali layanan dimulai menggunakan startService().
     * @param intent Intent yang berisi informasi pemanggilan layanan.
     * @param flags Flag yang memberikan informasi tentang cara layanan dimulai.
     * @param startId ID unik untuk setiap permintaan layanan.
     * @return Kode yang menentukan perilaku layanan.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Memeriksa aksi dari intent untuk menentukan tindakan yang diambil.
        when(intent?.action){
            Action.START.toString() -> start() // Jika aksi adalah START, panggil metode start().
            Action.STOP.toString() -> stopSelf() //  Jika aksi adalah STOP, hentikan layanan.
        }
        return super.onStartCommand(intent, flags, startId)
    }

    //Memulai layanan dan menampilkan notifikasi foreground.
    private fun start(){
        // Membangun notifikasi menggunakan NotificationCompat.
        val notification = NotificationCompat.Builder(this,"running_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Run is active")
            .setContentText("Elapsed time: 00:40")
            .build()

        // Memulai layanan dalam mode foreground dengan notifikasi.
        startForeground(1,notification)
    }

    /**
     * Enumerasi yang mendefinisikan aksi yang dapat diambil oleh layanan.
     */
    enum class Action{
        START,STOP
    }
}