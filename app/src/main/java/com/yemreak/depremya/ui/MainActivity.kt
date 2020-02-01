package com.yemreak.depremya.ui

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yemreak.depremya.R
import com.yemreak.depremya.api.KandilliAPI
import com.yemreak.depremya.entity.Quake
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.filter_dialog.view.*
import kotlinx.android.synthetic.main.urgent_layout.*
import java.util.*

class MainActivity : AppCompatActivity() {
	private var quakes: List<Quake> = emptyList()
	private var selectedMag: Int = 0
	private var mainLayout: View? = null
	private var urgentLayout: View? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mainLayout = layoutInflater.inflate(R.layout.activity_main, null)
		urgentLayout = layoutInflater.inflate(R.layout.urgent_layout, null)
		chooseLayout()
	}
	
	private fun chooseLayout() {
		if (checkConnection()) {
			setContentView(mainLayout)
			initRecyclerView()
			quake_refresh_layout.setOnRefreshListener {
				chooseLayout()
			}
		} else {
			setContentView(urgentLayout)
			initUrgentLayout(R.drawable.no_internet, R.string.no_internet)
			urgent_refresh_layout.isRefreshing = false
			urgent_refresh_layout.setOnRefreshListener {
				chooseLayout()
			}
		}
	}
	
	private fun initUrgentLayout(id: Int, text: Int) {
		urgent_image?.setImageResource(id)
		urgent_text?.setText(text)
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
			quake_recycler_view.adapter = QuakeAdapter(this, quakes.filter {
				(it.ml.toDouble() >= selectedMag)
			})
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
	
	fun checkConnection(): Boolean {
		var connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		var networkInfo =
			Objects.requireNonNull(connManager).getNetworkInfo(ConnectivityManager.TYPE_WIFI)
		var isWifiConn = Objects.requireNonNull(networkInfo).isConnected
		networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
		var isMobileConn = Objects.requireNonNull(networkInfo).isConnected
		return isWifiConn || isMobileConn
	}
}
