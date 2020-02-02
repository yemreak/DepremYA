package com.yemreak.depremya.worker

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yemreak.depremya.R
import com.yemreak.depremya.api.KandilliAPI
import com.yemreak.depremya.api.NotificationAPI
import com.yemreak.depremya.db.entity.Quake
import com.yemreak.depremya.ui.MainActivity
import kotlinx.coroutines.coroutineScope

class SyncCoWorker(private val context: Context, params: WorkerParameters) :
	CoroutineWorker(context, params) {
	
	companion object {
		
		const val KEY_NOTIFY_LIMIT = "notifyLimit"
		const val KEY_QUAKE_DATE = "quakeDate"
		const val KEY_QUAKE_HOUR = "quakeHour"
		const val DEFAULT_LIMIT = 6
		
		private const val CHANNEL_ID = "DepremYa"
		
		private val TAG = SyncCoWorker::class.java.simpleName
	}
	
	override suspend fun doWork(): Result = coroutineScope {
		return@coroutineScope try {
			
			val notifyLimit: Int = inputData.getInt(KEY_NOTIFY_LIMIT, DEFAULT_LIMIT)
			val quakeDate = inputData.getString(KEY_QUAKE_DATE)!!
			val quakeHour = inputData.getString(KEY_QUAKE_HOUR)!!
			
			KandilliAPI.requestEarthQuakes(context) {
				Log.i(TAG, "doWork: Sonuç alındı ${it?.size}")
				if (it == null) throw NullPointerException("API sonuç döndürmedi")
				loop@ for (quake: Quake in it) when {
					quake.isHappenOn(quakeDate, quakeHour) -> break@loop
					quake.ml.toDouble() >= notifyLimit -> notifyUser(quake)
					else -> Log.i(
						TAG,
						"doWork: ${quake.ml.toDouble()} $notifyLimit"
								+ " ${quake.ml.toDouble() >= notifyLimit}"
					)
				}
			}
			
			Result.success()
			
		} catch (throwable: Throwable) {
			Log.e(TAG, "doWork: Hata oluştu", throwable)
			Result.failure()
		}
	}
	
	private fun notifyUser(quake: Quake) {
		with(NotificationManagerCompat.from(context)) {
			notify(0, createNotification(quake))
		}
	}
	
	private fun createNotification(quake: Quake): Notification {
		// TODO: 2/2/2020 Bu işlemin burada olmaması gerekebilir (?)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationAPI.createNotificationChannel(
				context,
				"Deprem",
				"Yüksek şiddetli deprem bildirimleri",
				NotificationManager.IMPORTANCE_DEFAULT,
				CHANNEL_ID
			)
		}
		
		return NotificationCompat.Builder(context, CHANNEL_ID)
			.setSmallIcon(R.mipmap.ic_launcher_foreground)
			.setContentTitle("📍 ${quake.ml} ${quake.city}")
			.setStyle(
				NotificationCompat.BigTextStyle()
					.bigText("${quake.city}'de ${quake.ml} şiddetinde deprem oldu")
			)
			.setPriority(NotificationCompat.PRIORITY_MIN)
			.setCategory(NotificationCompat.CATEGORY_SERVICE)
			.setContentIntent(
				NotificationAPI.createShowAppPI(
					context, MainActivity::class.java,
					0
				)
			)
			.setAutoCancel(true)
			.build()
	}
}
