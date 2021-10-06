package cookyt.baking.one.adapter

import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import cookyt.baking.one.App
import cookyt.baking.one.R
import cookyt.baking.one.model.Recipe
import cookyt.baking.one.room.RecipeController
import cookyt.baking.one.view.RecipeActivity

class RecipesListAdapter(
    var values: List<Recipe>,
    val context: Context,
    val category: String,
    val activity: AppCompatActivity
    ) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    val ADD_FAVORITE = 1
    val SHARE = 2
    var CURRENT_PAGE = 1

    var updateFavorite: (() -> Unit)? = null
    var loadNextPage: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == values.size - 1) loadNextPage?.invoke(++CURRENT_PAGE)
//        if(position == 15 * CURRENT_PAGE) loadNextPage?.invoke(++CURRENT_PAGE)
        val isFavorite = MutableLiveData("")
        Thread {
            val isFav = RecipeController.checkIsFavorite(values[position].getRecipeRoom())
            activity.runOnUiThread {
                isFavorite.value = if(isFav) "Удалить из избранного" else "Добавить в избранное"
            }
        }.start()
        App.loadPhoto(values[position].picture, holder.img)
        holder.title?.text = values[position].title
        holder.category?.text = category
        holder.clRecipe?.setOnClickListener {
            val intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("recipe_id", values[position].id)
            context.startActivity(intent)
        }
        holder.btnMenu?.setOnClickListener {
            val popup = PopupMenu(context, it, Gravity.RIGHT)
            popup.menu.add(Menu.NONE, ADD_FAVORITE, Menu.NONE, isFavorite.value)
//            popup.menu.add(Menu.NONE, SHARE, Menu.NONE, "Поделиться")

            popup.setOnMenuItemClickListener {
                when(it.itemId) {
                    ADD_FAVORITE -> {
                        Thread {
                            RecipeController.changeFavorite(values[position].getRecipeRoom())
                            activity.runOnUiThread {
                                notifyDataSetChanged()
                                updateFavorite?.invoke()
                            }
                        }.start()
                    }
//                    SHARE -> {
//                        val sendIntent: Intent = Intent().apply {
//                            action = Intent.ACTION_SEND
//                            putExtra(Intent.EXTRA_TEXT,
//                                "${values[position].title}\n\n" +
//                                "Я хотел бы поделиться этим с вами. Здесь вы можете загрузить это приложение из PlayStore\n\n" +
//                                        "{ссылка}"
//                            )
//                            type = "text/plain"
//                        }
//                        val shareIntent = Intent.createChooser(sendIntent, null)
//                        context.startActivity(shareIntent)
//                    }
                }
                true
            }

            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    fun updateValues(values: List<Recipe>) {
        this.values = values
        notifyDataSetChanged()
    }

    fun loadNextPage(f: (Int) -> Unit) {
        this.loadNextPage = f
    }

    fun updateFavoriteSet(updateFavorite: () -> Unit) {
        this.updateFavorite = updateFavorite
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView? = null
        var title: TextView? = null
        var category: TextView? = null
        var btnMenu: ImageButton? = null
        var clRecipe: ConstraintLayout? = null

        init {
            img = itemView.findViewById(R.id.iv_recipe_img)
            title = itemView.findViewById(R.id.tv_recipe_title)
            category = itemView.findViewById(R.id.tv_category_title)
            btnMenu = itemView.findViewById(R.id.btn_more)
            clRecipe = itemView.findViewById(R.id.cl_recipe)
        }
    }
}