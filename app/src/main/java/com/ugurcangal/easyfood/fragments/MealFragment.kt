package com.ugurcangal.easyfood.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ugurcangal.easyfood.R
import com.ugurcangal.easyfood.databinding.FragmentMealBinding
import com.ugurcangal.easyfood.db.MealDatabase
import com.ugurcangal.easyfood.pojo.Meal
import com.ugurcangal.easyfood.viewmodel.MealViewModel
import com.ugurcangal.easyfood.viewmodel.MealViewModelFactory


class MealFragment : Fragment() {

    private var _binding: FragmentMealBinding? = null
    private val binding get() = _binding!!

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private lateinit var mealViewModel: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mealDatabase = MealDatabase.getInstance(context!!.applicationContext)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealViewModel = ViewModelProvider(this,viewModelFactory).get(MealViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMealBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            mealId = MealFragmentArgs.fromBundle(it).mealID
            mealName = MealFragmentArgs.fromBundle(it).mealName
            mealThumb = MealFragmentArgs.fromBundle(it).mealThumb
        }


        mealViewModel.getMealDetail(mealId)

        loadingCase()
        setInformationInViews()
        observerMealDetailsLiveData()
        onYoutubeImageClick()
        onFavoriteClick()


    }

    private fun onFavoriteClick() {
        binding.buttonAddFavorite.setOnClickListener {
            mealToSave?.let { meal ->
                mealViewModel.insertMeal(meal)
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave: Meal? = null
    private fun observerMealDetailsLiveData() {
        mealViewModel.observerMealDetailsLiveData()
            .observe(viewLifecycleOwner, object : Observer<Meal> {
                override fun onChanged(t: Meal?) {
                    onResponseCase()
                    mealToSave = t
                    binding.tvCategory.text = "Category: ${t!!.strCategory}"
                    binding.tvArea.text = "Area : ${t.strArea}"
                    binding.tvInstructionsSteps.text = t.strInstructions
                    youtubeLink = t.strYoutube!!
                }

            })
    }

    private fun setInformationInViews() {
        Glide.with(this@MealFragment)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.buttonAddFavorite.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvInstructionsSteps.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE

    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.buttonAddFavorite.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvInstructionsSteps.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE

    }

}