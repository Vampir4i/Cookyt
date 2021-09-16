package com.example.cookyt

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.room.Room
import com.example.cookyt.room.AppDatabase
import com.squareup.picasso.Picasso

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database").build()
    }

    companion object {
        val TAG = "myLog"
        lateinit var context: Context
        lateinit var database: AppDatabase

        fun makeToast(msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        fun makeLog(msg: String) {
            Log.d(TAG, msg)
        }
        fun loadPhoto(src: String?, iv: ImageView?) {
            if(src.isNullOrEmpty()) return
            Picasso.get().load(src)
                .fit().centerCrop().into(iv)
        }
    }
}