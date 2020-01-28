package com.yemreak.depremya.entity

/**
 * Deprem bilgileri
 * @see <a href="http://www.koeri.boun.edu.tr/scripts/lst0.asp">Son depremler `~ Kandilli Rasathanesi</a>
 */
data class EarthQuake(
    val date: String,
    val hour: String,
    val lat: String,
    val long: String,
    val depth: String,
    val md: String,
    val ml: String,
    val mw: String,
    val place: String,
    val resolution: String
)