package com.github.jeremyrempel.strictmodetest

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.util.Log
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
                            var ignoreReason = ""

                            violation.stackTrace.forEach { st ->
                                if (!isIgnore) {
                                    val className = st.className
                                    val clazz = Class.forName(className)
                                    clazz
                                        .methods
                                        .filter { it.name == st.methodName }
                                        .forEach { m ->
                                            val annotations =
                                                m.getDeclaredAnnotationsByType(SuppressStrictMode::class.java)
                                            if (annotations.isNotEmpty()) {
                                                isIgnore = true
                                                ignoreReason = annotations[0].reason
                                            }
                                        }
                                }
                            }

                            if (isIgnore) {
                                Log.w(
                                    "jeremy",
                                    "ignoring strict mode violation, reason: $ignoreReason"
                                )
                            } else {
                                // crash
                                Log.e("jeremy", violation.message)
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