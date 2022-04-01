package com.jm.coroutineex

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class MainViewModel(application : Application) : AndroidViewModel(application) {
    var liveText = MutableLiveData("")

    init {
        CoroutineScope(Dispatchers.Main).launch {
            performSlowTaskAsync()
        }
    }

    fun changeText() {
        CoroutineScope(Dispatchers.Main).launch {
            val addText = withContext(Dispatchers.Default) {
                Log.e("Jaemu>>", "changeText start")
                delay(5000)
                Log.e("Jaemu>>", "changeText end")
                StringBuffer("add! ")
            }
            liveText.value = liveText.value.plus(addText.toString())
        }
    }

    private suspend fun performSlowTaskAsync() {
        withContext(Dispatchers.Main) {
            Log.e("Jaemu>>", "perfoemSlowTask start")
            delay(5000)
            Log.e("Jaemu>>", "perfoemSlowTask end")
            liveText.value = liveText.value.plus("finish!!")
        }
    }
}