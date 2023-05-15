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
        // call parent constructor
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        binding.btnPlaceOrder.setOnClickListener {
            placeAnOrder()
        }

        getProductList()
    }

    /**
     * A function to setup an actionBar .
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCheckoutActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarCheckoutActivity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to get product list to compare the current stock with the cart items.
     */
    private fun getProductList() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getAllProductsList(this@CheckoutActivity)
    }

    /**
     * A function to get the success result of product list.
     *
     * @param productsList
     */
    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        mProductsList = productsList

        getCartItemsList()
    }

    /**
     * A function to get the list of cart items in the activity.
     */
    private fun getCartItemsList() {

        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     *
     * @param cartList
     */
    fun successCartItemsList(cartList: ArrayList<CartItem>) {

        // Hide progress dialog.
        hideProgressDialog()

        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_quantity = product.quantity
                }
            }
        }

        mCartItemsList = cartList

        binding.rvCartListItems.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCartListItems.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity, mCartItemsList, false)
        binding.rvCartListItems.adapter = cartListAdapter

    }

    /**
     * A function to prepare the Order details to place an order.
     */
    private fun placeAnOrder() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        val order = Order(
            FirestoreClass().getCurrentUserID(),
            mCartItemsList,
            "My order ${System.currentTimeMillis()}"
        )

        FirestoreClass().placeOrder(this@CheckoutActivity, order)
    }

    /**
     * A function to notify the success result of the order placed.
     */
    fun orderPlacedSuccess() {

        FirestoreClass().updateAllDetails(this@CheckoutActivity, mCartItemsList)
    }

    /**
     * A function to notify the success result after updating all the required details.
     */
    fun allDetailsUpdatedSuccessfully() {

        hideProgressDialog()

        Toast.makeText(this@CheckoutActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this@CheckoutActivity, NavigationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}