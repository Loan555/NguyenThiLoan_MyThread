package com.loan555.mythread

import android.util.Log

class ThreadAdd(var sharedData: SharedData) : Runnable {
    override fun run() {
        for (i in 1..5) {
            sharedData.add()
            Log.d("aaa", "${Thread.currentThread().name} is running ${sharedData.count}")
            Thread.sleep(1000)
        }
    }
}