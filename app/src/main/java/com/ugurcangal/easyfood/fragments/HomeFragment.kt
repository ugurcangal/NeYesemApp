package com.ugurcangal.easyfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ugurcangal.easyfood.activities.MainActivity
import com.ugurcangal.easyfood.adapters.CategoriesAdapter
import com.ugurcangal.easyfood.adapters.MostPopularAdapter
import com.ugurcangal.easyfood.databinding.FragmentHomeBinding
import com.ugurcangal.easyfood.pojo.MealsByCategory
import com.ugurcangal.easyfood.pojo.Meal
import com.ugurcangal.easyfood.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal : Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesItemsAdapter: CategoriesAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick(view)

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick(view)

    }

    private fun onCategoryClick(view: View) {
        categoriesItemsAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToCategoryMealsFragment()
            action.categoryName = it.strCategory
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.recyclerViewCategories.apply {
            categoriesItemsAdapter = CategoriesAdapter()
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesItemsAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer {

                categoriesItemsAdapter.setCategoryList(it)

        })
    }

    private fun onPopularItemClick(view: View) {
        popularItemsAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToMealFragment()
            action.mealID = it.idMeal
            action.mealName = it.strMeal
            action.mealThumb = it.strMealThumb
            Navigation.findNavController(view).navigate(action)

        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recyclerViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToMealFragment()
            action.mealID = randomMeal.idMeal
            action.mealName = randomMeal.strMeal!!
            action.mealThumb = randomMeal.strMealThumb!!
            Navigation.findNavController(it).navigate(action)


        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLivedata().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = meal
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}