package cookyt.salads.one.retrofit

import cookyt.salads.one.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Endpoints {
    @GET("api-recipe-category/{id_category}")
    fun getCategories(@Path("id_category") idCategory: String): Call<CategoryListRequest>

    @GET("api-recipe/sub-category/{id_category}/extended/page/{page}")
    fun getListRecipes(
        @Path("id_category") idCategory: String,
        @Path("page") page: String = "1"
    ): Call<List<Recipe>>

    @GET("api-recipe/{id_recipe}/extended")
    fun getRecipe(@Path("id_recipe") idCategory: String): Call<Recipe>

    @GET("api-recipe/v-{category_id}/search")
    fun findRecipes(
        @Path("category_id") categoryId: String,
        @Query("q") text: String
    ): Call<List<Recipe>>

    @GET("api-recipe/category/{id_category}/page/{page}")
    fun getLastRecipes(
        @Path("id_category") idCategory: String,
        @Path("page") page: String = "1"
    ): Call<List<Recipe>>
}