package cookyt.salads.one.retrofit

import cookyt.salads.one.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryListRequest(
    @SerializedName("subCategory")
    var categories: List<Category>? = null
)
