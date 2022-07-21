package com.ugurcangal.easyfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ugurcangal.easyfood.R
import com.ugurcangal.easyfood.activities.MainActivity
import com.ugurcangal.easyfood.adapters.CategoriesAdapter
import com.ugurcangal.easyfood.databinding.FragmentCategoriesBinding
import com.ugurcangal.easyfood.viewmodel.HomeViewModel


class CategoriesFragment : Fragment() {

    private var _binding : FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel : HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoriesBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeCategories()
        onItemClick(view)
    }

    private fun onItemClick(view: View) {
        categoriesAdapter.onItemClick = {
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToCategoryMealsFragment()
            action.categoryName = it.strCategory
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun observeCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer {
            categoriesAdapter.setCategoryList(it)
        })
    }

    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }
}