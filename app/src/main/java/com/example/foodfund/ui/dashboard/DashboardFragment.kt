package com.example.foodfund.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfund.*
import com.example.foodfund.databinding.FragmentDashboardBinding
import com.example.foodfund.firestore.FirestoreClass
import com.example.foodfund.models.Product
import com.example.foodfund.ui.AvailableProductsListAdapter
import com.example.foodfund.ui.DashboardItemsListAdapter
import com.example.foodfund.ui.home.AvailableProductsFragment
import com.example.foodfund.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        //val root = binding.root
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_logout -> {
                // logout from app
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(activity, LoginActivity::class.java))
            }
            R.id.action_cart ->{
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        getDashboardItemsList()
    }

    private fun getDashboardItemsList() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getDashboardItemsList(this@DashboardFragment)
    }

    fun successDashboardItemsList(dashboardItemsList: ArrayList<Product>) {

        hideProgressDialog()

        if (dashboardItemsList.size > 0) {

            binding.rvDashboardItems.visibility = View.VISIBLE
            binding.tvNoDashboardItemsFound.visibility = View.GONE

            binding.rvDashboardItems.layoutManager = GridLayoutManager(activity, 2)
            binding.rvDashboardItems.setHasFixedSize(true)

            val adapterAvailableProducts = AvailableProductsListAdapter(requireActivity(), dashboardItemsList, AvailableProductsFragment())
            binding.rvDashboardItems.adapter = adapterAvailableProducts

            val adapterDashboardItems = DashboardItemsListAdapter(requireActivity(), dashboardItemsList)


            adapterDashboardItems.setOnClickListener(object: DashboardItemsListAdapter.OnClickListener{
                override fun onClick(position: Int, product: Product){
                    hideProgressDialog()
                    val intent = Intent(context, AvailableProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
                    startActivity(intent)
                }
            })

        } else {
            binding.rvDashboardItems.visibility = View.GONE
            binding.tvNoDashboardItemsFound.visibility = View.VISIBLE
        }

        fun successDashboardItemsList(dashboardItemsList: ArrayList<Product>) {

            hideProgressDialog()

           if (dashboardItemsList.size > 0) {

                binding.rvDashboardItems.visibility = View.VISIBLE
                binding.tvNoDashboardItemsFound.visibility = View.GONE

                binding.rvDashboardItems.layoutManager = GridLayoutManager(activity, 2)
                binding.rvDashboardItems.setHasFixedSize(true)

                val adapter = AvailableProductsListAdapter(requireActivity(), dashboardItemsList, AvailableProductsFragment())
                binding.rvDashboardItems.adapter = adapter
            } else {
                binding.rvDashboardItems.visibility = View.GONE
                binding.tvNoDashboardItemsFound.visibility = View.VISIBLE
            }
        }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
