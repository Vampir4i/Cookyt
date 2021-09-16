package com.example.cookyt.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookyt.App
import com.example.cookyt.model.Category
import com.example.cookyt.model.Recipe
import com.example.cookyt.repository.MainRepository

class MainActivityViewModel: ViewModel() {
    val T_LAST_VIDEOS = 0
    val T_CATEGORY = 1
    val T_FAVORITES = 2
    val T_BACK = 10

    var action = MutableLiveData(T_LAST_VIDEOS)
    var categories = MutableLiveData(listOf<Category>())
    var recipes = MutableLiveData<List<Recipe>>()

    fun getCategories(idCategory: String) {
        MainRepository.getCategories(idCategory, {
            categories.value = it
        }, {

        })
    }

    fun getRecipes(idCategory: String) {
        MainRepository.getRecipes(idCategory, {
            recipes.value = it
        }, {

        })
    }

}