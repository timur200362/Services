package com.example.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService: JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // жц сервисов
    override fun onCreate() { // создаётся
        super.onCreate()
        log("onCreate")
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        log("onStartJob")
        coroutineScope.launch {
            var workItem = params?.dequeueWork() // из очереди будет взят сервис
            while (workItem != null) { // пока в очереди есть объекты
                val page = workItem.intent.getIntExtra(PAGE, 0)

                for (i in 0 until 5) {
                    delay(1000)
                    log("Timer $i $page")
                }
                params?.completeWork(workItem) // завершение сервиса из очереди
                workItem = params?.dequeueWork()
            }
            jobFinished(params, false) // завершение работы всего сервиса
            // но вроде система сама завершит работу всего сервиса после завершения работы последнего сервиса из очереди
        }

        return true // сервис выполняется, сами говорим когда нужно завершить работу
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        log("onStopJob")
        return true // перезапуск сервиса после его остановки
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    } // умирает

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyJobService: $message")
    }

    companion object {
        const val JOB_ID = 111
        private const val PAGE = "page"

        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }
}
