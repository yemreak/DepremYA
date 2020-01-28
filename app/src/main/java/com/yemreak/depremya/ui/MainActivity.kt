package com.yemreak.depremya.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.yemreak.depremya.R
import com.yemreak.depremya.entity.EarthQuake
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	val quakes = ArrayList<EarthQuake>()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		quake_recycler_view.layoutManager = LinearLayoutManager(this)
		getData()
		quake_recycler_view.adapter = QuakeAdapter(quakes)
	}

	fun getData() {
		val quake = EarthQuake("2020.01.28", "21:31:32", "39.0787", "27.8380", "5.4", "-.-",  "0.5",  "-.-", "ILYASLAR-KIRKAGAC (MANISA)","İlksel")
		quakes.add(quake)
		quakes.add(quake)
		quakes.add(quake)
		quakes.add(quake)
		quakes.add(quake)
		quakes.add(quake)
		quakes.add(quake)

	}

}