package com.example.cookyt.retrofit

import com.example.cookyt.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryListRequest(
    @SerializedName("subCategory")
    var categories: List<Category>? = null
)
