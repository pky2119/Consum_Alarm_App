package com.example.consum_alarm_app

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class Regist : AppCompatActivity() {
    private lateinit var edtPurchaseDate: EditText
    private lateinit var edtExpirationDate: EditText
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)

        edtPurchaseDate = findViewById(R.id.edtPurchaseDate)
        edtExpirationDate = findViewById(R.id.edtExpirationDate)
    }

    fun showPurchaseDatePicker(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate =
                    String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                edtPurchaseDate.setText(formattedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    fun showExpirationDatePicker(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate =
                    String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                edtExpirationDate.setText(formattedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    fun saveProductInfo(view: View) {
        // 로그인 중인 사용자의 UID 가져오기
        val user = auth.currentUser
        val uid = user?.uid

        // EditText에서 정보 가져오기
        val productName = findViewById<EditText>(R.id.edtProductName).text.toString()
        val purchaseDate = findViewById<EditText>(R.id.edtPurchaseDate).text.toString()
        val price = findViewById<EditText>(R.id.edtPrice).text.toString()
        val expirationDate = findViewById<EditText>(R.id.edtExpirationDate).text.toString()


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("products")

        // Product 객체 생성
        val product = mapOf("productName" to productName,
            "purchaseDate" to purchaseDate,
            "price" to price,
            "expirationDate" to expirationDate)

        // Firebase 실시간 데이터베이스에 저장
        myRef.child(uid!!).push().setValue(product)
        // 정보를 캘린더 이벤트로 저장
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, "상품명: $productName, 가격: $price")
            putExtra(
                CalendarContract.Events.DESCRIPTION,
                "구매일: $purchaseDate, 소비기한: $expirationDate"
            )

            // 이벤트 시작 및 종료 시간 설정 (예: 소비기한)
            // 소비기한을 yyyy-mm-dd 형식으로 변환 필요
            val parts = expirationDate.split("-").map { it.toInt() }
            val startTime = Calendar.getInstance().apply {
                set(parts[0], parts[1] - 1, parts[2])
            }
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.timeInMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTime.timeInMillis)
        }

        // 사용자가 캘린더 이벤트를 추가할 수 있게 앱 시작
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }

        // 캘린더 화면으로 전환하기 위한 인텐트를 생성합니다.
        val intentToMainCalendar = Intent(this, MainCalendar::class.java)

        // 인텐트를 시작합니다.
        startActivity(intentToMainCalendar)

        // 현재 액티비티를 종료합니다.
        finish()
    }
}
