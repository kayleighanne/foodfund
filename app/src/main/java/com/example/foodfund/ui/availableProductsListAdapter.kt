package com.example.foodfund.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfund.R
import com.example.foodfund.models.Product
import com.example.foodfund.utils.GlideLoader

class availableProductsListAdapter(
    private val context: Context,
            private var list: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder> () {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder)
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.iv_item_image)
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}