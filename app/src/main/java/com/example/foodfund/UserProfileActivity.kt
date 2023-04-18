package com.example.foodfund

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodfund.databinding.ActivityUserProfileBinding
import com.example.foodfund.models.User


private lateinit var binding: ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enable viewBinding to allow you to reference from other files
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDetails: User = User()

        binding.etFirstName.isEnabled = false
        binding.etFirstName.setText(userDetails.firstName)

        binding.etLastName.isEnabled = false
        binding.etLastName.setText(userDetails.lastName)

        binding.etEmail.isEnabled = false
        binding.etEmail.setText(userDetails.email)
    }
}