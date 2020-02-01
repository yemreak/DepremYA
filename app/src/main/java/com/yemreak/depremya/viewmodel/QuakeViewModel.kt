package com.yemreak.depremya.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yemreak.depremya.QuakeRepository
import com.yemreak.depremya.db.QuakeRoom
import com.yemreak.depremya.db.entity.Quake
import kotlinx.coroutines.launch

class QuakeViewModel(application: Application) : AndroidViewModel(application) {
	
	private val repository: QuakeRepository
	
	val allQuakes: LiveData<List<Quake>>
	
	init {
		val quakeDao = QuakeRoom.getDatabase(application.applicationContext).quakeDao()
		repository = QuakeRepository(quakeDao)
		allQuakes = repository.allQuakes.asLiveData()
	}
	
	fun refreshQuakes(quakes: List<Quake>) = viewModelScope.launch {
		repository.deleteAll()
		repository.insert(quakes.toTypedArray())
	}
}
