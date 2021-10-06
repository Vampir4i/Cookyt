package cookyt.baking.one.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import cookyt.baking.one.R
import cookyt.baking.one.model.AboutModel
import cookyt.baking.one.view.PoliciesActivity


class AboutListAdapter(
    var values: List<AboutModel>,
    val context: Context
) : RecyclerView.Adapter<AboutListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.about_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle?.text = values[position].title
        holder.tvSub?.text = values[position].sub
        holder.icon?.setImageResource(values[position].icon)
        holder.divider?.visibility = if(values[position].showDivider) View.VISIBLE else View.INVISIBLE

        if(position == values.size - 2) {
            holder.clAbout?.setOnClickListener {
                val packageName = "One+Pro+Studio"

                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=$packageName")))
                } catch (e: ActivityNotFoundException) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=$packageName")))
                }
            }
        }

        if(position == values.size - 1) {
            holder.clAbout?.setOnClickListener {
                val intent = Intent(context, PoliciesActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView? = null
        var tvSub: TextView? = null
        var divider: View? = null
        var icon: ImageView? = null
        var clAbout: ConstraintLayout? = null
        init {
            tvTitle = itemView.findViewById(R.id.title_text)
            tvSub = itemView.findViewById(R.id.sub_text)
            divider = itemView.findViewById(R.id.divider)
            icon = itemView.findViewById(R.id.icon)
            clAbout = itemView.findViewById(R.id.cl_about)
        }
    }
}