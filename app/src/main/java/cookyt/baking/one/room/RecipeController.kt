package cookyt.baking.one.room

import android.util.Log
import cookyt.baking.one.model.RecipeRoom

object RecipeController {

    val recipeDao = cookyt.baking.one.App.database.recipeDao()

    fun checkIsFavorite(recipe: RecipeRoom): Boolean {
        val r = recipeDao.getById(recipe.id)
        return r != null
    }

    fun changeFavorite(recipe: RecipeRoom): Boolean {
        cookyt.baking.one.App.makeLog("FAVORITE ${recipe.title}")
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