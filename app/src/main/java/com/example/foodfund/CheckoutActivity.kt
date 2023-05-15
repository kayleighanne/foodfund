package com.example.foodfund

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfund.databinding.ActivityCartListBinding
import com.example.foodfund.databinding.ActivityCheckoutBinding
import com.example.foodfund.firestore.FirestoreClass
import com.example.foodfund.models.CartItem
import com.example.foodfund.models.Order
import com.example.foodfund.models.Product
import com.example.foodfund.ui.home.CartItemsListAdapter


class CheckoutActivity : BaseActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCheckoutActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarCheckoutActivity.setNavigationOnClickListener { onBackPressed() }
    }
}