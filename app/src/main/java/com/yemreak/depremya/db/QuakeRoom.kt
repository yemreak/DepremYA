package com.yemreak.depremya.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yemreak.depremya.db.dao.QuakeDao
import com.yemreak.depremya.db.entity.Quake

@Database(entities = [Quake::class], version = 1, exportSchema = false)
abstract class QuakeRoom : RoomDatabase() {
	
	companion object {
		
		const val DB_NAME = "quake_db"
		
		/**
		 * Singleton yapısı ile birden fazla örneğin oluşmasını engelleme
		 */
		@Volatile
		private var INSTANCE: QuakeRoom? = null
		
		fun getDatabase(context: Context): QuakeRoom {
			return when (val tempInstance = INSTANCE) {
				null -> synchronized(this) {
					val instance = Room.databaseBuilder(
						context,
						QuakeRoom::class.java,
						DB_NAME
					).fallbackToDestructiveMigration().build()
					INSTANCE = instance
					
					return instance
				}
				else -> tempInstance
			}
		}
	}
	
	abstract fun quakeDao(): QuakeDao
}
