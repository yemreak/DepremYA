package com.yemreak.depremya.ui

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yemreak.depremya.R
import com.yemreak.depremya.api.KandilliAPI
import com.yemreak.depremya.entity.Earthquake
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	//private var sbMag: SeekBar? = null
	private val MIN_MAG = 0
	private val MAX_MAG = 10
	private var quakes: List<Earthquake> = emptyList()
	private var minMag: Int = 0
	private var maxMag: Int = 0
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		initRecyclerView()
		quake_refresh_layout.setOnRefreshListener {
			initRecyclerView()
		}
	}
	
	private fun initRecyclerView() {
		quake_recycler_view.layoutManager = LinearLayoutManager(this)
		KandilliAPI.requestEarthQuakes(this) {
			quakes = it
			quake_recycler_view.adapter = QuakeAdapter(this, it)
			quake_refresh_layout.isRefreshing = false
		}
	}
	
	
	private fun buildDialog() {
		val dialog: Dialog = Dialog(this)
		
		/*if (option == R.id.mag_filter) {
			dialog.setContentView(R.layout.mag_filter_dialog)
			dialog.btnAcceptMag.setOnClickListener {
				quake_recycler_view.adapter = QuakeAdapter(this, quakes.filter {
					(it.ml.toDouble() >= minMag) && (it.ml.toDouble() <= maxMag)
				})
				dialog.dismiss()
			}
		} */
		dialog.show()
	}
	
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.action_bar, menu)
		return super.onCreateOptionsMenu(menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.menu_item_filter)
			buildDialog()
		return super.onOptionsItemSelected(item)
	}
}
