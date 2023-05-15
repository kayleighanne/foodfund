package com.example.foodfund

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.foodfund.databinding.ActivityLoginBinding
import com.example.foodfund.firestore.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import com.example.foodfund.models.User
import com.example.foodfund.ui.home.AvailableProductsFragment


class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enable viewBinding to reference from other files
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                // make fullscreen on the device
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding.tvRegister.setOnClickListener {

            // Launch the register screen when the user clicks on register
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvForgotPassword.setOnClickListener {

            // launch the forgot password screen when the user clicks on forgot password
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)

        }

        binding.btnLogin.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                    //prompt the user to enter email
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                    // prompt the user to enter password
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                    // store the users details in variables and trim any whitespace
                    val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
                    val password: String = binding.etPassword.text.toString().trim { it <= ' ' }

                    // login using FirebaseAuth
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                FirestoreClass().getUserDetails(this@LoginActivity)

                                Toast.makeText(
                                    this@LoginActivity,
                                    "You have successfully logged in.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@LoginActivity, NavigationActivity::class.java)
                                intent.flags =
                                        // clear the fields after login is complete
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(
                                    "user_id",
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                )
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            } else {

                                // If the login is unsuccessful then show error message.
                                Toast.makeText(
                                    this@LoginActivity,
                                    "error logging in",
                                    // task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
    // function to get user details from db
    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()

        // Print the user details in the log as of now.
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        // Redirect the user to Main Screen after log in.
        startActivity(Intent(this@LoginActivity, NavigationActivity::class.java))
        finish()
    }


}
