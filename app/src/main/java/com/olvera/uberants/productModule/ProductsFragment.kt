package com.olvera.uberants.productModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.olvera.uberants.common.dataAccess.FakeDatabase
import com.olvera.uberants.common.entities.Product
import com.olvera.uberants.databinding.FragmentProductsBinding


class ProductsFragment : Fragment(), OnClickListener {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val selectedProducts: MutableList<Product?> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProductsBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val products = FakeDatabase.getProducts()

        setupRecyclerView(products)
        setupButtons()
    }

    private fun setupRecyclerView(products: List<Product>) {
        val productsAdapter = ProductsAdapter(products, this)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productsAdapter
        }
    }

    private fun setupButtons(){
        binding.btnAddCar.setOnClickListener {
            val action = ProductsFragmentDirections.actionProductsToCar()
            action.products = productsStr
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val productsStr: Array<String?>
        get() {
            val productsStr = arrayOfNulls<String>(selectedProducts.size)
            var index = 0
            for (product in selectedProducts) {
                productsStr[index] = product?.name
                index++
            }
            return productsStr
        }

    override fun onClick(product: Product) {
        if (product.isSelected) {
            selectedProducts.add(product)
        } else {
            selectedProducts.remove(product)
        }
    }
}