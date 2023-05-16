package com.example.foodfund.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfund.*
import com.example.foodfund.databinding.FragmentAvailableProductsBinding
import com.example.foodfund.firestore.FirestoreClass
import com.example.foodfund.models.Product
import com.example.foodfund.ui.AvailableProductsListAdapter

class AvailableProductsFragment : BaseFragment() {

    private lateinit var binding: FragmentAvailableProductsBinding
    private lateinit var adapter: AvailableProductsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAvailableProductsBinding.inflate(inflater, container, false)

        adapter = AvailableProductsListAdapter(requireContext(), ArrayList(), this)
        binding.availableProducts.adapter = adapter
        binding.availableProducts.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu, menu)
        inflater.inflate(R.menu.cart_menu, menu)
        inflater.inflate(R.menu.checkout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }

        when (id) {
            R.id.action_cart -> {
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        when (id) {
            R.id.action_checkout -> {
                startActivity(Intent(activity, CheckoutActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFirestore()
    }

    fun deleteProduct(productID: String){
        showAlertDialogToDeleteProduct(productID)
    }

    fun productDeleteSuccess() {

        hideProgressDialog()

        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.product_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()

        getProductListFromFirestore()
    }

    fun getProductListFromFirestore() {
        showProgressDialog(getString(R.string.please_wait))

        FirestoreClass().getProductsList(this@AvailableProductsFragment)
    }

    fun successProductsListFromFirestore(productsList: ArrayList<Product>) {
        hideProgressDialog()
        if (productsList.isNotEmpty()) {
            binding.availableProducts.visibility = View.VISIBLE
            binding.tvNoProductsFound.visibility = View.GONE

            binding.availableProducts.layoutManager = LinearLayoutManager(activity)
            binding.availableProducts.setHasFixedSize(true)

            val adapterProducts = AvailableProductsListAdapter(requireActivity(), productsList, this@AvailableProductsFragment)
            binding.availableProducts.adapter = adapterProducts
        } else {
            binding.availableProducts.visibility = View.GONE
            binding.tvNoProductsFound.visibility = View.VISIBLE
        }
    }
    private fun showAlertDialogToDeleteProduct(productID: String) {
        val builder = AlertDialog.Builder(requireActivity())
        //set title & message for alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        // positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))
            // call deleteProduct from firestore class
            FirestoreClass().deleteProduct(this@AvailableProductsFragment, productID)
            dialogInterface.dismiss()
        }
        // negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        // create AlertDialog & set properties
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}

