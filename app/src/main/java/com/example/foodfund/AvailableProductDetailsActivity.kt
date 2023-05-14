package com.example.foodfund

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.foodfund.databinding.ActivityAvailableProductDetailsBinding
import com.example.foodfund.firestore.FirestoreClass
import com.example.foodfund.models.CartItem
import com.example.foodfund.models.Product
import com.example.foodfund.ui.dashboard.DashboardFragment
import com.example.foodfund.utils.Constants

class AvailableProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAvailableProductDetailsBinding
    private var mProductId: String = ""
    private lateinit var mProductDetails: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvailableProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()


        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product ID", mProductId)
        }
        var productOwnerId = ""

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if (FirestoreClass().getCurrentUserID() == productOwnerId) {
            binding.btnAddToCart.visibility = View.GONE
            binding.btnGoToCart.visibility = View.GONE
        } else {
            binding.btnAddToCart.visibility = View.VISIBLE
        }

        binding.btnAddToCart.setOnClickListener(this)
        binding.btnGoToCart.setOnClickListener(this)
    }

    fun productExistsInCart() {
        hideProgressDialog()
        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this, mProductId)
        hideProgressDialog()
    }

    fun productDetailsSuccess(product: Product) {
        mProductDetails = product

        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsLocation.text = product.pickup_point
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsAvailableQuantity.text = product.quantity

        if (FirestoreClass().getCurrentUserID() == product.user_id) {
            hideProgressDialog()
        } else {
            FirestoreClass().checkIfItemExistsInCart(this, mProductId)
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAvailableProductDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.login_bg)
        }

        binding.toolbarAvailableProductDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun addToCart() {
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.pickup_point,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addCartItems(this, cartItem)
    }

    fun addToCartSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@AvailableProductDetailsActivity,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()

        binding.btnAddToCart.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_add_to_cart -> {
                    addToCart()
                }
                R.id.btn_go_to_cart -> {
                    startActivity(
                        Intent(
                            this@AvailableProductDetailsActivity,
                            CartListActivity::class.java
                        )
                    )
                }
            }
        }
    }
}