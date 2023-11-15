package com.example.consum_alarm_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.database.*

class MainCalendar : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var dateInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistProduct = findViewById<Button>(R.id.regist_product)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        // Firebase 인스턴스 초기화
        database = FirebaseDatabase.getInstance()



        btnRegistProduct.setOnClickListener {
            val intent = Intent(this, Regist::class.java)
            startActivity(intent)
        }

        calendarView.setOnDateChangeListener { _, year, month, day ->
            val selectedDate = String.format("%d-%02d-%02d", year, month + 1, day)
            loadProductInfo(selectedDate)
        }
    }

    private fun loadProductInfo(selectedDate: String) {
        val myRef = database.getReference("products")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val productInfoLayout = findViewById<LinearLayout>(R.id.productInfoLayout)
                productInfoLayout.removeAllViews()
                for (productSnapshot in dataSnapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null && product.expirationDate == selectedDate) {
                        val productInfo = TextView(this@MainCalendar)
                        productInfo.text = "상품명: ${product.productName}, 가격: ${product.price}, 구매일: ${product.purchaseDate}, 소비기한: ${product.expirationDate}"
                        productInfoLayout.addView(productInfo)
                    }
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}

data class Product(
    var productName: String? = null,
    var purchaseDate: String? = null,
    var price: String? = null,
    var expirationDate: String? = null
)
