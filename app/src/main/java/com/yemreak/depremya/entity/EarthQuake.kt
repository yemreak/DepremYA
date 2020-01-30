package com.yemreak.depremya.entity

/**
 * Deprem bilgileri
 * @see <a href="http://www.koeri.boun.edu.tr/scripts/lst0.asp">Son depremler `~ Kandilli Rasathanesi</a>
 */
data class Earthquake(
	val date: String,
	val hour: String,
	val lat: String,
	val long: String,
	val depth: String,
	val md: String,
	val ml: String,
	val mw: String,
	val city: String,
	val region: String,
	val resolution: String
) {
	override fun toString(): String {
		return "EarthQuake(date='$date', hour='$hour', lat='$lat', long='$long', depth='$depth', md='$md', ml='$ml', mw='$mw', city='$city', region='$region', resolution='$resolution')"
	}
}