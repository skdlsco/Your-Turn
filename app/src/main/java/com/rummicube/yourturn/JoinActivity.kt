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
import java.util.*

class JoinActivity : AppCompatActivity() {

    val db = FirebaseDatabase.getInstance()
    var clickable = true
    val id = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        close_button.setOnClickListener {
            finish()
        }

        button.setOnClickListener {
            if (code_text.text.toString().length == 5 && clickable)
                joinRoom()
        }
    }

    fun joinRoom() {
        clickable = false
        val ref = db.getReference(code_text.text.toString())
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(data: DataSnapshot) {
                ref.removeEventListener(this)
                if (data.value == null) {
                    Toast.makeText(this@JoinActivity, "방을 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
                    clickable = true
                } else {
                    val count = data.child("count").value.toString().toInt()
                    val max = data.child("max").value.toString().toInt()
                    if (count < max) {
                        data.ref.child("users").child(id).setValue(0)
                        data.ref.child("count").setValue(data.child("users").childrenCount.toInt()).addOnCompleteListener {
                            if (it.isSuccessful) {
                                startActivity(Intent(this@JoinActivity, PushActivity::class.java).apply {
                                    putExtra("room", data.key)
                                    putExtra("id", id)
                                })
                                finish()
                            } else {
                                clickable = true
                            }
                        }
                    } else {
                        clickable = true
                        Toast.makeText(this@JoinActivity, "방이 꽉찼습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
