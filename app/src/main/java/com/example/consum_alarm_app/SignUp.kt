package com.example.consum_alarm_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var signName: EditText
    private lateinit var signID: EditText
    private lateinit var signPW: EditText
    private lateinit var signPW2: EditText
    private lateinit var signMail: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        signName = findViewById(R.id.signName)
        signPW = findViewById(R.id.signPW)
        signPW2 = findViewById(R.id.signPW2)
        signMail = findViewById(R.id.signmail)
        signUpButton = findViewById(R.id.signupbutton)

        signUpButton.setOnClickListener {
            val email = signMail.text.toString()
            val password = signPW.text.toString()
            val password2 = signPW2.text.toString()

            if (password == password2) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
            } else {
                Toast.makeText(baseContext, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }
    }
}