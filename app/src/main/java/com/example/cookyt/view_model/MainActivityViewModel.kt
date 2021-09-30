package com.example.cookyt.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookyt.model.Category
import com.example.cookyt.model.Recipe
import com.example.cookyt.repository.MainRepository

class MainActivityViewModel: ViewModel() {
    val T_LAST_VIDEOS = 0
    val T_CATEGORY = 1
    val T_FAVORITES = 2
    val T_HISTORY = 3
    val T_BACK = 10

    var action = MutableLiveData(T_LAST_VIDEOS)
    var categories = MutableLiveData(listOf<Category>())
    var recipes = MutableLiveData<MutableList<Recipe>>(mutableListOf())
    var recipe = MutableLiveData<Recipe>()
    var searchRecipes = MutableLiveData<List<Recipe>>()
    var lastRecipes = MutableLiveData<MutableList<Recipe>>(mutableListOf())

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
            val l = lastRecipes.value
            l?.addAll(it)
            lastRecipes.value = l ?: mutableListOf()
        }, {

        })
    }
}