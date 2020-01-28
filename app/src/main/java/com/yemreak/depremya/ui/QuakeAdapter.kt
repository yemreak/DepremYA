package com.yemreak.depremya.ui

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.yemreak.depremya.R
import com.yemreak.depremya.entity.EarthQuake
import kotlinx.android.synthetic.main.earthquake_item.view.*

class QuakeAdapter(val quakes: ArrayList<EarthQuake>):
    RecyclerView.Adapter<QuakeAdapter.QuakeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuakeAdapter.QuakeHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.earthquake_item, parent, false)
        return QuakeHolder(view)
    }

    override fun onBindViewHolder(holder: QuakeAdapter.QuakeHolder, position: Int) {
        holder.dateTV.text = quakes[position].date
        holder.hourTV.text = quakes[position].hour
        holder.depthTV.text = quakes[position].depth
        holder.longTV.text = quakes[position].long
        holder.mdTV.text = quakes[position].md
        holder.mlTV.text = quakes[position].ml
        holder.mwTV.text = quakes[position].mw
        holder.placeTV.text = quakes[position].place
        holder.resolutionTV.text = quakes[position].resolution
    }

    override fun getItemCount() = quakes.size

    class QuakeHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dateTV: TextView = itemView.findViewById(R.id.quake_item_date)
        val hourTV: TextView = itemView.findViewById(R.id.quake_item_hour)
        val depthTV: TextView = itemView.findViewById(R.id.quake_item_depth)
        val longTV: TextView = itemView.findViewById(R.id.quake_item_long)
        val mdTV: TextView = itemView.findViewById(R.id.quake_item_md)
        val mlTV: TextView = itemView.findViewById(R.id.quake_item_ml)
        val mwTV: TextView = itemView.findViewById(R.id.quake_item_mw)
        val placeTV: TextView = itemView.findViewById(R.id.quake_item_place)
        val resolutionTV: TextView = itemView.findViewById(R.id.quake_item_resolution)

    }
}