package com.rummicube.yourturn

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_push.*
import java.util.*

class PushActivity : AppCompatActivity() {

    val db = FirebaseDatabase.getInstance()
    lateinit var pref: DatabaseReference
    var isFull = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push)

        val roomId = intent.getStringExtra("room")
        val id = UUID.randomUUID().toString()
        pref = db.getReference(roomId)
        pref.child("users").child(id).setValue(0)
        pref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(data: DataSnapshot) {
                val count = data.child("count").value.toString().toInt()
                val max = data.child("max").value.toString().toInt()
                if (count == max) {
                    pref.removeEventListener(this)
                    startCount()
                }
            }
        })
        close_button.setOnClickListener {
            finish()
        }
        button.setOnClickListener {
            if (!isFull)
                return@setOnClickListener
            pref.child("users").child(id).setValue(ServerValue.TIMESTAMP)
            startActivity(Intent(this@PushActivity, ResultActivity::class.java).apply {
                putExtra("id", id)
                putExtra("room", roomId)
            })
            finish()
        }
    }

    fun startCount() {
        (1..5).forEach {
            Handler().postDelayed({
                time_text.text = "00:0" + (5 - it)
                time_text.invalidate()
                if (it == 5) {
                    isFull = true
                    button.setImageResource(R.drawable.able_button)
                }
            }, (it * 1000).toLong())
        }
    }
}
