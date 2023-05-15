package com.example.foodfund.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfund.CartListActivity
import com.example.foodfund.R
import com.example.foodfund.databinding.ItemCartLayoutBinding
import com.example.foodfund.firestore.FirestoreClass
import com.example.foodfund.models.CartItem
import com.example.foodfund.utils.Constants

class CartItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<CartItem>,
    private var updateCartItems: Boolean
) : RecyclerView.Adapter<CartItemsListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCartLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvCartItemTitle.text = model.title
        holder.binding.tvCartItemPickupPoint.text = model.pickup_point
        holder.binding.tvCartQuantity.text = model.cart_quantity

        if (model.cart_quantity == "0") {
            holder.binding.ibRemoveCartItem.visibility = View.GONE
            holder.binding.ibAddCartItem.visibility = View.GONE

            if (updateCartItems) {
                holder.binding.ibDeleteCartItem.visibility = View.VISIBLE
            } else {
                holder.binding.ibDeleteCartItem.visibility = View.GONE
            }

            holder.binding.tvCartQuantity.text =
                context.resources.getString(R.string.lbl_out_of_stock)

            holder.binding.tvCartQuantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorSnackBarError
                )
            )
        } else {
            if (updateCartItems) {
                holder.binding.ibRemoveCartItem.visibility = View.VISIBLE
                holder.binding.ibAddCartItem.visibility = View.VISIBLE
                holder.binding.ibDeleteCartItem.visibility = View.VISIBLE
            } else {

                holder.binding.ibRemoveCartItem.visibility = View.GONE
                holder.binding.ibDeleteCartItem.visibility = View.GONE
                holder.binding.ibDeleteCartItem.visibility = View.GONE
            }
        }

        holder.binding.ibDeleteCartItem.setOnClickListener {
            when (context) {
                is CartListActivity -> {
                    context.showProgressDialog(context.resources.getString(R.string.please_wait))
                }
            }

            FirestoreClass().removeItemFromCart(context, model.id)
        }

        holder.binding.ibRemoveCartItem.setOnClickListener(){
            if (model.cart_quantity == "1") {
                FirestoreClass().removeItemFromCart(context, model.id)
            } else {

                val cartQuantity: Int = model.cart_quantity.toInt()

                val itemHashMap = HashMap<String, Any>()

                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                // Show the progress dialog.

                if (context is CartListActivity) {
                    context.showProgressDialog(context.resources.getString(R.string.please_wait))
                }

                FirestoreClass().updateMyCart(context, model.id, itemHashMap)
            }
        }

        holder.binding.ibAddCartItem.setOnClickListener(){
            val cartQuantity: Int = model.cart_quantity.toInt()

            if (cartQuantity < model.stock_quantity.toInt()) {

                val itemHashMap = HashMap<String, Any>()

                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                if (context is CartListActivity) {
                    context.showProgressDialog(context.resources.getString(R.string.please_wait))
                }

                FirestoreClass().updateMyCart(context, model.id, itemHashMap)
            } else {
                if (context is CartListActivity) {
                    context.showErrorSnackBar(
                        context.resources.getString(
                            R.string.msg_for_available_stock,
                            model.stock_quantity
                        ),
                        true
                    )
                }
            }
        }
        }

    class MyViewHolder(val binding: ItemCartLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return list.size
    }
}