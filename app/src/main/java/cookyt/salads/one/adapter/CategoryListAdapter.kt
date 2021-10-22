package cookyt.salads.one.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import cookyt.salads.one.R
import cookyt.salads.one.model.Category
import cookyt.salads.one.view.RecipesActivity

class CategoryListAdapter(
    var values: List<Category>,
    val context: Context
) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle?.text = values[position].title
        holder.clCategory?.setOnClickListener {
            val intent = Intent(context, RecipesActivity::class.java)
            intent.putExtra("category_id", values[position].id)
            intent.putExtra("category", values[position].title)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    fun updateValues(values: List<Category>) {
        this.values = values
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView? = null
        var clCategory: ConstraintLayout? = null
        init {
            tvTitle = itemView.findViewById(R.id.tv_category_title)
            clCategory = itemView.findViewById(R.id.cl_category)
        }
    }
}