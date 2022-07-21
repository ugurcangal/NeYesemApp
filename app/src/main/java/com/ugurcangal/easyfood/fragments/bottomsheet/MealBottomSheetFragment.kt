package com.ugurcangal.easyfood.fragments.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ugurcangal.easyfood.R
import com.ugurcangal.easyfood.activities.MainActivity
import com.ugurcangal.easyfood.databinding.FragmentMealBottomSheetBinding
import com.ugurcangal.easyfood.fragments.HomeFragmentDirections
import com.ugurcangal.easyfood.viewmodel.HomeViewModel

private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentMealBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var mealId: String? = null
    private lateinit var viewModel : HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }

        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMealBottomSheetBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            viewModel.getMealById(it)
        }

        observeBottomSheetMeal()
        onBottomSheetDialogClick(view)

    }



    private fun onBottomSheetDialogClick(view: View) {
        binding.bottomSheet.setOnClickListener {
            if (mealId != null && mealName != null && mealThumb != null){
                val action = HomeFragmentDirections.actionHomeFragmentToMealFragment()
                action.mealID = mealId!!
                action.mealName = mealName!!
                action.mealThumb = mealThumb!!
                findNavController().navigate(action)
                dialog?.dismiss()
            }else{
                dismiss()
            }
        }
    }

    private var mealName: String? = null
    private var mealThumb: String? = null

    private fun observeBottomSheetMeal() {
        viewModel.observeBottomShetMeal().observe(viewLifecycleOwner, Observer {
            Glide.with(this).load(it.strMealThumb).into(binding.imgBottomSheet)
            binding.tvBottomSheetArea.text = it.strArea
            binding.tvBottomSheetCategory.text = it.strCategory
            binding.tvBottomSheetMealName.text = it.strMeal

            mealName = it.strMeal
            mealThumb = it.strMealThumb
        })
    }

    companion object{

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }


}