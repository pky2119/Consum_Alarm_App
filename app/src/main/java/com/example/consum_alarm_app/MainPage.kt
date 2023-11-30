package com.example.consum_alarm_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainPage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ProductAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerview)
        layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter()

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val databaseUrl = "https://consumalarmapp-default-rtdb.firebaseio.com/"
        database = FirebaseDatabase.getInstance(databaseUrl).reference

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser!!.uid

        val ref = database.child("products").child(uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<Product>()
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                val currentDate = Date()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)

                    if (product != null) {
                        val expirationDate = sdf.parse(product.expirationDate)
                        val diffInMillis = expirationDate.time - currentDate.time
                        val diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)

                        if (diffInDays >= 0) {
                            product.remainingDays = diffInDays
                            products.add(product)
                        }
                    }
                }

                products.sortBy { it.remainingDays }
                adapter.setData(products)
                Log.d("MainPage", "데이터 가져오기 성공 - 상품 개수: ${products.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainPage", "데이터 가져오기 실패: ${error.toException().message}")
            }
        })

        val btnRegistProduct = findViewById<Button>(R.id.regist_product)
        btnRegistProduct.setOnClickListener {
            val intent = Intent(this, Regist::class.java)
            startActivity(intent)
        }

        val btnRegist_Infomation = findViewById<Button>(R.id.check_product)
        btnRegist_Infomation.setOnClickListener {
            val intent = Intent(this, Regist_Infomation::class.java)
            startActivity(intent)
        }
    }

    data class Product(
        val productName: String ?= null,
        val purchaseDate: String ?= null,
        val price: String ?= null,
        val expirationDate: String ?= null,
        var remainingDays: Long = 0 // 추가된 속성
    )
}
