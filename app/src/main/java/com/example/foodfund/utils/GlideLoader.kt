package com.example.foodfund.utils


import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.foodfund.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(imageURI: Uri, imageView: ImageView) {
        try {
            // load image in ImageView.
            Glide
                .with(context)
                .load(Uri.parse(imageURI.toString()))
                .centerCrop()
                .placeholder(R.drawable.user_placeholder) // if image fails to load a placeholder will be shown
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}