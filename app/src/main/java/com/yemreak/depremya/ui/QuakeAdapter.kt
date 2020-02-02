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
import com.yemreak.depremya.db.entity.Quake
import kotlinx.android.synthetic.main.quake_item.view.*

class QuakeAdapter(private val context: Context, private var quakes: List<Quake>) :
	RecyclerView.Adapter<QuakeAdapter.QuakeHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuakeAdapter.QuakeHolder {
		val view: View =
			LayoutInflater.from(parent.context).inflate(R.layout.quake_item, parent, false)
		return QuakeHolder(view)
	}
	
	override fun onBindViewHolder(holder: QuakeAdapter.QuakeHolder, position: Int) {
		with(quakes[position]) {
			holder.tvDate.text = date
			holder.tvHour.text = context.getString(R.string.str_hour, hour)
			holder.tvMl.text = ml
			holder.tvCity.text = city
			if (region == "")
				holder.tvRegion.text = context.getString(R.string.str_region, " - ")
			else
				holder.tvRegion.text =
					context.getString(R.string.str_region, region)
			
			holder.ibLocation.setOnClickListener {
				val uri = "https://maps.google.com/?q=${lat},${lng}&ll=${lat},${lng}&z=7"
				val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
				intent.setPackage("com.google.android.apps.maps")
				if (intent.resolveActivity(context.packageManager) != null)
					context.startActivity(intent)
			}
			holder.tvMl.background.setColorFilter(
				context.resources.getColor(generateMagColor(ml.toDouble())),
				PorterDuff.Mode.SRC_IN
			)
		}
	}
	
	fun setQuakesAndNotify(quakes: List<Quake>) {
		this.quakes = quakes
		notifyDataSetChanged()
	}
	
	private fun generateMagColor(mag: Double): Int {
		return when {
			mag < 4 -> R.color.quake0_4
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
		val tvMl: TextView = itemView.tvMl
		val tvCity: TextView = itemView.tvCity
		val tvRegion: TextView = itemView.tvRegion
		val ibLocation: ImageButton = itemView.ibLocation
	}
	
}
