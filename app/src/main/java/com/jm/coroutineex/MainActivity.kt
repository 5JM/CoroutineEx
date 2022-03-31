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

        // launch , join
//        val job_l = CoroutineScope(Dispatchers.Default).launch{
//            launch{
//                for (i in 0..5){
//                    delay(500)
//                    Log.e("Jaemu>>","코루틴 $i")
//                }
//            }.join() // 1. join 선언시 해당 launch블록이 먼저 실행
//
//            launch{
//                for (i in 6..10){
//                    delay(500)
//                    Log.e("Jaemu>>","코루틴 $i")
//                }
//            }
//        }

        //  async , await
//        val job_a = CoroutineScope(Dispatchers.Default).async{
//            val as1 = async{
//                Log.e("Jaemu>>", "as1 start : "+formatter.format(LocalDateTime.now()))
//                delay(500)
//                Log.e("Jaemu>>", "as1 end : "+formatter.format(LocalDateTime.now()))
//                200
//            }
//
//            val as2 = async{
//                Log.e("Jaemu>>", "as2 start : "+formatter.format(LocalDateTime.now()))
//                delay(1000)
//                Log.e("Jaemu>>", "as2 end : "+formatter.format(LocalDateTime.now()))
//                300
//            }
//
//            Log.e("Jaemu>>","${as1.await()} + ${as2.await()}")
//        }

        // suspend
        suspend fun suspendFunc(){
            for(i in 0..10){
                Log.e("Jaemu>>","$i")
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            launch{
                for (i in 11..20){
                    delay(500)
                    Log.e("Jaemu>>","코루틴 $i")
                }
            }

            suspendFunc()

            launch{
                for (i in 21..30){
                    delay(500)
                    Log.e("Jaemu>>","코루틴 $i")
                }
            }
        }


//        button.setOnClickListener {
//            job.cancel()
//        }
    }
}