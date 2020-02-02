package com.yemreak.depremya.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yemreak.depremya.R
import com.yemreak.depremya.api.KandilliAPI
import com.yemreak.depremya.viewmodel.QuakeViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
	
	private val splashTime = 2000
	
	private lateinit var quakeViewModel: QuakeViewModel
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)
		supportActionBar?.hide()
		
		runProgressBar()
		
		val handler = Handler()
		handler.postDelayed(splashHandler, splashTime.toLong())
		
		quakeViewModel = ViewModelProvider(this).get(QuakeViewModel::class.java)
		quakeViewModel.allQuakes.observe(this, Observer {
			KandilliAPI.requestEarthQuakes(this) { quakes ->
				if (quakes != null) {
					if (it.isEmpty() || it.first() != quakes.first())
						quakeViewModel.refreshQuakes(quakes)
				}
			}
		})
	}
	
	private var splashHandler: Runnable = Runnable {
		val intent = Intent(this, MainActivity::class.java)
		startActivity(intent)
		finish()
	}
	
	private fun runProgressBar() {
		ObjectAnimator.ofInt(pbSplash, "progress", 100).setDuration(splashTime.toLong())
			.start()
	}
}
