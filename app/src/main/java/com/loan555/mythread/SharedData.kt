package com.loan555.mythread

import java.util.logging.Handler

class SharedData(var count: Int, var lastEvent: Int, var mHandler: Handler) {
    fun add() {
        count++
    }

    fun sub() {
        count--
    }
}