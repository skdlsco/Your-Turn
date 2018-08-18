package com.rummicube.yourturn

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create2.*


class Create2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create2)

        val roomId = intent.getStringExtra("room")
        code_text.text = roomId
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        code_text.setOnLongClickListener {
            clipboardManager.primaryClip = ClipData.newPlainText("code", code_text.text.toString())
            Toast.makeText(this@Create2Activity, "복사됬습니다", Toast.LENGTH_SHORT).show()
            true
        }
        button.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, roomId)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, "코드 공유하기"))
        }
        close_button.setOnClickListener {
            finish()
        }
    }
}
