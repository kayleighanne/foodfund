package com.example.foodfund

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodfund.databinding.ActivityHomeBinding
import com.example.foodfund.databinding.ActivityRegisterBinding
import com.example.foodfund.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enable viewBinding to reference from other files
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences  = getSharedPreferences(Constants.FOODFUND_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USER, "")!!

        // variables to store user id and email address
        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        binding.tvUserId.text = "User ID :: $userId"
        binding.tvEmailId.text = "Email ID :: $emailId"

        binding.btnLogout.setOnClickListener {

            // Logout from app.
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }
    }
}