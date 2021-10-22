package cookyt.salads.one.room

import android.util.Log
import cookyt.salads.one.App
import cookyt.salads.one.model.RecipeRoom

object RecipeController {

    val recipeDao = App.database.recipeDao()

    fun checkIsFavorite(recipe: RecipeRoom): Boolean {
        val r = recipeDao.getById(recipe.id)
        return r != null
    }

    fun changeFavorite(recipe: RecipeRoom): Boolean {
        val isFav = checkIsFavorite(recipe)
        if(isFav) recipeDao.delete(recipe)
        else recipeDao.insert(recipe)

        return !isFav
    }

    fun getFavorites(): List<RecipeRoom> {
        return recipeDao.getAll()
    }
}