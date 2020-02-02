package com.yemreak.depremya.api

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Bildirim yönetim arayüzü
 */
class NotificationAPI {
	
	companion object {
		
		/**
		 * Bildirim kanalı oluşturma
		 * @see <a href="https://android.yemreak.com/arkaplan/notification">Android ~ YEmreAk</a>
		 * @see <a href="https://developer.android.com/training/notify-user/build-notification#Priority">Create a channel and set the importance</a>
		 */
		@RequiresApi(Build.VERSION_CODES.O)
		fun createNotificationChannel(
			context: Context,
			name: String,
			description: String,
			importance: Int,
			channelId: String,
			lightColor: Int = Color.BLUE,
			lockScreenVisibility: Int = Notification.VISIBILITY_PRIVATE
		
		) {
			val channel = NotificationChannel(channelId, name, importance).apply {
				this.description = description
				this.lightColor = lightColor
				this.lockscreenVisibility = lockScreenVisibility
			}
			
			(context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
				.apply {
					createNotificationChannel(channel)
				}
		}
		
		fun createShowAppPI(
			context: Context,
			clazz: Class<*>,
			requestCode: Int
		): PendingIntent {
			val intent = Intent(context, clazz).apply {
				flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
			}
			
			return PendingIntent.getActivity(
				context,
				requestCode,
				intent,
				PendingIntent.FLAG_UPDATE_CURRENT
			)
		}
	}
}