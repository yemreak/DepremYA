package com.yemreak.depremya.ui

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yemreak.depremya.R
import com.yemreak.depremya.api.KandilliAPI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	
	
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
			quake_recycler_view.adapter = QuakeAdapter(this, it)
			quake_refresh_layout.isRefreshing = false
		}
	}
	
	fun initNavDrawer() {
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
					buildDialog()
					true
				}
				R.id.mag_filter -> {
					buildDialog()
					true
				}
				else -> true
			}
		}
	}
	
	fun buildDialog() {
		val dialog: Dialog = Dialog(this)
		dialog.setContentView(R.layout.nav_header)
		dialog.show()
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
