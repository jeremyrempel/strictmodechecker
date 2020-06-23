package com.github.jeremyrempel.strictmodetest

import android.content.Context
import android.os.strictmode.DiskReadViolation
import android.os.strictmode.DiskWriteViolation
import java.io.OutputStreamWriter

class MyRepo {

    fun strictModeViolation(context: Context) {
        val outputStreamWriter =
            OutputStreamWriter(context.openFileOutput("myfile", Context.MODE_PRIVATE))
        outputStreamWriter.write("hello world")
        outputStreamWriter.close()
    }

    @SuppressStrictMode("this is my desc", [DiskReadViolation::class, DiskWriteViolation::class])
    fun strictModeViolationIgnore(context: Context) {
        val outputStreamWriter =
            OutputStreamWriter(context.openFileOutput("myfile", Context.MODE_PRIVATE))
        outputStreamWriter.write("hello world")
        outputStreamWriter.close()
    }
}