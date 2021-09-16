package com.example.cookyt.retrofit

import com.example.cookyt.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Endpoints {
    @GET("api-recipe-category/{id_category}")
    fun getCategories(@Path("id_category") idCategory: String): Call<CategoryListRequest>

    @GET("api-recipe/sub-category/{id_category}")
    fun getListRecipes(@Path("id_category") idCategory: String): Call<List<Recipe>>

    @GET("api-recipe/sub-category/{id_recipe}")
    fun getRecipes(@Path("id_recipe") idCategory: String): Call<Recipe>

    @GET("api-recipe/sub-category/search")
    fun findRecipes(@Query("q") text: String): Call<List<Recipe>>
}