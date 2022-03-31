package com.jm.coroutineex

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.measureTimedValue

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val formatter = DateTimeFormatter.ISO_LOCAL_TIME

        val job = CoroutineScope(Dispatchers.Default).async{
            val as1 = async{
                Log.e("Jaemu>>", "as1 start : "+formatter.format(LocalDateTime.now()))
                delay(500)
                Log.e("Jaemu>>", "as1 end : "+formatter.format(LocalDateTime.now()))
                200
            }

            val as2 = async{
                Log.e("Jaemu>>", "as2 start : "+formatter.format(LocalDateTime.now()))
                delay(1000)
                Log.e("Jaemu>>", "as2 end : "+formatter.format(LocalDateTime.now()))
                300
            }

            Log.e("Jaemu>>","${as1.await()} + ${as2.await()}")
        }

        button.setOnClickListener {
            job.cancel()
        }
    }
}