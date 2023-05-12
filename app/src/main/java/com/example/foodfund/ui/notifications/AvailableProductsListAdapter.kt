package com.example.foodfund.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfund.databinding.ItemListLayoutBinding
import com.example.foodfund.models.Product
import com.example.foodfund.ui.home.AvailableProductsFragment
import com.example.foodfund.utils.GlideLoader

class AvailableProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    availableProductsFragment: AvailableProductsFragment
) : RecyclerView.Adapter<AvailableProductsListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        GlideLoader(context).loadProductPicture(model.image, holder.binding.itemImageBg)
        holder.binding.tvItemName.text = model.title
        holder.binding.tvItemPickupLocation.text = model.pickup_point
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(val binding: ItemListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
