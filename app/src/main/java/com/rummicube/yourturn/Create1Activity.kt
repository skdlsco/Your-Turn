package com.rummicube.yourturn

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create1.*
import java.util.*


class Create1Activity : AppCompatActivity() {
    val db = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create1)
        button.setOnClickListener {
            if (edit_text.text.toString() != "")
                makeRoom()
            else
                Toast.makeText(this@Create1Activity, "최대 인원 수를 입력해 주세요", Toast.LENGTH_SHORT).show()
        }
        close_button.setOnClickListener {
            finish()
        }
    }

    fun makeRoom() {
        val id = randomString()
        db.getReference(id).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(data: DataSnapshot) {
                db.getReference(id).removeEventListener(this)
                if (data.value != null)
                    makeRoom()
                else {
                    data.ref.child("max").setValue(edit_text.text.toString().toInt())
                    data.ref.child("count").setValue(0)
                    startActivity(Intent(this@Create1Activity, Create2Activity::class.java).apply {
                        putExtra("room", data.key)
                    })
                    finish()
                }
            }
        })
    }

    fun randomString(): String {
        val data = "0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz"
        var result = ""
        (0 until 5).forEach {
            result += data[Random().nextInt((data.length) - 0)]
        }
        return result
    }
}
