package com.example.foodfund.ui.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfund.utils.GlideLoader
import com.example.foodfund.databinding.ItemDashboardLayoutBinding
import com.example.foodfund.databinding.ItemListLayoutBinding
import com.example.foodfund.models.Product
import com.example.foodfund.ui.home.AvailableProductsFragment

class DashboardItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: AvailableProductsFragment
) : RecyclerView.Adapter<DashboardItemsListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val dashboardBinding = ItemDashboardLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val listBinding = ItemListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(dashboardBinding, ItemListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.dashboardBinding.ivDashboardItemImage
            )
            holder.dashboardBinding.tvDashboardItemTitle.text = model.title
            holder.dashboardBinding.tvDashboardItemLocation.text = model.pickup_point

            holder.listBinding.ibDeleteProduct.setOnClickListener {
                fragment.deleteProduct(model.product_id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // add viewBinding for both layout files necessary
    class MyViewHolder(val dashboardBinding: ItemDashboardLayoutBinding, val listBinding: ItemListLayoutBinding) : RecyclerView.ViewHolder(listBinding.root)

}
