package cookyt.soups.one.retrofit

import cookyt.soups.one.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryListRequest(
    @SerializedName("subCategory")
    var categories: List<Category>? = null
)
