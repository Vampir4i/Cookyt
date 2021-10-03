package cookyt.baking.one.retrofit

import cookyt.baking.one.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryListRequest(
    @SerializedName("subCategory")
    var categories: List<Category>? = null
)
