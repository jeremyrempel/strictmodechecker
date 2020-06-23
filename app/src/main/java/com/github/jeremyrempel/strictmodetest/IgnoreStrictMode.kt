package com.github.jeremyrempel.strictmodetest

import android.os.strictmode.Violation
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
annotation class SuppressStrictMode(val reason: String, val input: Array<KClass<out Violation>>)