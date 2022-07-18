package com.ugurcangal.easyfood.retrofit

import com.ugurcangal.easyfood.pojo.CategoryList
import com.ugurcangal.easyfood.pojo.MealsByCategoryList
import com.ugurcangal.easyfood.pojo.MealList
import com.ugurcangal.easyfood.pojo.MealsByCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealAPI {

    //https://www.themealdb.com/api/json/v1/1/random.php
    @GET("random.php")
    fun getRandomMeal() : Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id:String) : Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c")categoryName: String) : Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories() : Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String) : Call<MealsByCategoryList>


}