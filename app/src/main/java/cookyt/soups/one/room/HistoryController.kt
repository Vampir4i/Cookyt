package cookyt.soups.one.room

import cookyt.soups.one.model.RecipeHistory

object HistoryController {

    val historyDao = cookyt.soups.one.App.database.historyDao()

    fun checkInHistory(recipe: RecipeHistory): Boolean {
        val r = historyDao.getById(recipe.id)
        return r != null
    }

    fun changeHistory(recipe: RecipeHistory) {
        val inHis = checkInHistory(recipe)
        if(inHis) historyDao.delete(recipe)
        historyDao.insert(recipe)
        getHistories()
    }

    fun getHistories(): List<RecipeHistory> {
        return historyDao.getAll()
    }
}