package com.yemreak.depremya.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yemreak.depremya.Globals
import com.yemreak.depremya.R
import com.yemreak.depremya.api.KandilliAPI
import com.yemreak.depremya.db.entity.Quake
import com.yemreak.depremya.viewmodel.QuakeViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
	val SPLASH_TIME = 2000
	private var quakes: List<Quake> = emptyList()
	private lateinit var quakeViewModel: QuakeViewModel
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)
		supportActionBar?.hide()
		runProgressBar()
		val handler = Handler()
		handler.postDelayed(splashHandler, SPLASH_TIME.toLong())
		quakeViewModel = ViewModelProvider(this).get(QuakeViewModel::class.java)
		quakeViewModel.allQuakes.observe(this, Observer {
			it?.let {
				quakes = it
				requestData()
			}
		})
	}
	
	private fun requestData() {
		KandilliAPI.requestEarthQuakes(this) {
			if (it != null) {
				if (quakes.isEmpty() || it.first() != quakes.first()) {
					Globals.quakes = it
					quakeViewModel.refreshQuakes(it)
				}
			}
		}
	}
	
	internal var splashHandler: Runnable = Runnable {
		val intent = Intent(this, MainActivity::class.java)
		startActivity(intent)
		finish()
	}
	
	fun runProgressBar() {
		ObjectAnimator.ofInt(pbSplash, "progress", 100).setDuration(SPLASH_TIME.toLong())
			.start()
	}
}
