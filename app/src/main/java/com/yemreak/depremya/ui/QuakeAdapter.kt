package com.yemreak.depremya.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yemreak.depremya.R
import com.yemreak.depremya.entity.EarthQuake

class QuakeAdapter(val context: Context, val quakes: List<EarthQuake>) :
    RecyclerView.Adapter<QuakeAdapter.QuakeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuakeAdapter.QuakeHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.quake_item, parent, false)
        return QuakeHolder(view)
    }

    override fun onBindViewHolder(holder: QuakeAdapter.QuakeHolder, position: Int) {
        holder.dateTV.text = quakes[position].date
        holder.hourTV.text = context.getString(R.string.quake_string_hour, quakes[position].hour)
        holder.depthTV.text = context.getString(R.string.quake_string_depth, quakes[position].depth)
        holder.mdTV.text = context.getString(R.string.quake_string_md, quakes[position].md)
        holder.mlTV.text = quakes[position].ml
        holder.mwTV.text = context.getString(R.string.quake_string_mw, quakes[position].mw)
        holder.placeTV.text = context.getString(R.string.quake_string_place, quakes[position].place)
        holder.resolutionTV.text =
            context.getString(R.string.quake_string_resolution, quakes[position].resolution)

        holder.locationImgBtn.setOnClickListener {
            val uri =
                "http://maps.google.com/maps?q=loc:${quakes[position].lat},${quakes[position].long}(${quakes[position].place})"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(context.packageManager) != null)
                context.startActivity(intent)

        }
    }

    override fun getItemCount() = quakes.size

    class QuakeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTV: TextView = itemView.findViewById(R.id.quake_item_date)
        val hourTV: TextView = itemView.findViewById(R.id.quake_item_hour)
        val depthTV: TextView = itemView.findViewById(R.id.quake_item_depth)
        val mdTV: TextView = itemView.findViewById(R.id.quake_item_md)
        val mlTV: TextView = itemView.findViewById(R.id.quake_item_ml)
        val mwTV: TextView = itemView.findViewById(R.id.quake_item_mw)
        val placeTV: TextView = itemView.findViewById(R.id.quake_item_place)
        val resolutionTV: TextView = itemView.findViewById(R.id.quake_item_resolution)
        val locationImgBtn: ImageButton = itemView.findViewById(R.id.quake_item_location)
    }

}