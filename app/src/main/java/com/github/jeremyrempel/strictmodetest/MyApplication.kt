package com.github.jeremyrempel.strictmodetest

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.os.strictmode.Violation
import android.util.Log
import java.lang.RuntimeException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor


class MyApplication : Application() {
    override fun onCreate() {

        val executor = Executors.newSingleThreadExecutor()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyListener(executor,
                        StrictMode.OnThreadViolationListener { violation ->

                            var isIgnore = false

                            violation.stackTrace.forEach {
                                if (!isIgnore) {
                                    val className = it.className
                                    val methodName = it.methodName

                                    val clazz = Class.forName(className)

                                    val methods = clazz.methods.filter { m -> m.name == methodName }

                                    isIgnore = methods.filter { m ->
                                        m.isAnnotationPresent(IgnoreStrictMode::class.java)
                                    }.isNotEmpty()
                                }
                            }

                            if (isIgnore) {
                                println("ignoring strict mode violation")
                            } else {
                                // crash
                                violation.printStackTrace()
                                throw RuntimeException("strict mode violation, add to ignore or resolve")
                            }
                        })
                    .build()
            )
        }

        super.onCreate()
    }
}