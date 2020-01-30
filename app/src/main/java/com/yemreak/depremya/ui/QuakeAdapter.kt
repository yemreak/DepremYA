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
import com.yemreak.depremya.entity.Earthquake
import kotlinx.android.synthetic.main.quake_item.view.*

class QuakeAdapter(val context: Context, val quakes: List<Earthquake>) :
    RecyclerView.Adapter<QuakeAdapter.QuakeHolder>() {
    val MAX_COLOR = 255
    val MAX_GREEN = 25
    val MAX_BLUE = 25
    val COLOR_FACTOR = 25
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuakeAdapter.QuakeHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.quake_item, parent, false)
        return QuakeHolder(view)
    }

    override fun onBindViewHolder(holder: QuakeAdapter.QuakeHolder, position: Int) {
        holder.tvDate.text = quakes[position].date
        holder.tvHour.text = context.getString(R.string.str_hour, quakes[position].hour)
        holder.tvDepth.text = context.getString(R.string.str_depth, quakes[position].depth)
        holder.tvMd.text = context.getString(R.string.str_md, quakes[position].md)
        holder.tvMl.text = quakes[position].ml
        holder.tvMw.text = context.getString(R.string.str_mw, quakes[position].mw)
        holder.tvCity.text = quakes[position].city
        if (quakes[position].region == "")
            holder.tvRegion.text = context.getString(R.string.str_region, " - ")
        else
            holder.tvRegion.text = context.getString(R.string.str_region, quakes[position].region)
        holder.tvResolution.text =
            context.getString(R.string.str_resolution, quakes[position].resolution)

        holder.ibLocation.setOnClickListener {
            val uri =
                "http://maps.google.com/maps?q=loc:${quakes[position].lat},${quakes[position].long}(${quakes[position].city})"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(context.packageManager) != null)
                context.startActivity(intent)
        }
        holder.tvMl.background.setColorFilter(
            context.resources.getColor(generateMagColor(quakes[position].ml.toDouble())),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun generateMagColor(mag: Double): Int {
        return when {
            mag < 3 -> R.color.quake0_3
            mag >= 3 && mag < 4 -> R.color.quake3_4
            mag >= 4 && mag < 5 -> R.color.quake4_5
            mag >= 5 && mag < 5.5 -> R.color.quake5_5h
            mag >= 5.5 && mag < 6 -> R.color.quake5h_6
            mag >= 6 && mag < 6.5 -> R.color.quake6_6h
            mag >= 6.5 && mag < 7 -> R.color.quake6h_7
            mag >= 7 && mag < 8 -> R.color.quake7_8
            else -> {
                R.color.quakeMax
            }
        }
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
        val tvRegion: TextView = itemView.tvRegion
        val tvResolution: TextView = itemView.tvResolution
        val ibLocation: ImageButton = itemView.ibLocation
    }

}
