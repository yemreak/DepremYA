package com.yemreak.depremya.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yemreak.depremya.api.KandilliAPI
import com.yemreak.depremya.db.entity.Quake
import kotlinx.coroutines.coroutineScope

class SyncCoWorker(
	private val context: Context,
	params: WorkerParameters,
	private var lastQuake: Quake,
	private var notifyLimit: Float
) :
	CoroutineWorker(context, params) {
	
	companion object {
		
		private val TAG = SyncCoWorker::class.java.simpleName
		
	}
	
	override suspend fun doWork(): Result = coroutineScope {
		return@coroutineScope try {
			KandilliAPI.requestEarthQuakes(context) {
				if (it == null) return@requestEarthQuakes
				loop@ for (quake: Quake in it) {
					when (quake) {
						lastQuake -> {
							lastQuake = it.first()
							break@loop
						}
						else -> if (quake.ml.toDouble() >= notifyLimit) notifyUser(quake)
					}
				}
			}
			Result.success()
		} catch (throwable: Throwable) {
			Log.e(TAG, "doWork: Hata oluştu", throwable)
			Result.failure()
		}
	}
	
	private fun notifyUser(quake: Quake) {
		TODO("Kullanıcıya bildirim gönderilmeli")
	}
}
