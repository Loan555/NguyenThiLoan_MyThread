package com.loan555.mythread

import android.util.Log

class ThreadSub(var sharedData: SharedData) : Runnable {
    override fun run() {
        for (i in 1..5) {
            sharedData.sub()
            Log.d("aaa", "${Thread.currentThread().name} is running! ${sharedData.count}")
            Thread.sleep(1000)
        }
    }
}