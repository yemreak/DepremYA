package com.yemreak.depremya.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.yemreak.depremya.db.entity.Quake.Companion.TABLE_NAME

/**
 * Deprem bilgileri
 * @see <a href="http://www.koeri.boun.edu.tr/scripts/lst0.asp">Son depremler `~ Kandilli Rasathanesi</a>
 */
@Entity(tableName = TABLE_NAME)
data class Quake(
	@ColumnInfo(name = COLUMN_ID) @PrimaryKey(autoGenerate = true) val id: Int,
	@ColumnInfo(name = COLUMN_DATE) val date: String,
	@ColumnInfo(name = COLUMN_HOUR) val hour: String,
	@ColumnInfo(name = COLUMN_LAT) val lat: String,
	@ColumnInfo(name = COLUMN_LNG) val lng: String,
	@ColumnInfo(name = COLUMN_DEPTH) val depth: String,
	@ColumnInfo(name = COLUMN_MD) val md: String,
	@ColumnInfo(name = COLUMN_ML) val ml: String,
	@ColumnInfo(name = COLUMN_MW) val mw: String,
	@ColumnInfo(name = COLUMN_CITY) val city: String,
	@ColumnInfo(name = COLUMN_REGION) val region: String,
	@ColumnInfo(name = COLUMN_RESOLUTION) val resolution: String
) {
	
	companion object {
		
		const val TABLE_NAME = "quake"
		const val COLUMN_ID = "id"
		const val COLUMN_DATE = "date"
		const val COLUMN_HOUR = "hour"
		const val COLUMN_LAT = "lat"
		const val COLUMN_LNG = "lng"
		const val COLUMN_DEPTH = "depth"
		const val COLUMN_MD = "md"
		const val COLUMN_ML = "ml"
		const val COLUMN_MW = "mw"
		const val COLUMN_CITY = "city"
		const val COLUMN_REGION = "region"
		const val COLUMN_RESOLUTION = "resolution"
		
	}
	
	@Ignore
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as Quake
		
		if (date != other.date) return false
		if (hour != other.hour) return false
		if (lat != other.lat) return false
		if (lng != other.lng) return false
		if (depth != other.depth) return false
		if (md != other.md) return false
		if (ml != other.ml) return false
		if (mw != other.mw) return false
		if (city != other.city) return false
		if (region != other.region) return false
		if (resolution != other.resolution) return false
		
		return true
	}
	
	@Ignore
	override fun toString(): String {
		return "Quake(id=$id, date='$date', hour='$hour', lat='$lat', lng='$lng', depth='$depth', md='$md', ml='$ml', mw='$mw', city='$city', region='$region', resolution='$resolution')"
	}
	
}
