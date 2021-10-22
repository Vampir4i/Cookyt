package cookyt.salads.one.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cookyt.salads.one.R
import cookyt.salads.one.model.NavigationItemModel

class NavigationRVAdapter(private var items: List<NavigationItemModel>, private var currentPos: Int)
    : RecyclerView.Adapter<NavigationRVAdapter.NavigationItemViewHolder>(){
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationItemViewHolder {
        context = parent.context
        val navItem = LayoutInflater.from(parent.context).inflate(R.layout.row_nav_drawer, parent, false)
        return NavigationItemViewHolder(navItem)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: NavigationItemViewHolder, position: Int) {
        if(position == currentPos)
            holder.itemView.setBackgroundColor(context.resources.getColor(R.color.main_light_color))
        else holder.itemView.setBackgroundColor(context.resources.getColor(android.R.color.transparent))

        holder.title.text = items[position].title
        holder.icon.setImageResource(items[position].icon)
    }

    class NavigationItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var icon: ImageView

        init {
            title = itemView.findViewById(R.id.navigation_title)
            icon = itemView.findViewById(R.id.navigation_icon)
        }
    }
}