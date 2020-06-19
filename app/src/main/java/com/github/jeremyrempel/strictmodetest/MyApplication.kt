package com.github.jeremyrempel.strictmodetest

import android.app.Application
import android.os.Build
import android.os.StrictMode
import java.util.concurrent.Executors


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
                                    isIgnore = methods.any { m -> m.isAnnotationPresent(IgnoreStrictMode::class.java) }
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
        } else {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }

        super.onCreate()
    }
}