package com.example.foodfund.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val USERS: String = "users"
    const val FOODFUND_PREFERENCES: String = "FoodFundPrefs"
    const val LOGGED_IN_USER: String = "logged_in_user"
    const val SELECT_IMAGE_REQUEST_CODE = 1
    const val PRODUCTS: String = "products"
    const val USER_ID: String = "user_id"
    const val EXTRA_PRODUCT_ID: String = "extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID: String = "extra_product_owner_id"
    const val DEFAULT_CART_QUANTITY: String = "1"
    const val CART_ITEMS: String = "cart_items"
    const val PRODUCT_ID: String = "product_id"


    fun showImageChooser(activity: Activity) {

        // an intent for launching selection of the picture
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // launches the picture selection - starts activity with expectation of a result
        activity.startActivityForResult(galleryIntent, SELECT_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {

        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}