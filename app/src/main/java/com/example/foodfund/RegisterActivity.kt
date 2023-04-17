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
import com.example.foodfund.databinding.ActivityRegisterBinding
import com.example.foodfund.firestore.FirestoreClass
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.myshoppal.models.User
import java.util.concurrent.locks.ReentrantLock


class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enable viewBinding to reference from other files
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        setupActionBar()


        binding.tvLogin.setOnClickListener {

            onBackPressed()

            // Launch the login screen when the user clicks on the text.
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }


        binding.btnRegister.setOnClickListener {

            // call register user function
            registerUser()
        }


    }

    private fun validateRegisterDetails(): Boolean {
        return when {

            TextUtils.isEmpty(binding.etFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding.etLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false
            }

            // checking password and confirm password match
            binding.etPassword.text.toString()
                .trim { it <= ' ' } != binding.etConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    true
                )
                false
            }

            // checking terms and conditions check box is ticked
            !binding.cbTermsAndCondition.isChecked -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_agree_terms_and_condition),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }

    private fun registerUser() {

        // Check with validation function if the entries are valid or not.
        if (validateRegisterDetails()) {

            // show the spinner while details are being validated
            showProgressDialog(resources.getString((R.string.please_wait)))


            // store email and password in variables and strip whitespace
            val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.etPassword.text.toString().trim { it <= ' ' }

            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                // onCompleteListener executes this once this situation has happened

                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully complete
                        if (task.isSuccessful) {

                            // Firebase registers user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                binding.etFirstName.text.toString().trim { it <= ' '},
                                binding.etLastName.text.toString().trim { it <= ' '},
                                binding.etEmail.text.toString().trim { it <= ' '}
                            )

                            // call registerUser function and pass through user info
                            FirestoreClass().registerUser(this@RegisterActivity, user)

                            // success message
                            Toast.makeText(
                                this@RegisterActivity,
                                "You have been successfully registered to FoodFund.",
                                Toast.LENGTH_SHORT
                            ).show()


                            val intent =
                                Intent(this@RegisterActivity, HomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                            finish()

                        } else {

                            // dismiss the progress spinner
                            hideProgressDialog()

                            // If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

    fun userRegistrationSuccess() {

        // hide the progress spinner
        hideProgressDialog()

        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar_register_activity))
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_button)
        }
        binding.toolbarRegisterActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}

