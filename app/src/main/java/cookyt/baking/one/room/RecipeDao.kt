package cookyt.baking.one.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cookyt.baking.one.model.RecipeHistory
import cookyt.baking.one.model.RecipeRoom

@Dao
interface RecipeDao {

    @Query("SELECT * FROM RecipeRoom")
    fun getAll(): List<RecipeRoom>

    @Query("SELECT * FROM RecipeRoom WHERE id = :id")
    fun getById(id: String): RecipeRoom?

    @Insert
    fun insert(recipe: RecipeRoom)

    @Delete
    fun delete(recipe: RecipeRoom)
}

@Dao
interface HistoryDao {

    @Query("SELECT * FROM RecipeHistory")
    fun getAll(): List<RecipeHistory>

    @Query("SELECT * FROM RecipeHistory WHERE id = :id")
    fun getById(id: String): RecipeHistory?

    @Insert
    fun insert(recipe: RecipeHistory)

    @Delete
    fun delete(recipe: RecipeHistory)
}