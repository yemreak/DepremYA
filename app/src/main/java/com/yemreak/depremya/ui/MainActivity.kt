package com.yemreak.depremya.ui

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import kotlinx.android.synthetic.main.notification_dialog.view.*
import kotlinx.android.synthetic.main.urgent_layout.*
import java.util.*

class MainActivity : AppCompatActivity() {
	
	private var quakes: List<Quake> = emptyList()
	private var selectedMag: Int = 0
	private var selectedNotifMag: Int = 0
	private var mainLayout: View? = null
	private var urgentLayout: View? = null
	private lateinit var quakeViewModel: QuakeViewModel
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		mainLayout = layoutInflater.inflate(R.layout.activity_main, null)
		urgentLayout = layoutInflater.inflate(R.layout.urgent_layout, null)
		getData()
	}
	
	private fun getData() {
		quakeViewModel = ViewModelProvider(this).get(QuakeViewModel::class.java)
		quakeViewModel.allQuakes.observe(this, Observer {
			it?.let {
				if (it.isEmpty() && !checkConnection()) {
					// ilk çalıştırma ve internet yok
					setContentView(urgentLayout)
					initUrgentLayout(R.drawable.no_internet, R.string.no_internet)
				} else {
					quakes = it
					setContentView(mainLayout)
					initRecyclerView()
				}
			}
		})
		if (quakes.isEmpty()) refreshData()
	}
	
	private fun refreshData() {
		KandilliAPI.requestEarthQuakes(this) {
			when {
				it == null -> {
					// layout'un değişmesi "Bad behaviour" olabilir, kullanıcı elindeki verileri kaybetmiş gibi hissedecek
					// Toast daha verimli olabilir
					// setContentView(urgentLayout)
					// initUrgentLayout(R.drawable.no_internet, R.string.no_internet)
					Toast.makeText(
						this,
						R.string.conn_fail,
						Toast.LENGTH_SHORT
					).show()
				}
				quakes.isEmpty() || it.first() != quakes.first() -> {
					setContentView(mainLayout)
					initRecyclerView()
					quakeViewModel.refreshQuakes(it)
				}
			}
		}
	}
	
	private fun initRecyclerView() {
		quake_recycler_view.layoutManager = LinearLayoutManager(this)
		quake_recycler_view.adapter = QuakeAdapter(this, quakes)
		quake_refresh_layout.setOnRefreshListener {
			refreshData()
			quake_refresh_layout.isRefreshing = false
		}
	}
	
	private fun initUrgentLayout(id: Int, text: Int) {
		urgent_image?.setImageResource(id)
		urgent_text?.setText(text)
		urgent_refresh_layout.setOnRefreshListener {
			refreshData()
			urgent_refresh_layout.isRefreshing = false
		}
	}
	
	private fun buildFilterDialog() {
		val filterDialog = Dialog(this)
		val view =
			LayoutInflater
				.from(filterDialog.context)
				.inflate(R.layout.filter_dialog, null)
		filterDialog
			.setContentView(view)
		filterDialog.show()
		view.tbgMag.addOnButtonCheckedListener { group, checkedId, isChecked ->
			selectedMag = checkedId
			selectedMag = when (checkedId) {
				R.id.btnGreater0 -> 0
				R.id.btnGreater4 -> 4
				R.id.btnGreater5 -> 5
				R.id.btnGreater6 -> 6
				else -> 0
			}
			val filtered = quakes.filter {
				(it.ml.toDouble() >= selectedMag)
			}
			if (filtered.isEmpty()) {
				setContentView(urgentLayout)
				initUrgentLayout(R.drawable.magnifier, R.string.no_result)
			} else {
				if (quakes.isNotEmpty()) {
					setContentView(mainLayout)
					(quake_recycler_view?.adapter as QuakeAdapter)
						.setQuakesAndNotify(filtered)
					// TODO: 2/2/2020 Asmaa Mirkhan - Bunu kaldırıp kendi arayüzünden alınan limit ile bu metodu çalıştır
					//quakeViewModel.syncData(1, quakes.first())
				}
			}
			filterDialog.dismiss()
		}
	}
	
	private fun buidNotificationDialog() {
		val notifyDialog = Dialog(this)
		val view =
			LayoutInflater
				.from(notifyDialog.context)
				.inflate(R.layout.notification_dialog, null)
		notifyDialog
			.setContentView(view)
		notifyDialog.show()
		view.btgNotif.addOnButtonCheckedListener { group, checkedId, isChecked ->
			selectedNotifMag = when (checkedId) {
				// TODO: geliştirilmelidir, bildirim istememe durumu?
				R.id.btnNoNotif -> 20
				R.id.btnPlus5 -> 5
				R.id.btnPlus6 -> 6
				R.id.btnPlus7 -> 7
				else -> 20
			}
			quakeViewModel.syncData(selectedNotifMag, quakes.first())
			notifyDialog.dismiss()
		}
	}
	
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.action_bar, menu)
		return super.onCreateOptionsMenu(menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.menu_item_filter -> buildFilterDialog()
			R.id.menu_item_notification -> buidNotificationDialog()
		}
		return super.onOptionsItemSelected(item)
	}
	
	private fun checkConnection(): Boolean {
		var connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		var networkInfo =
			Objects.requireNonNull(connManager).getNetworkInfo(ConnectivityManager.TYPE_WIFI)
		var isWifiConn = Objects.requireNonNull(networkInfo).isConnected
		networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
		var isMobileConn = Objects.requireNonNull(networkInfo).isConnected
		return isWifiConn || isMobileConn
	}
}
