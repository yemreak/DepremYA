package com.yemreak.depremya

import com.yemreak.depremya.db.dao.QuakeDao
import com.yemreak.depremya.db.entity.Quake
import kotlinx.coroutines.flow.Flow

class QuakeRepository(private val quakeDao: QuakeDao) {
	
	val allQuakes: Flow<List<Quake>> = quakeDao.getAll()
	
	suspend fun insert(quakes: Array<out Quake>) {
		quakeDao.insertAll(quakes)
	}
	
	suspend fun deleteAll() {
		quakeDao.deleteAll()
	}
	
}
