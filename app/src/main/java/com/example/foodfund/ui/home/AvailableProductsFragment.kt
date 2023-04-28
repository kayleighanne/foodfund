package com.example.foodfund.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.foodfund.AddProductActivity
import com.example.foodfund.BaseFragment
import com.example.foodfund.LoginActivity
import com.example.foodfund.R
import com.example.foodfund.firestore.FirestoreClass
import com.example.foodfund.models.Product
import com.google.firebase.auth.FirebaseAuth

class AvailableProductsFragment : BaseFragment() {

    /*private lateinit var homeViewModel: HomeViewModel*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        hideProgressDialog()

        for(i in productsList){
            Log.i("Product Name", i.title)
        }
    }

    private fun getProductListFromFireStore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductsList(this)
    }

    override fun onResume(){
        super.onResume()
        getProductListFromFireStore()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)*/

        val root = inflater.inflate(R.layout.fragment_available_products, container, false)

        /*homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        return root
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
}