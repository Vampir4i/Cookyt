package com.example.cookyt.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cookyt.model.Recipe
import com.example.cookyt.model.RecipeRoom

@Database(entities = [RecipeRoom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}