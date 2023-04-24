package com.example.foodfund.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodfund.LoginActivity
import com.example.foodfund.R
import com.example.foodfund.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : Fragment() {

    /*private lateinit var dashboardViewModel: DashboardViewModel*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // add the option menu to fragment to use it
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)*/

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        textView.text = "This is dashboard Fragment"

        /*dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
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
                    // Logout from app.
                    FirebaseAuth.getInstance().signOut()

                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
        return super.onOptionsItemSelected(item)
    }
}
