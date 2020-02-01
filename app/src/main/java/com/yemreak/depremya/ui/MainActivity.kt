package com.yemreak.depremya.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yemreak.depremya.R
import com.yemreak.depremya.api.KandilliAPI
import com.yemreak.depremya.db.entity.Quake
import com.yemreak.depremya.viewmodel.QuakeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.filter_dialog.view.*

class MainActivity : AppCompatActivity() {
	
	private var quakes: List<Quake> = emptyList()
	private var selectedMag: Int = 0
	
	private lateinit var quakeViewModel: QuakeViewModel
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		setContentView(R.layout.activity_main)
		initRecyclerView()
		
		quakeViewModel = ViewModelProvider(this).get(QuakeViewModel::class.java)
		quakeViewModel.allQuakes.observe(this, Observer {
			it?.let {
				quakes = it
				(quake_recycler_view.adapter as QuakeAdapter).setQuakesAndNotify(quakes)
			}
		})
		
		quake_refresh_layout.setOnRefreshListener {
			KandilliAPI.requestEarthQuakes(this) {
				when {
					it == null -> Toast.makeText(
						this,
						"Bağlantı başarısız",
						Toast.LENGTH_SHORT
					).show()
					quakes.isEmpty() || it.first() != quakes.first() ->
						quakeViewModel.refreshQuakes(it)
				}
				quake_refresh_layout.isRefreshing = false
			}
		}
	}
	
	private fun initRecyclerView() {
		quake_recycler_view.layoutManager = LinearLayoutManager(this)
		quake_recycler_view.adapter = QuakeAdapter(this, quakes)
	}
	
	private fun buildDialog() {
		val filterDialog = Dialog(this)
		val viewGroup =
			LayoutInflater
				.from(filterDialog.context)
				.inflate(R.layout.filter_dialog, null)
		filterDialog
			.setContentView(viewGroup)
		filterDialog.show()
		viewGroup.tbgMag.addOnButtonCheckedListener { group, checkedId, isChecked ->
			selectedMag = checkedId
			selectedMag = when (checkedId) {
				R.id.btnGreater0 -> 0
				R.id.btnGreater4 -> 4
				R.id.btnGreater5 -> 5
				R.id.btnGreater6 -> 6
				else -> 0
			}
			(quake_recycler_view.adapter as QuakeAdapter)
				.setQuakesAndNotify(quakes.filter { it.ml.toDouble() >= selectedMag })
			filterDialog.dismiss()
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
