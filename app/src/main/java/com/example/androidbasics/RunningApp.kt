package com.example.androidbasics

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/**
 * Kelas [Application] kustom untuk menginisialisasi komponen aplikasi saat pertama kali dibuat.
 */
class RunningApp: Application() {

    /**
     * Metode yang dipanggil saat aplikasi pertama kali dibuat.
     */
    override fun onCreate() {
        super.onCreate()
        // Memeriksa apakah versi SDK Android saat ini lebih besar atau sama dengan Oreo (versi 26).
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Jika ya, membuat saluran notifikasi untuk pengelolaan notifikasi di Android Oreo ke atas.
            val channel = NotificationChannel(
                "running_channel", // ID saluran notifikasi
                "Running Notification", // Nama saluran notifikasi
                NotificationManager.IMPORTANCE_HIGH // Tingkat kepentingan notifikasi
            )
            // Mendapatkan instance NotificationManager dari sistem.
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Membuat saluran notifikasi.
            notificationManager.createNotificationChannel(channel)
        }
    }
}