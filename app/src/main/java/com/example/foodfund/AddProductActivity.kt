package com.example.foodfund
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.foodfund.databinding.ActivityAddProductBinding
import com.example.foodfund.utils.Constants
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import com.example.foodfund.firestore.FirestoreClass
import com.example.foodfund.models.Product
import com.example.foodfund.utils.GlideLoader
//import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.IOException


class AddProductActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddProductBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityAddProductBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupActionBar()

            // Assign the click event to submit button.
            binding.btnSubmit.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            if (v != null) {
                when (v.id) {
                    R.id.btn_submit -> {

                        if (validateProductDetails()) {

                            uploadProductDetails()
                        }
                    }
                }
            }
        }

        private fun validateProductDetails(): Boolean {
            return when {

                TextUtils.isEmpty(binding.etProductTitle.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title), true)
                    false
                }

                TextUtils.isEmpty(binding.etPickupPoint.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_pickup_location), true)
                    false
                }

                TextUtils.isEmpty(binding.etProductDescription.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar(
                        resources.getString(R.string.err_msg_enter_product_description),
                        true
                    )
                    false
                }

                TextUtils.isEmpty(binding.etProductQuantity.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar(
                        resources.getString(R.string.err_msg_enter_product_quantity),
                        true
                    )
                    false
                }
                else -> {
                    true
                }
            }
        }

        private fun uploadProductDetails() {

            // Get the logged in username from the SharedPreferences that we have stored at a time of login.
            val username =
                this.getSharedPreferences(Constants.FOODFUND_PREFERENCES, Context.MODE_PRIVATE)
                    .getString(Constants.LOGGED_IN_USER, "")!!

            // Here we get the text from editText and trim the space
            val product = Product(
                FirestoreClass().getCurrentUserID(),
                username,
                binding.etProductTitle.text.toString().trim { it <= ' ' },
                binding.etPickupPoint.text.toString().trim { it <= ' ' },
                binding.etProductDescription.text.toString().trim { it <= ' ' },
                binding.etProductQuantity.text.toString().trim { it <= ' ' }
            )

            FirestoreClass().uploadProductDetails(this@AddProductActivity, product)
        }

        /**
         * A function to return the successful result of Product upload.
         */
        fun productUploadSuccess() {

            // Hide the progress dialog
            hideProgressDialog()

            Toast.makeText(
                this@AddProductActivity,
                resources.getString(R.string.product_uploaded_success_message),
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }


    private fun setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar_add_product_activity))
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_button)
        }
        binding.toolbarAddProductActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}

