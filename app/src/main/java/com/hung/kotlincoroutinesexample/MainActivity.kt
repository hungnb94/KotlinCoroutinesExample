package com.hung.kotlincoroutinesexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private val TAG = "HHMainActivity"

    private var raceEnd = false
    private var greenJob: Job? = null
    private var redJob: Job? = null
    private var blueJob: Job? = null

    private val parentJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart.setOnClickListener {
            startUpdate()
        }
        buttonStop.setOnClickListener {
            coroutineContext.cancel()
        }
    }

    override fun onDestroy() {
        parentJob.cancelChildren()
        super.onDestroy()
        resetRun()
    }

    private fun startUpdate() {
        resetRun()

        greenJob = launch {
            startRunning(progressBarGreen)
        }

        redJob = launch {
            startRunning(progressBarRed)
        }

        blueJob = launch {
            startRunning(progressBarBlue)
        }
//        launch {
//            val abc = async {
//                withContext(Dispatchers.Main) {
//                    for (i in 1 until 10) {
//                        Log.w(TAG, "value: $i")
//                    }
//                }
//            }
//            abc.await()
//            Log.w(TAG, "Chay xong coroutines")
//            delay(1000)
//            val result = withContext(Dispatchers.Main) {
//                buttonStart.text = "Chay xong roi"
//                "kdkdkdkdk"
//            }
//            Log.w(TAG, "Result of withContext: $result")
//        }

        launch(Dispatchers.IO) {
            val result = mutableListOf(3 , 2, 5, 4)
            withContext(Dispatchers.Default) {
                result.sort()
            }
            withContext(Dispatchers.Main) {
                //Update UI here
                buttonStop.text = result.toString()
            }
        }
    }

    private suspend fun startRunning(progressBar: RoundCornerProgressBar) {
        progressBar.progress = 0f
        while (progressBar.progress < 1000 && !raceEnd) {
            delay(50)
            progressBar.progress += (1..10).random()
            Log.w(TAG, "${progressBar.tooltipText} + ${progressBar.progress}")
        }
        if (!raceEnd) {
            raceEnd = true
            Toast.makeText(this, "${progressBar.tooltipText} won!", Toast.LENGTH_SHORT).show()
        }
    }

    fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) + start

    private fun resetRun() {
        raceEnd = false
        greenJob?.cancel()
        blueJob?.cancel()
        redJob?.cancel()
    }
}
