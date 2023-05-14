package com.example.foodfund

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.foodfund.databinding.ActivityCartListBinding
import com.example.foodfund.databinding.ActivityLoginBinding
import com.example.foodfund.firestore.FirestoreClass
import com.example.foodfund.models.CartItem

class CartListActivity : BaseActivity() {

    private lateinit var binding: ActivityCartListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
    }

    override fun onResume() {
        super.onResume()

        getCartItemsList()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCartListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarCartListActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getCartItemsList() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCartList(this@CartListActivity)
    }


    fun successCartItemsList(cartList: ArrayList<CartItem>) {

        // Hide progress dialog.
        hideProgressDialog()

        for (i in cartList) {

            Log.i("Cart Item Title", i.title)

        }
    }
}