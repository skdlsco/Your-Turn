package com.rummicube.yourturn

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    val db = FirebaseDatabase.getInstance()
    lateinit var pref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val id = intent.getStringExtra("id")
        val roomId = intent.getStringExtra("room")
        pref = db.getReference(roomId)
        pref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(data: DataSnapshot) {
                pref.removeEventListener(this)
                val count = data.child("max").value.toString().toInt() - 1
                data.child("users").children.reversed().forEachIndexed { i, data ->
                    if (data.key == id) {
                        Log.e("asdsdf", "${i}    $count")
                        result_text.text =
                                if (count == i)
                                    "당신은 마지막 로켓입니다!"
                                else
                                    "당신은 마지막 로켓이 아닙니다!"
                    }
                }
            }
        })
        close_button.setOnClickListener { finish() }
    }
}
