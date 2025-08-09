package com.example.foodfund.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfund.AvailableProductDetailsActivity
import com.example.foodfund.databinding.ItemDashboardLayoutBinding
import com.example.foodfund.databinding.ItemListLayoutBinding
import com.example.foodfund.models.Product
import com.example.foodfund.utils.Constants
import com.example.foodfund.ui.home.AvailableProductsFragment

class DashboardItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: AvailableProductsFragment
) : RecyclerView.Adapter<DashboardItemsListAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null

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

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener  = onClickListener
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val model = list[position]
        holder.binding.tvDashboardItemTitle.text = model.title
        holder.binding.tvDashboardItemLocation.text = model.pickup_point

        holder.itemView.setOnClickListener{
            val intent = Intent(context, AvailableProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
            intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
            context.startActivity(intent)
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

        /*
        holder.itemView.setOnClickListener{
            if(onClickListener != null){
                onClickListener!!.onClick(position, model)
            }
        } */
    }
    class MyViewHolder(val binding: ItemDashboardLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnClickListener{
        fun onClick(position: Int, product: Product)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    }

    // add viewBinding for both layout files necessary
    class MyViewHolder(val dashboardBinding: ItemDashboardLayoutBinding, val listBinding: ItemListLayoutBinding) : RecyclerView.ViewHolder(listBinding.root)

}
