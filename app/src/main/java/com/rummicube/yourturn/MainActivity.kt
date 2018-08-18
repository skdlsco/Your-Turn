package com.rummicube.yourturn

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_create.setOnClickListener {
            startActivity(Intent(this@MainActivity, Create1Activity::class.java))
        }
        button_join.setOnClickListener {
            startActivity(Intent(this@MainActivity, JoinActivity::class.java))
        }
    }
}
