package com.yemreak.depremya.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yemreak.depremya.R
import com.yemreak.depremya.api.KandilliAPI
import com.yemreak.depremya.entity.Earthquake
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.filter_dialog.view.*

class MainActivity : AppCompatActivity() {
	private var quakes: List<Earthquake> = emptyList()
	private var selectedMag: Int = 0
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
	
	private fun buildDialog() {
		val filterDialog = AlertDialog.Builder(this)
		val viewGroup =
			LayoutInflater
				.from(filterDialog.context)
				.inflate(R.layout.filter_dialog, null)
		filterDialog
			.setView(viewGroup)
			.setPositiveButton(R.string.str_show, DialogInterface.OnClickListener() { _, _ ->
				quake_recycler_view.adapter = QuakeAdapter(this, quakes.filter {
					(it.ml.toDouble() >= selectedMag)
				})
			})
			.setTitle(R.string.filter)
			.create()
			.show()
		viewGroup.tbgMag?.addOnButtonCheckedListener { group, checkedId, isChecked ->
			selectedMag = checkedId
			/*var buttonChange = findViewById<MaterialButton>(checkedId)
			viewGroup.findViewById<MaterialButton>(checkedId)
				.setBackgroundColor(resources.getColor(R.color.quakeMax))*/
			selectedMag = when (checkedId) {
				R.id.btnGreater0 -> 0
				R.id.btnGreater4 -> 4
				R.id.btnGreater5 -> 5
				R.id.btnGreater6 -> 6
				else -> 0
			}
		}
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
