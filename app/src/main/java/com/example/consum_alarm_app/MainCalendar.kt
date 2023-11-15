package com.example.consum_alarm_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView

class MainCalendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistProduct = findViewById<Button>(R.id.regist_product)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        btnRegistProduct.setOnClickListener {
            val intent = Intent(this, Regist::class.java)
            startActivity(intent)
        }
    }
}