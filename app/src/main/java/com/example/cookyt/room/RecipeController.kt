package com.example.cookyt.room

import com.example.cookyt.App
import com.example.cookyt.model.RecipeRoom

object RecipeController {

    val recipeDao = App.database.recipeDao()

    fun checkIsFavorite(recipe: RecipeRoom): Boolean {
        val r = recipeDao.getById(recipe.id)
        return r != null
    }

    fun changeFavorite(recipe: RecipeRoom) {
        if(checkIsFavorite(recipe)) recipeDao.delete(recipe)
        else recipeDao.insert(recipe)
    }

    fun getFavorites(): List<RecipeRoom> {
        return recipeDao.getAll()
    }
}