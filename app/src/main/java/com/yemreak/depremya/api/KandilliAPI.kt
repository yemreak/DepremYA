package com.yemreak.depremya.api

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.yemreak.depremya.entity.Earthquake
import java.util.*
import kotlin.collections.ArrayList


abstract class KandilliAPI {

	companion object {

		private val TAG = KandilliAPI::class.java.simpleName

		private const val URL = "http://www.koeri.boun.edu.tr/scripts/lst1.asp"

		private const val dataIndex = 591

		private const val indexDates = 10
		private const val indexHours = indexDates + 10
		private const val latIndex = indexHours + 10
		private const val longIndex = latIndex + 10
		private const val depthIndex = longIndex + 14
		private const val mdsIndex = depthIndex + 5
		private const val mlsIndex = mdsIndex + 5
		private const val mwsIndex = mlsIndex + 5
		private const val placesIndex = mwsIndex + 50

		fun requestEarthQuakes(context: Context, onResponse: (List<Earthquake>) -> Unit) {
			val queue = Volley.newRequestQueue(context)
			val stringRequest = StringRequest(Request.Method.GET, URL,
					Response.Listener<String> { response ->
						onResponse(parseResponse(response.replace("Ý", "İ")))
					},
					Response.ErrorListener {
						Log.e(
								TAG,
								"requestEarthQuakes: İstek atılamadı $URL",
								it
						)
					})

			queue.add(stringRequest)
		}

		private fun parseResponse(response: String): List<Earthquake> {
			val earthQuakeList = ArrayList<Earthquake>()

			val regex = """<pre>.*</pre>""".toRegex(RegexOption.DOT_MATCHES_ALL)
			regex.find(response)?.value?.let {
				it.slice(dataIndex until it.length).split("\n").forEach { line ->
					if (line.trim().isNotEmpty() && !line.contains("</pre>"))
						earthQuakeList.add(parseLine(line))
				}
			}

			return earthQuakeList.toList()
		}

		private fun parseLine(line: String): Earthquake {
			var city: String = ""
			var region: String = ""

			line.slice(mwsIndex..placesIndex).trim().split(" ").let {
				city = it.last().replace("(", "").replace(")", "").run {
					this[0] + this.slice(1 until length).toLowerCase(Locale.ROOT)
				}
				if (it.size > 1) region = it.first()
			}

			return Earthquake(
					line.slice(0..indexDates).trim(),
					line.slice(indexDates..indexHours).trim(),
					line.slice(indexHours..latIndex).trim(),
					line.slice(latIndex..longIndex).trim(),
					line.slice(longIndex..depthIndex).trim(),
					line.slice(depthIndex..mdsIndex).trim(),
					line.slice(mdsIndex..mlsIndex).trim(),
					line.slice(mlsIndex..mwsIndex).trim(),
					city,
					region,
					line.slice(placesIndex until line.length).trim()
			)
		}
	}
}
