package com.yemreak.depremya.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.yemreak.depremya.QuakeRepository
import com.yemreak.depremya.db.QuakeRoom
import com.yemreak.depremya.db.entity.Quake
import com.yemreak.depremya.worker.SyncCoWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class QuakeViewModel(application: Application) : AndroidViewModel(application) {
	
	companion object {
		
		/**
		 * **Her 30dk içerisinde** amacıyla kullanılır
		 */
		private const val NOTIFY_PERIODIC_INTERVAL_MINUTE: Long = 30
		
		private val TAG = QuakeViewModel::class.java.simpleName
		
	}
	
	private val repository: QuakeRepository
	
	val allQuakes: LiveData<List<Quake>>
	
	private val workManager: WorkManager
	
	init {
		val quakeDao = QuakeRoom.getDatabase(application.applicationContext).quakeDao()
		repository = QuakeRepository(quakeDao)
		allQuakes = repository.allQuakes.asLiveData()
		
		workManager = WorkManager.getInstance(application)
	}
	
	fun refreshQuakes(quakes: List<Quake>) = viewModelScope.launch {
		repository.deleteAll()
		repository.insert(quakes.toTypedArray())
	}
	
	fun stopSync() {
		workManager.cancelAllWork()
	}
	
	
	/**
	 * Arka planda depremleri verilen [intervalTime] ya da [NOTIFY_PERIODIC_INTERVAL_MINUTE] sıklığında takip eder.
	 * Şiddeti [notifyLimit]'den yüksek olan depremler hakkında bildirim verir.
	 * Zaten görülen depremler hakkında bildirim vermemek için [lastQuake] bilgisini alır.
	 */
	fun syncData(
		notifyLimit: Int,
		lastQuake: Quake,
		intervalTime: Long = NOTIFY_PERIODIC_INTERVAL_MINUTE
	) {
		Log.i(TAG, "syncData: Deprem takim işçisi aktif ediliyor")
		
		workManager.enqueue(
			PeriodicWorkRequest.Builder(
				SyncCoWorker::class.java,
				intervalTime,
				TimeUnit.MILLISECONDS,
				intervalTime / 2,
				TimeUnit.MINUTES
			)
				.setInputData(
					Data.Builder()
						.putInt(SyncCoWorker.KEY_NOTIFY_LIMIT, notifyLimit)
						.putString(SyncCoWorker.KEY_QUAKE_DATE, lastQuake.date)
						.putString(SyncCoWorker.KEY_QUAKE_HOUR, lastQuake.hour)
						.build()
				)
				.build()
		)
	}
}
