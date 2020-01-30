package com.yemreak.depremya.ui

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yemreak.depremya.R
import com.yemreak.depremya.api.KandilliAPI
import com.yemreak.depremya.entity.Earthquake
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.mag_filter_dialog.*

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
		initNavDrawer()
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
	
	private fun initNavDrawer() {
		val actionBarDrawerToggle = ActionBarDrawerToggle(
			this, drawerLayout, R.string
				.str_open, R.string.str_close
		)
		actionBarDrawerToggle.isDrawerIndicatorEnabled = true
		
		drawerLayout.addDrawerListener(actionBarDrawerToggle)
		actionBarDrawerToggle.syncState()
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		navView?.setNavigationItemSelectedListener {
			when (it.itemId) {
				R.id.city_filter -> {
					buildDialog(R.id.city_filter)
					true
				}
				R.id.mag_filter -> {
					buildDialog(R.id.mag_filter)
					true
				}
				else -> true
			}
		}
	}
	
	private fun buildDialog(option: Int) {
		val dialog: Dialog = Dialog(this)
		if (option == R.id.mag_filter) {
			dialog.setContentView(R.layout.mag_filter_dialog)
			initNumPickers(dialog)
			dialog.btnAcceptMag.setOnClickListener {
				quake_recycler_view.adapter = QuakeAdapter(this, quakes.filter {
					(it.ml.toDouble() >= minMag) && (it.ml.toDouble() <= maxMag)
				})
				dialog.dismiss()
			}
		}
		dialog.show()
		drawerLayout.closeDrawer(navView)
		
	}
	
	private fun initNumPickers(dialog: Dialog) {
		dialog.npMin.minValue = MIN_MAG
		dialog.npMin.maxValue = MAX_MAG
		dialog.npMax.minValue = MIN_MAG
		dialog.npMax.maxValue = MAX_MAG
		dialog.npMin.setOnValueChangedListener() { picker, oldVal, newVal ->
			minMag = newVal
		}
		dialog.npMax.setOnValueChangedListener() { picker, oldVal, newVal ->
			maxMag = newVal
		}
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val actionBarDrawerToggle = ActionBarDrawerToggle(
			this, drawerLayout, R.string
				.str_open, R.string.str_close
		)
		if (actionBarDrawerToggle.onOptionsItemSelected(item))
			return true
		return super.onOptionsItemSelected(item)
	}
	
}
