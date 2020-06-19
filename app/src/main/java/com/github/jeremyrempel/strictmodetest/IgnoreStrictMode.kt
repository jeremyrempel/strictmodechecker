package com.github.jeremyrempel.strictmodetest

@Target(AnnotationTarget.FUNCTION)
annotation class IgnoreStrictMode(val reason: String)