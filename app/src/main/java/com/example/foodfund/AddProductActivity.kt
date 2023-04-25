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

        // A global variable for URI of a selected image from phone storage.
        private var mSelectedImageFileUri: Uri? = null

        // A global variable for uploaded product image URL.
        private var mProductImageURL: String = ""

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityAddProductBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupActionBar()

            // Assign the click event to iv_add_update_product image.
            binding.ivAddUpdateProduct.setOnClickListener(this)

            // Assign the click event to submit button.
            binding.btnSubmit.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            if (v != null) {
                when (v.id) {

                    // The permission code is similar to the user profile image selection.
                    R.id.iv_add_update_product -> {
                        if (ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            Constants.showImageChooser(this@AddProductActivity)
                        } else {
                            /*Requests permissions to be granted to this application. These permissions
                             must be requested in your manifest, they should not be granted to your app,
                             and they should have protection level*/
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                Constants.READ_STORAGE_PERMISSION_CODE
                            )
                        }
                    }

                    R.id.btn_submit -> {
                        if (validateProductDetails()) {

                            uploadProductImage()
                        }
                    }
                }
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
                //If permission is granted
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Constants.showImageChooser(this@AddProductActivity)
                } else {
                    //Displaying another toast if permission is not granted
                    Toast.makeText(
                        this,
                        resources.getString(R.string.read_storage_permission_denied),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK
                && requestCode == Constants.SELECT_IMAGE_REQUEST_CODE
                && data!!.data != null
            ) {

                // Replace the add icon with edit icon once the image is selected.
                binding.ivAddUpdateProduct.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@AddProductActivity,
                        R.drawable.ic_baseline_mode_edit_outline_24
                    )
                )

                // The uri of selection image from phone storage.
                mSelectedImageFileUri = data.data!!

                try {
                    // Load the product image in the ImageView.
                    GlideLoader(this@AddProductActivity).loadProductPicture(
                        mSelectedImageFileUri!!,
                        binding.ivProductImage
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        private fun validateProductDetails(): Boolean {
            return when {

                mSelectedImageFileUri == null -> {
                    showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image), true)
                    false
                }

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

        private fun uploadProductImage() {

            showProgressDialog(resources.getString(R.string.please_wait))

            FirestoreClass().uploadImageToCloudStorage(
                this@AddProductActivity,
                mSelectedImageFileUri,
                Constants.PRODUCT_IMAGE
            )
        }

        fun imageUploadSuccess(imageURL: String) {

            // Initialize the global image url variable.
            mProductImageURL = imageURL

            uploadProductDetails()
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
                binding.etProductQuantity.text.toString().trim { it <= ' ' },
                mProductImageURL
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

