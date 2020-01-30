package com.yemreak.depremya.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.yemreak.depremya.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    val SPLASH_TIME = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        runProgressBar()
        val handler = Handler()
        handler.postDelayed(splashHandler, SPLASH_TIME.toLong())
        
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