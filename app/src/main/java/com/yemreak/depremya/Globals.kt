package com.yemreak.depremya

import com.yemreak.depremya.db.entity.Quake

public class Globals {
	companion object {
		//var INSTANCE: Globals? = null
		public var quakes: List<Quake> = emptyList()
	}
	
	private var quakes: List<Quake> = emptyList()
	
	/*object Singleton {
		fun getInstance(): Globals {
			if (INSTANCE == null) {
				INSTANCE = Globals()
			}
			return INSTANCE as Globals
		}
	}
	
	@Throws(NullPointerException::class)
	public fun getQuakes(): List<Quake> {
		if (quakes.isEmpty()) {
			throw NullPointerException("The selected news invoked without creation")
		}
		return quakes
	}
	
	public fun setQuakes(quakeList: List<Quake>) {
		quakes = quakeList
	}*/
}
