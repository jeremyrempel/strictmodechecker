package com.github.jeremyrempel.strictmodetest

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_ignorev).setOnClickListener {
            MyRepo().strictModeViolationIgnore(applicationContext)
        }

        findViewById<Button>(R.id.btn_crashv).setOnClickListener {
            MyRepo().strictModeViolation(applicationContext)
        }
    }
}