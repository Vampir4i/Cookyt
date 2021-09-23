package com.example.cookyt.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cookyt.model.Recipe
import com.example.cookyt.model.RecipeHistory
import com.example.cookyt.model.RecipeRoom

@Database(
    entities = [RecipeRoom::class, RecipeHistory::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun historyDao(): HistoryDao
}