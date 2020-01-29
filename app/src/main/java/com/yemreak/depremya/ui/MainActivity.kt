package com.yemreak.depremya.ui

import android.os.Bundle
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

}