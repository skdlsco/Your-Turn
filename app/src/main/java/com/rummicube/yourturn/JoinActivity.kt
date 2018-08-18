package com.rummicube.yourturn

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {

    val db = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        close_button.setOnClickListener {
            finish()
        }

        button.setOnClickListener {
            if (code_text.text.toString().length == 5)
                joinRoom()
        }
    }

    fun joinRoom() {
        val ref = db.getReference(code_text.text.toString())
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(data: DataSnapshot) {
                ref.removeEventListener(this)
                if (data.value == null) {
                    Toast.makeText(this@JoinActivity, "방을 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
                } else {
                    val count = data.child("count").value.toString().toInt()
                    val max = data.child("max").value.toString().toInt()
                    if (count < max) {
                        data.ref.child("count").setValue(count + 1)
                        startActivity(Intent(this@JoinActivity, PushActivity::class.java).apply {
                            putExtra("room", data.key)
                        })
                        finish()
                    } else {
                        Toast.makeText(this@JoinActivity, "방이 꽉찼습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
