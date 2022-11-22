package com.olvera.uberants.cartModule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.olvera.uberants.R
import com.olvera.uberants.databinding.ItemCartBinding

class CartAdapter(private val products: Array<String>): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productStr = products[position]
        holder.binding.tvName.text = productStr
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding: ItemCartBinding = ItemCartBinding.bind(itemView)
    }
}