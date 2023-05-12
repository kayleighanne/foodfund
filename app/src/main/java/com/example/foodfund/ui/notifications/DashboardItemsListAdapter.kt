package com.example.foodfund.ui.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfund.utils.GlideLoader
import com.example.foodfund.databinding.ItemDashboardLayoutBinding
import com.example.foodfund.models.Product

class DashboardItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
) : RecyclerView.Adapter<DashboardItemsListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemDashboardLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.binding.ivDashboardItemImage
            )
            holder.binding.tvDashboardItemTitle.text = model.title
            holder.binding.tvDashboardItemLocation.text = model.pickup_point
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(val binding: ItemDashboardLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
