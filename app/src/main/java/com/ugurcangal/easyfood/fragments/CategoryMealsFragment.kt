package com.ugurcangal.easyfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ugurcangal.easyfood.adapters.CategoryMealsAdapter
import com.ugurcangal.easyfood.databinding.FragmentCategoryMealsBinding
import com.ugurcangal.easyfood.viewmodel.CategoryMealsViewModel


class CategoryMealsFragment : Fragment() {
    private var _binding : FragmentCategoryMealsBinding? = null
    private val binding get() = _binding!!

    lateinit var categoryMealsViewModel : CategoryMealsViewModel
    private lateinit var categoryName: String
    lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryMealsBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            categoryName = CategoryMealsFragmentArgs.fromBundle(it).categoryName
        }
        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]
        categoryMealsViewModel.getMealsByCategory(categoryName)

        categoryMealsViewModel.observeMealsLiveData().observe(viewLifecycleOwner, Observer {
            binding.tvCategoryCount.text = it.size.toString()
            categoryMealsAdapter.setMealsList(it)
        })

        prepareRecyclerView()
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.recyclerViewMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoryMealsAdapter
        }
    }


}