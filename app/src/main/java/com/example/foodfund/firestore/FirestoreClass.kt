package com.example.foodfund.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.foodfund.LoginActivity
import com.example.foodfund.RegisterActivity
import com.example.foodfund.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.example.foodfund.models.User


class FirestoreClass {

    // access a Cloud Firestore instance
    private val mFireStore = FirebaseFirestore.getInstance()

    // make a record of the user in the db
    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // create a collection called users
        mFireStore.collection(com.example.foodfund.utils.Constants.USERS)
            // document id == user uid
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                //  call a function of base activity
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }


    // retrieve user id of the current user
    fun getCurrentUserID(): String {
        // create an instancw of current user
        val currentUser = FirebaseAuth.getInstance().currentUser

        // create a variable to assign the user id to
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    // retrieve current user details from db
    fun getUserDetails(activity: Activity) {

        // pass through the users collection
        mFireStore.collection(com.example.foodfund.utils.Constants.USERS)
            // get the document id
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // turn the document into an object
                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.FOODFUND_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USER,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        // call a function of base activity for storing result
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                // hide progress dialog and log and display error message
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while retrieving user details.",
                    e
                )
            }
    }
}
