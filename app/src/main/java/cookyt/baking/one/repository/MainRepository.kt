package cookyt.baking.one.repository

import cookyt.baking.one.model.Category
import cookyt.baking.one.model.Recipe
import cookyt.baking.one.retrofit.CategoryListRequest
import cookyt.baking.one.retrofit.RetrofitClient
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
        page: String = "1",
        success: (List<Recipe>) -> Unit,
        failure: () -> Unit
    ) {
        RetrofitClient.client.getListRecipes(idCategory, page).enqueue(object: Callback<List<Recipe>> {
            override fun onResponse(
                call: Call<List<Recipe>>,
                response: Response<List<Recipe>>
            ) {
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

    fun getRecipe(
        idRecipe: String,
        success: (Recipe) -> Unit,
        failure: () -> Unit
    ) {
        RetrofitClient.client.getRecipe(idRecipe).enqueue(object: Callback<Recipe> {
            override fun onResponse(
                call: Call<Recipe>,
                response: Response<Recipe>
            ) {
                if(response.code() == 200) {
                    success(response.body() ?: Recipe())
                } else {
                    failure()
                }
            }

            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                failure()
            }

        })
    }

    fun searchRecipes(
        text: String,
        success: (List<Recipe>) -> Unit,
        failure: () -> Unit
    ) {
        if(text.isEmpty()) {
            success(listOf())
            return
        }
        RetrofitClient.client.findRecipes(text).enqueue(object: Callback<List<Recipe>> {
            override fun onResponse(
                call: Call<List<Recipe>>,
                response: Response<List<Recipe>>
            ) {
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

    fun getLastRecipes(
        idCategory: String,
        page: String = "1",
        success: (List<Recipe>) -> Unit,
        failure: () -> Unit
    ) {
        RetrofitClient.client.getLastRecipes(idCategory, page).enqueue(object: Callback<List<Recipe>> {
            override fun onResponse(
                call: Call<List<Recipe>>,
                response: Response<List<Recipe>>
            ) {
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