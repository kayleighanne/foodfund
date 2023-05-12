package com.example.foodfund.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfund.AddProductActivity
import com.example.foodfund.BaseFragment
import com.example.foodfund.R
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
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFirestore()
    }

    private fun getProductListFromFirestore() {
        showProgressDialog(getString(R.string.please_wait))

        FirestoreClass().getProductsList(this@AvailableProductsFragment)
    }

    fun deleteProduct(productID: String) {
        Toast.makeText(
            requireActivity(),
            "You can now delete the product. $productID",
            Toast.LENGTH_SHORT
        ).show()
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
}

