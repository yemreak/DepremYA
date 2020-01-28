package com.yemreak.depremya.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yemreak.depremya.R
import com.yemreak.depremya.api.KandilliAPI
import com.yemreak.depremya.entity.EarthQuake
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.annotations.TestOnly

class MainActivity : AppCompatActivity() {
    //val quakes = List<EarthQuake>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        quake_recycler_view.layoutManager = LinearLayoutManager(this)
        KandilliAPI.requestEarthQuakes(this) {
            quake_recycler_view.adapter = QuakeAdapter(it)
        }
    }

    @TestOnly
    private fun getData(): List<EarthQuake> {
        val quake = EarthQuake(
            "2020.01.28",
            "21:31:32",
            "39.0787",
            "27.8380",
            "5.4",
            "-.-",
            "0.5",
            "-.-",
            "ILYASLAR-KIRKAGAC (MANISA)",
            "İlksel"
        )
        val quakes: List<EarthQuake> = arrayListOf<EarthQuake>().apply {
            add(quake)
            add(quake)
            add(quake)
            add(quake)
            add(quake)
        }
        return quakes
    }

}