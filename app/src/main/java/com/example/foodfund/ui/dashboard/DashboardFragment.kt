package com.example.foodfund.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfund.BaseFragment
import com.example.foodfund.LoginActivity
import com.example.foodfund.R
import com.example.foodfund.databinding.FragmentDashboardBinding
import com.example.foodfund.models.Product
import com.example.foodfund.ui.home.AvailableProductsFragment
import com.example.foodfund.ui.notifications.AvailableProductsListAdapter
import com.google.firebase.auth.FirebaseAuth

    class DashboardFragment : BaseFragment() {

        private var _binding: FragmentDashboardBinding? = null
        private val binding get() = _binding!!

        /*private lateinit var dashboardViewModel: DashboardViewModel*/

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentDashboardBinding.inflate(inflater, container, false)
            val root = binding.root

            /*dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)*/

            return root
        }

        /*
        override fun onResume() {
            super.onResume()

            getDashboardItemsList()
        }
*/

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Call your method to load data here or in another suitable place
            // For example:
            // viewModel.loadDashboardItemsList()
        }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.dashboard_menu, menu)
            super.onCreateOptionsMenu(menu, inflater)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId

            when (id) {
                R.id.action_logout -> {
                    // logout from app.
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
            return super.onOptionsItemSelected(item)
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