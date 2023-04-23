package com.example.foodfund

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.foodfund.databinding.ActivityUserProfileBinding
import com.example.foodfund.models.User
import com.example.foodfund.utils.Constants
//import java.util.jar.Manifest
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.foodfund.firestore.FirestoreClass
import com.example.foodfund.utils.GlideLoader
import java.io.IOException

private lateinit var binding: ActivityUserProfileBinding

private var mSelectedImageFileUri: Uri? = null

private var mUserProfileImageURL: String = ""

class UserProfileActivity : BaseActivity(), View.OnClickListener {
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

        // assign an on click listener to the profile photo
        binding.userImageBackground.setOnClickListener(this@UserProfileActivity)

        binding.btnSave.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.user_image_background -> {

                    // check read external storage permission and ensure it is allowed
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)
                    } else {

                        // permissions must be granted in manifest not the actual app itself
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_save -> {

                    showProgressDialog(resources.getString(R.string.please_wait))

                    FirestoreClass().uploadImageToCloudStorage(
                        this@UserProfileActivity,
                        mSelectedImageFileUri
                    )

                    if (mSelectedImageFileUri != null) {

                        FirestoreClass().uploadImageToCloudStorage(
                            this@UserProfileActivity,
                            mSelectedImageFileUri
                        )
                    } else {
                        Log.e("Request Cancelled", "Image upload cancelled")
                    }

                    updateUserImage()
                }
            }

        }

        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Constants.showImageChooser(this)
                } else {

                    Toast.makeText(
                        this,
                        resources.getString(R.string.read_storage_permission_denied),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == Constants.SELECT_IMAGE_REQUEST_CODE) {
                    if (data != null) {
                        try {
                            // The uri of selected image from phone storage.
                            mSelectedImageFileUri = data.data!!

                            GlideLoader(this).loadUserPicture(
                                mSelectedImageFileUri!!,
                                binding.userImageBackground
                            )
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@UserProfileActivity,
                                resources.getString(R.string.image_selection_failed),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // A log is printed when user close or cancel the image selection.
                Log.e("Request Cancelled", "Image selection cancelled")
            }
        }

    }
    fun imageUploadSuccess(imageURL: String) {

        mUserProfileImageURL = imageURL
        updateUserImage()
    }

    private fun updateUserImage() {

        val userHashMap = HashMap<String, Any>()

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }
    }
}