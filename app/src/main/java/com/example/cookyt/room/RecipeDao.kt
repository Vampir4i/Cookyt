package com.example.cookyt.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.cookyt.model.Recipe
import com.example.cookyt.model.RecipeRoom

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