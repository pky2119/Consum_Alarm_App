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

        // 리사이클러뷰에 레이아웃 매니저와 어댑터 설정
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        // Firebase Realtime Database 초기화
        val databaseUrl = "https://consumalarmapp-default-rtdb.firebaseio.com/"
        database = FirebaseDatabase.getInstance(databaseUrl).reference

        // Firebase 사용자 인증 정보 가져오기
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser!!.uid

        // Firebase에서 현재 사용자의 상품 정보 가져오기
        val ref = database.child("products").child(uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<Product>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let {
                        products.add(it)
                    }
                }
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
    }

    data class Product(
        val productName: String ?= null,
        val purchaseDate: String ?= null,
        val price: String ?= null,
        val expirationDate: String ?= null,
        val remainingExpiration: String ?= null
    )
}
