package com.example.foodfund

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.foodfund.databinding.ActivityBaseBinding
import com.example.foodfund.databinding.ActivityHomeBinding
import com.example.foodfund.databinding.DialogProgressBinding
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    // lateinit to enable view binding
    private lateinit var dialogProgressBinding: DialogProgressBinding

    // lateinit to use progress bar
    private lateinit var mProgressDialog: Dialog

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    // function to show progress spinner on screen
    fun showProgressDialog(text: String) {

        dialogProgressBinding = DialogProgressBinding.inflate(layoutInflater)
        setContentView(dialogProgressBinding.root)

        // init dialog
        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.dialog_progress)

        // gives the option to change the text displayed
        dialogProgressBinding.tvProgressText.text = text


        // cant be cancelled by clicking beside it
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        // start the dialog and display it on screen
        mProgressDialog.show()
    }

    // function to hide progress spinner from screen
    fun hideProgressDialog() {
        if (::mProgressDialog.isInitialized) {
            mProgressDialog.dismiss()
        }
    }

    fun doubleBackToExit() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}
