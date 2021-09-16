package com.example.cookyt.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookyt.R
import com.example.cookyt.model.Category
import com.example.cookyt.view.RecipesActivity

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
        holder.tvTitle?.setOnClickListener {
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

        init {
            tvTitle = itemView.findViewById(R.id.tv_category_title)
        }
    }
}