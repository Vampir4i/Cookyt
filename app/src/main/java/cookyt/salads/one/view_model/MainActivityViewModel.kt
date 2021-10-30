package cookyt.salads.one.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cookyt.salads.one.App
import cookyt.salads.one.model.Category
import cookyt.salads.one.model.Recipe
import cookyt.salads.one.repository.MainRepository

class MainActivityViewModel: ViewModel() {
    val T_LAST_VIDEOS = 0
    val T_CATEGORY = 1
    val T_FAVORITES = 2
    val T_HISTORY = 3

    val T_RATE = 4
    val T_MORE = 5
    val T_SHARE = 6
    val T_ABOUT = 7
    val T_POLICIES = 8
    val T_BACK = 10

    var action = MutableLiveData(T_LAST_VIDEOS)
    var categories = MutableLiveData(listOf<Category>())
    var recipes = MutableLiveData<MutableList<Recipe>>(mutableListOf())
    var recipe = MutableLiveData<Recipe>()
    var searchRecipes = MutableLiveData<List<Recipe>>()
    var lastRecipes = MutableLiveData<MutableList<Recipe>>(mutableListOf())

    var showInter = true

    fun getCategories(idCategory: String) {
        MainRepository.getCategories(idCategory, {
            categories.value = it
        }, {

        })
    }

    fun getRecipes(idCategory: String, page: String = "1") {
        MainRepository.getRecipes(idCategory, page, {
            val l = recipes.value
            l?.addAll(it)
            recipes.value = l ?: mutableListOf()
        }, {

        })
    }

    fun getRecipe(recipeId: String) {
        MainRepository.getRecipe(recipeId, {
            recipe.value = it
        }, {

        })
    }

    fun searchRecipe(text: String) {
        MainRepository.searchRecipes(text, {
            searchRecipes.value = it
        }, {})
    }

    fun getLastRecipes(idCategory: String, page: String = "1") {
        MainRepository.getLastRecipes(idCategory, page, {
            if(it.isNotEmpty()) {
                val l = lastRecipes.value
                l?.addAll(it)
                lastRecipes.value = l ?: mutableListOf()
            }
        }, {

        })
    }
}