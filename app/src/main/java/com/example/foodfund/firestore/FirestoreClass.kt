package com.example.foodfund.firestore

import android.util.Log
import com.example.foodfund.RegisterActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.myshoppal.models.User

class FirestoreClass {

    // access a Cloud Firestore instance
    private val mFireStore = FirebaseFirestore.getInstance()

    // functio to make a record of the registered user in the db
    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // create a users collection in db
        mFireStore.collection("users")
            // document id == user id
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // call register function
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                // log error
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }



}