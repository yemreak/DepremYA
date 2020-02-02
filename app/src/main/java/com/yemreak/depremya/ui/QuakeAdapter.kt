package com.yemreak.depremya.ui

import android.app.Dialog
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
import kotlinx.android.synthetic.main.detailed_quake.view.*
import kotlinx.android.synthetic.main.quake_item.view.*
import kotlinx.android.synthetic.main.quake_item.view.tvCity
import kotlinx.android.synthetic.main.quake_item.view.tvDate
import kotlinx.android.synthetic.main.quake_item.view.tvHour
import kotlinx.android.synthetic.main.quake_item.view.tvMl
import kotlinx.android.synthetic.main.quake_item.view.tvRegion

class QuakeAdapter(val context: Context, private var quakes: List<Quake>) :
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
			openMaps(holder.ibLocation, lat, lng)
			setMagColor(holder.tvMl, ml.toDouble())
		}
	}

	fun setQuakesAndNotify(quakes: List<Quake>) {
		this.quakes = quakes
		notifyDataSetChanged()
	}


	override fun getItemCount() = quakes.size

	inner class QuakeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val tvDate: TextView = itemView.tvDate
		val tvHour: TextView = itemView.tvHour
		val tvMl: TextView = itemView.tvMl
		val tvCity: TextView = itemView.tvCity
		val tvRegion: TextView = itemView.tvRegion
		val ibLocation: ImageButton = itemView.ibLocation
		val view: View? = null

		init {
			itemView.setOnClickListener {
				val pos = adapterPosition
				val filterDialog = Dialog(it.context)
				val view =
					LayoutInflater
						.from(filterDialog.context)
						.inflate(R.layout.detailed_quake, null)
				filterDialog
					.setContentView(view)
				initDialog(view, pos)
				filterDialog.show()
			}
		}

		private fun initDialog(view: View, pos: Int) {
			with(quakes[pos]) {
				view.tvMl.text = ml
				view.tvHour.text = context.getString(R.string.str_hour, hour)
				view.tvDate.text = date
				view.tvDepth.text = context.getString(R.string.str_depth, depth)
				view.tvResolution.text = context.getString(R.string.str_resolution, resolution)
				view.tvMd.text = context.getString(R.string.str_md, md)
				view.tvMw.text = context.getString(R.string.str_mw, mw)
				view.tvCity.text = city
				if (region == "")
					view.tvRegion.text = context.getString(R.string.str_region, " - ")
				else
					view.tvRegion.text =
						context.getString(R.string.str_region, region)
				openMaps(view.ibLocationDetailed, lat, lng)
				setMagColor(view.tvMl, ml.toDouble())
			}
			setQuakeSender(view.ibShare, quakes[pos])
		}
	}

	fun openMaps(imageButton: ImageButton, lat: String, lng: String) {
		imageButton.setOnClickListener {
			val uri = context.getString(R.string.maps_url, lat, lng, lat, lng)
			val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
			intent.setPackage("com.google.android.apps.maps")
			if (intent.resolveActivity(context.packageManager) != null)
				context.startActivity(intent)
		}
	}

	fun setQuakeSender(imageButton: ImageButton, quake: Quake) {
		imageButton.setOnClickListener {
			val sendIntent: Intent = Intent().setAction(Intent.ACTION_SEND)
			with(quake) {
				sendIntent.putExtra(
					Intent.EXTRA_TEXT,
					context.getString(
						R.string.share_content,
						ml,
						city,
						region,
						depth,
						md,
						mw,
						context.getString(R.string.maps_url, lat, lng, lat, lng)
					)
				)
			}
			sendIntent.type = "text/plain"
			var chooser: Intent = Intent.createChooser(sendIntent, "title")
			if (sendIntent.resolveActivity(context.packageManager) != null)
				context.startActivity(sendIntent)
		}
	}

	private fun setMagColor(textView: TextView, mag: Double) {
		val color = when {
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
		textView.background.setColorFilter(
			context.resources.getColor(color),
			PorterDuff.Mode.SRC_IN
		)
	}
}
