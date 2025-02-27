package com.example.services

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class MyIntentSecondService: IntentService(NAME) {

    // жц сервисов
    override fun onCreate() { // создаётся
        super.onCreate()
        log("onCreate")
        setIntentRedelivery(true) // START STICKY, intent будет сохранен
    }

    override fun onHandleIntent(p0: Intent?) {
        log("onHandleIntent")
        val page = intent?.getIntExtra(PAGE, 0) ?: 0
        for (i in 0 until 3) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }
    } // Выполняется не в главном потоке

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    } // умирает

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyIntentService: $message")
    }

    companion object {
        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentSecondService::class.java).apply {
                putExtra(PAGE, page)
            }
        }

        private const val NAME = "MyIntentService"
        private const val PAGE = "page"
    }

}