package com.example.cookyt.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class Recipe(
    var id: String? = null,
    var title: String? = null,
    var picture: String? = null,
    var preview: String? = null,
    var description: String? = null,
    @SerializedName("video_id")
    var videoId: String? = null,
    @SerializedName("channel_id")
    var channelId: String? = null,
    var category: String? = null,
    var sCategory: String? = null,
    @SerializedName("publish_time")
    var publishTime: String? = null,
    var isFavorite: Boolean? = null
) {
    fun getRecipeRoom(): RecipeRoom {
        return RecipeRoom(id ?: "", title ?: "", picture ?: "")
    }
}


@Entity
class RecipeRoom(
    @PrimaryKey
    var id: String,
    var title: String,
    var picture: String
) {
    fun getRecipe(): Recipe = Recipe(id, title, picture)
}