package com.yemreak.depremya.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yemreak.depremya.db.entity.Quake
import kotlinx.coroutines.flow.Flow


@Dao
abstract class QuakeDao {
	
	@Insert
	abstract suspend fun insertAll(quakes: Array<out Quake>)
	
	@Query("SELECT * FROM ${Quake.TABLE_NAME}")
	abstract fun getAll(): Flow<List<Quake>>
	
	@Query("SELECT * FROM ${Quake.TABLE_NAME} WHERE ${Quake.COLUMN_MD} > :md")
	abstract fun getAllHigherMd(md: Float): Flow<List<Quake>>
	
	@Query("DELETE FROM ${Quake.TABLE_NAME}")
	abstract suspend fun deleteAll()
	
}
