package cookyt.soups.one.room

import cookyt.soups.one.App
import cookyt.soups.one.model.RecipeRoom

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