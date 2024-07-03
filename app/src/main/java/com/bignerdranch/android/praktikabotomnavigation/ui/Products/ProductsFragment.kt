package com.bignerdranch.android.praktikabotomnavigation.ui.Products

import Models.Product
import adapters.ProductAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.praktikabotomnavigation.databinding.FragmentProductsBinding

class ProductsFragment : Fragment() {

    private lateinit var binding:FragmentProductsBinding
    private lateinit var context:Context
    private lateinit var adapter:ProductAdapter
    private lateinit var products: List<Product>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentProductsBinding.inflate(inflater,container,false)
        products =  listOf(Product(1,"Молоко"), Product(2,"Хлеб"), Product(3,"Батон"),
            Product(4,"Масло"), Product(5,"Сок"), Product(6,"Яблоки"), Product(7,"Мясо"))
        context = this.requireContext()
        adapter = ProductAdapter.create(context)
        binding.rvProducts.layoutManager = LinearLayoutManager(context)
        binding.rvProducts.adapter =adapter
        adapter.refreshProducts(products)
        val dashboardViewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        return binding.root
    }
}