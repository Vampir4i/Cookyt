package com.example.cookyt.repository

import android.util.Log
import com.example.cookyt.App
import com.example.cookyt.model.Category
import com.example.cookyt.model.Recipe
import com.example.cookyt.retrofit.CategoryListRequest
import com.example.cookyt.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainRepository {
    fun getCategories(
        idCategory: String,
        success: (List<Category>) -> Unit,
        failure: () -> Unit
    ) {
        RetrofitClient.client.getCategories(idCategory).enqueue(object: Callback<CategoryListRequest> {
            override fun onResponse(
                call: Call<CategoryListRequest>,
                response: Response<CategoryListRequest>
            ) {
                if(response.code() == 200) {
                    success(response.body()?.categories ?: listOf())
                } else {
                    failure()
                }
            }

            override fun onFailure(call: Call<CategoryListRequest>, t: Throwable) {
                failure()
            }

        })
    }

    fun getRecipes(
        idCategory: String,
        success: (List<Recipe>) -> Unit,
        failure: () -> Unit
    ) {
        RetrofitClient.client.getListRecipes(idCategory).enqueue(object: Callback<List<Recipe>> {
            override fun onResponse(
                call: Call<List<Recipe>>,
                response: Response<List<Recipe>>
            ) {
                App.makeLog("$idCategory ${response.body()?.size} ${response.code()}")
                if(response.code() == 200) {
                    success(response.body() ?: listOf())
                } else {
                    failure()
                }
            }

            override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
                failure()
            }

        })
    }
}