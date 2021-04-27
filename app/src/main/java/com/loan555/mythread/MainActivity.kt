package com.loan555.mythread

import android.graphics.Color
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var count: Int = 0
    var lastEvent: Int = 0
    var maxThreadResetCount: Int = 0
    var dem: Int = 0

    //0 : ko cos sk nao
    // 1: tang 1
    //-1: giam 1
    //2: tang nhieu
    //-2 giam nhieu
    // -3 stop
    lateinit var mHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("aaa", "start")

        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1002 -> {
                        text.setText(msg.arg1.toString())
                        if (msg.arg1 % 100 == 0) {
                            var color: Int =
                                Color.rgb((0..255).random(), (0..255).random(), (0..255).random())
                            text.setTextColor(color)
                        }
                    }
                    1001 -> {
                        when (msg.arg1) {
                            0 -> {
                                Log.d("aaa", "lastState vua thay doi ne")
//                                doResetCount()
                            }
                        }
                    }
                }
            }
        }

        btIncrease.setOnClickListener {
            Log.d("aaa", "bt increase click! ${btIncrease.isPressed}")
            dem++
            lastEvent = 1
            doIncreaseOne()
        }

        btIncrease.setOnLongClickListener {
            Log.d("aaa", "tang nhieu click!")
            lastEvent = 2
            doIncreaseMore()
            true
        }

        btIncrease.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastEvent = -3
                    Log.d("aaa", "nhan")
                }

                MotionEvent.ACTION_UP -> {
                    lastEvent = 0
                    doResetCount()
                    Log.d("aaa", "tha ${btIncrease.isPressed}")
                }
            }
            false
        }

        btReduce.setOnClickListener {
            Log.d("aaa", "giam 1 btIncreas = ${btIncrease.isPressed}")
            lastEvent = -1
            doReduceOne()
        }

        btReduce.setOnLongClickListener {
            Log.d("aaa", "giam nhieu click!")
            lastEvent = -2
            doReduceMore()
            true
        }

        btReduce.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastEvent = -3
                    Log.d("aaa", "tha")
                }
                MotionEvent.ACTION_UP -> {
                    lastEvent = 0
                    var message = Message()
                    message.what = 1001
                    message.arg1 = lastEvent
                    mHandler.sendMessage(message)
                    doResetCount()
                    Log.d("aaa", "tha")
                }
            }
            false
        }

        var dY: Float = 0.0f
        text.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dY = event.getRawY()
                        Log.d("aaa", "down  $dY ")
                        lastEvent = -3
                    }
                    MotionEvent.ACTION_MOVE -> {
                        var y = event.getRawY()
                        if (y > dY) {
                            lastEvent = -1
                            Log.d("aaa", "move  $y /$dY giam")
                            doReduceOneOne()
                        } else if (y < dY) {
                            lastEvent = 1
                            Log.d("aaa", "move  $y tang")
                            doIncreaseOneOne()
                        }
                        dY = y
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.d("aaa", "up ")
                        lastEvent = 0
                        var threadResetOne = Thread(Runnable {
                            maxThreadResetCount++
                            var idReset = maxThreadResetCount
                            Thread.sleep(1000)
                            while (lastEvent == 0 && count != 0 && idReset == maxThreadResetCount) {
                                if (count < 0) count++
                                else count--
                                var message = Message()
                                message.what = 1002
                                message.arg1 = count
                                mHandler.sendMessage(message)
                                Log.d(
                                    "aaa",
                                    "SumThread: ${Thread.activeCount()} -${Thread.currentThread().name}--count = $count"
                                )
                                Thread.sleep(50)
                            }
                        })
                        threadResetOne.start()
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    //------------------------------funtion
    fun doIncreaseOne() {
        if (lastEvent == 1) {
            var threadAddOne = Thread(Runnable {
                count++
                var message = Message()
                message.what = 1002
                message.arg1 = count
                mHandler.sendMessage(message)
                Log.d(
                    "aaa", "SumThread: ${Thread.activeCount()} ----count = $count"
                )
            })
            threadAddOne.start()
            threadAddOne.join()
            var threadResetOne = Thread(Runnable {
                maxThreadResetCount++
                var idReset = maxThreadResetCount
                Thread.sleep(2000)
                while (lastEvent == 1 && count != 0 && idReset == maxThreadResetCount) {
                    if (count < 0) count++
                    else count--
                    var message = Message()
                    message.what = 1002
                    message.arg1 = count
                    mHandler.sendMessage(message)
                    Log.d(
                        "aaa",
                        "SumThread: ${Thread.activeCount()} -${Thread.currentThread().name}--count = $count"
                    )
                    Thread.sleep(50)
                }
            })
            threadResetOne.start()
        }
    }

    fun doIncreaseMore() {
        var threadAddMore = Thread(Runnable {
            while (lastEvent == 2) {
                count++
                var message = Message()
                message.what = 1002
                message.arg1 = count
                mHandler.sendMessage(message)
                Log.d(
                    "aaa", "SumThread: ${Thread.activeCount()} ----count = $count"
                )
                Thread.sleep(20)
            }
        })
        threadAddMore.start()
    }

    fun doIncreaseOneOne() {
        if (lastEvent == 1) {
            var threadAddOne = Thread(Runnable {
                count++
                var message = Message()
                message.what = 1002
                message.arg1 = count
                mHandler.sendMessage(message)
                Log.d(
                    "aaa", "SumThread: ${Thread.activeCount()} ----count = $count"
                )
            })
            threadAddOne.start()
        }
    }

    fun doReduceOneOne() {
        if (lastEvent == -1) {
            var threadAddOne = Thread(Runnable {
                count--
                var message = Message()
                message.what = 1002
                message.arg1 = count
                mHandler.sendMessage(message)
                Log.d(
                    "aaa", "SumThread: ${Thread.activeCount()} ----count = $count"
                )
            })
            threadAddOne.start()
        }
    }

    fun doReduceOne() {
        if (lastEvent == -1) {
            var threadSubOne = Thread(Runnable {
                count--
                var message = Message()
                message.what = 1002
                message.arg1 = count
                mHandler.sendMessage(message)
                Log.d(
                    "aaa", "SumThread: ${Thread.activeCount()} ----count = $count"
                )
            })
            threadSubOne.start()
            threadSubOne.join()
            var threadResetOne = Thread(Runnable {
                maxThreadResetCount++
                var idReset = maxThreadResetCount
                Thread.sleep(2000)
                while (lastEvent == -1 && count != 0 && idReset == maxThreadResetCount) {
                    if (count < 0) count++
                    else count--
                    var message = Message()
                    message.what = 1002
                    message.arg1 = count
                    mHandler.sendMessage(message)
                    Log.d(
                        "aaa",
                        "SumThread: ${Thread.activeCount()} -${Thread.currentThread().name}--count = $count"
                    )
                    Thread.sleep(50)
                }
            })
            threadResetOne.start()
        }
    }

    fun doReduceMore() {
        var threadSubMore = Thread(Runnable {
            while (lastEvent == -2) {
                count--
                var message = Message()
                message.what = 1002
                message.arg1 = count
                mHandler.sendMessage(message)
                Log.d(
                    "aaa", "SumThread: ${Thread.activeCount()} ----count = $count"
                )
                Thread.sleep(20)
            }
        })
        threadSubMore.start()
    }

    fun doResetCount() {
        if (lastEvent == 0) {
            var threadResetCount = Thread(Runnable {
                Thread.sleep(2000)
                while (lastEvent == 0 && count != 0) {
                    if (count < 0) count++
                    else count--
                    var message = Message()
                    message.what = 1002
                    message.arg1 = count
                    mHandler.sendMessage(message)
                    Log.d(
                        "aaa",
                        "SumThread: ${Thread.activeCount()} -${Thread.currentThread().name}--count = $count"
                    )
                    Thread.sleep(50)
                }
            })
            threadResetCount.name = "threadResetCount"
            threadResetCount.start()
        }
    }

    fun resetOne() {
        if (lastEvent == 1) {
            var threadResetCount = Thread(Runnable {
                Thread.sleep(2000)
                while (lastEvent == 1 && count != 0) {
                    if (count < 0) count++
                    else count--
                    var message = Message()
                    message.what = 1002
                    message.arg1 = count
                    mHandler.sendMessage(message)
                    Log.d(
                        "aaa",
                        "SumThread: ${Thread.activeCount()} -${Thread.currentThread().name}--count = $count"
                    )
                    Thread.sleep(50)
                }
                maxThreadResetCount = 0
            })
            threadResetCount.name = "threadResetCount"
            threadResetCount.start()
            maxThreadResetCount++
        }
    }
}