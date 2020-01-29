package com.yemreak.depremya.ui

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yemreak.depremya.R
import com.yemreak.depremya.entity.EarthQuake
import kotlinx.android.synthetic.main.quake_item.view.*

class QuakeAdapter(val context: Context, val quakes: List<EarthQuake>) :
    RecyclerView.Adapter<QuakeAdapter.QuakeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuakeAdapter.QuakeHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.quake_item, parent, false)
        return QuakeHolder(view)
    }

    override fun onBindViewHolder(holder: QuakeAdapter.QuakeHolder, position: Int) {
        holder.tvDate.text = quakes[position].date
        holder.tvHour.text = context.getString(R.string.quake_string_hour, quakes[position].hour)
        holder.tvDepth.text = context.getString(R.string.quake_string_depth, quakes[position].depth)
        holder.tvMd.text = context.getString(R.string.quake_string_md, quakes[position].md)
        holder.tvMl.text = quakes[position].ml
        holder.tvMw.text = context.getString(R.string.quake_string_mw, quakes[position].mw)
        holder.tvCity.text = context.getString(R.string.quake_string_place, quakes[position].city)
        holder.tvResolution.text =
            context.getString(R.string.quake_string_resolution, quakes[position].resolution)

        holder.ibLocation.setOnClickListener {
            val uri =
                "http://maps.google.com/maps?q=loc:${quakes[position].lat},${quakes[position].long}(${quakes[position].city})"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(context.packageManager) != null)
                context.startActivity(intent)
        }

        var quakeColor = R.color.lightQuake
        when (quakes[position].ml.toDouble()) {
            in 0..4 -> quakeColor = R.color.lightQuake
            in 4..6 -> quakeColor = R.color.normalQuake
            else -> {
                quakeColor = R.color.heavyQuake
            }
        }

        holder.tvMl.background.setColorFilter(
            context.resources.getColor(quakeColor),
            PorterDuff.Mode.SRC_IN
        )

    }

    override fun getItemCount() = quakes.size

    class QuakeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.tvDate
        val tvHour: TextView = itemView.tvHour
        val tvDepth: TextView = itemView.tvDepth
        val tvMd: TextView = itemView.tvMd
        val tvMl: TextView = itemView.tvMl
        val tvMw: TextView = itemView.tvMw
        val tvCity: TextView = itemView.tvCity
        val tvResolution: TextView = itemView.tvResolution
        val ibLocation: ImageButton = itemView.ibLocation
    }

}