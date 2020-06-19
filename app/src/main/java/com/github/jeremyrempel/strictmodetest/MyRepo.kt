package com.github.jeremyrempel.strictmodetest

import android.content.Context
import java.io.OutputStreamWriter

class MyRepo {

    fun strictModeViolation(context: Context) {
        val outputStreamWriter =
            OutputStreamWriter(context.openFileOutput("myfile", Context.MODE_PRIVATE))
        outputStreamWriter.write("hello world")
        outputStreamWriter.close()
    }

    @IgnoreStrictMode("this is my desc")
    fun strictModeViolationIgnore(context: Context) {
        val outputStreamWriter =
            OutputStreamWriter(context.openFileOutput("myfile", Context.MODE_PRIVATE))
        outputStreamWriter.write("hello world")
        outputStreamWriter.close()
    }
}