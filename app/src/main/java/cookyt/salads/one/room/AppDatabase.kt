package cookyt.salads.one.room

import androidx.room.Database
import androidx.room.RoomDatabase
import cookyt.salads.one.model.RecipeHistory
import cookyt.salads.one.model.RecipeRoom

@Database(
    entities = [RecipeRoom::class, RecipeHistory::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun historyDao(): HistoryDao
}