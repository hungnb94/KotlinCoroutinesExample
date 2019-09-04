package com.hung.kotlincoroutinesexample

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object Example {

    @JvmStatic
    fun main(args: Array<String>) {
        Dispatchers.Default // Tinh toan, JsonParse, Sort List
        Dispatchers.IO //Input output
        Dispatchers.Main //UI thread
        Dispatchers.Unconfined //

        GlobalScope.launch(Dispatchers.Main) {
            println(Thread.currentThread().name)
            processA()
            println(Thread.currentThread().name)
            processB()
        }

        Thread.sleep(2000)
    }




    suspend fun processA() {
        //Long running task 10s
        println("Process A")
        for (i in 0 until 3) {
            println(i)
            delay(100)
        }
    }

    fun processB() {
        println("Process B")
    }
}