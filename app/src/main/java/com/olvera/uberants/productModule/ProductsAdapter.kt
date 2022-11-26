package com.olvera.uberants.productModule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.olvera.uberants.R
import com.olvera.uberants.common.entities.Product
import com.olvera.uberants.databinding.ItemProductBinding

class ProductsAdapter(private val products: List<Product>, private val listener: OnClickListener) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]

        holder.setListener(product, listener)
        holder.binding.tvName.text = product.name

        Glide.with(context)
            .load(product.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions().centerCrop())
            .into(holder.binding.imgPhoto)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemProductBinding.bind(itemView)

        fun setListener(product: Product, listener: OnClickListener) {
            binding.root.setOnClickListener {
                product.isSelected = !product.isSelected
                binding.root.isChecked = product.isSelected
                listener.onClick(product)
            }
        }
    }
}