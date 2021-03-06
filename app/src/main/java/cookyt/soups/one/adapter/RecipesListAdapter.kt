package cookyt.soups.one.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import cookyt.soups.one.App
import cookyt.soups.one.R
import cookyt.soups.one.model.Recipe
import cookyt.soups.one.room.RecipeController
import cookyt.soups.one.view.RecipeActivity

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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == values.size - 1) loadNextPage?.invoke(++CURRENT_PAGE)
//        if(position == 15 * CURRENT_PAGE) loadNextPage?.invoke(++CURRENT_PAGE)
        val isFavorite = MutableLiveData<Boolean>()
        Thread {
            val isFav = RecipeController.checkIsFavorite(values[position].getRecipeRoom())
            activity.runOnUiThread {
                isFavorite.value = isFav
//                isFavorite.value = if(isFav) "Удалить из избранного" else "Добавить в избранное"
            }
        }.start()
        App.loadPhoto(values[position].picture, holder.img)
        holder.title?.text = values[position].title
        holder.category?.text = category
        if(values[position].channelName.isNullOrEmpty())
            holder.channel?.visibility = View.GONE
        holder.channel?.text = values[position].channelName
        holder.clRecipe?.setOnClickListener {
            val intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("recipe_id", values[position].id)
            context.startActivity(intent)
        }
        isFavorite.observe(activity, {
            holder.btnMenu?.setImageResource(
                if(isFavorite.value == true) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24)
            holder.btnMenu?.setColorFilter(
                App.context.getColor(
                    if(isFavorite.value == true) R.color.main_light_color else R.color.light_grey
                ), android.graphics.PorterDuff.Mode.SRC_IN
            );
        })
        holder.btnMenu?.setOnClickListener {
            Thread {
                RecipeController.changeFavorite(values[position].getRecipeRoom())
                activity.runOnUiThread {
                    notifyDataSetChanged()
                    updateFavorite?.invoke()
                }
            }.start()
        }
//        holder.btnMenu?.setOnClickListener {
//            val popup = PopupMenu(context, it, Gravity.RIGHT)
//            popup.menu.add(Menu.NONE, ADD_FAVORITE, Menu.NONE, isFavorite.value)
////            popup.menu.add(Menu.NONE, SHARE, Menu.NONE, "Поделиться")
//
//            popup.setOnMenuItemClickListener {
//                when(it.itemId) {
//                    ADD_FAVORITE -> {
//                        Thread {
//                            RecipeController.changeFavorite(values[position].getRecipeRoom())
//                            activity.runOnUiThread {
//                                notifyDataSetChanged()
//                                updateFavorite?.invoke()
//                            }
//                        }.start()
//                    }
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
//                }
//                true
//            }
//
//            popup.show()
//        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    fun updateValues(values: List<Recipe>) {
        App.makeLog("UPDATE ${values.size} ${this.values.size}")
        values.forEach { App.makeLog(it.description ?: "") }
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
        var channel: TextView? = null
        var btnMenu: ImageButton? = null
        var clRecipe: ConstraintLayout? = null

        init {
            img = itemView.findViewById(R.id.iv_recipe_img)
            title = itemView.findViewById(R.id.tv_recipe_title)
            category = itemView.findViewById(R.id.tv_category_title)
            channel = itemView.findViewById(R.id.tv_channel)
            btnMenu = itemView.findViewById(R.id.btn_favorite)
            clRecipe = itemView.findViewById(R.id.cl_recipe)
        }
    }
}