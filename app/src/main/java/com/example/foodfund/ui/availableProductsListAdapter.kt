package com.example.foodfund.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfund.AvailableProductDetailsActivity
import com.example.foodfund.databinding.ItemListLayoutBinding
import com.example.foodfund.models.Product
import com.example.foodfund.ui.home.AvailableProductsFragment

class AvailableProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: AvailableProductsFragment
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

        if (holder is MyViewHolder) {
            holder.binding.tvItemName.text = model.title
            holder.binding.tvItemPickupLocation.text = model.pickup_point

            holder.binding.ibDeleteProduct.setOnClickListener{
                fragment.deleteProduct(model.product_id)
            }

            holder.itemView.setOnClickListener{
                val intent = Intent(context,AvailableProductDetailsActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(val binding: ItemListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
