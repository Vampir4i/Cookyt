package com.example.cookyt.room

import android.util.Log
import com.example.cookyt.App
import com.example.cookyt.model.RecipeRoom

object RecipeController {

    val recipeDao = App.database.recipeDao()

    fun checkIsFavorite(recipe: RecipeRoom): Boolean {
        val r = recipeDao.getById(recipe.id)
        return r != null
    }

    fun changeFavorite(recipe: RecipeRoom): Boolean {
        Log.d("myLog", "RecipeID ${recipe.id} ${recipe.title}")

        val isFav = checkIsFavorite(recipe)
        if(isFav) recipeDao.delete(recipe)
        else recipeDao.insert(recipe)
        Log.d("myLog", "isFav $isFav")

        return !isFav
    }

    fun getFavorites(): List<RecipeRoom> {
        return recipeDao.getAll()
    }
}