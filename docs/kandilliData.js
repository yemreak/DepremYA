const URL = "http://www.koeri.boun.edu.tr/scripts/lst0.asp"

const NULL = "-.-"
const DATA_INDEX = 578
const INDEX_DATES = 11
const INDEX_HOURS = INDEX_DATES + 10
const INDEX_LATS = INDEX_HOURS + 10
const INDEX_LONGS = INDEX_LATS + 10
const INDEX_DEPTHS = INDEX_LONGS + 14
const INDEX_MDS = INDEX_DEPTHS + 5
const INDEX_MLS = INDEX_MDS + 5
const INDEX_MWS = INDEX_MLS + 5
const INDEX_PLACES = INDEX_MWS + 50
const INDEX_RESOLUTIONS = INDEX_PLACES + 32

let response = document.getElementsByTagName("pre")[0].innerText
response = response.slice(DATA_INDEX)

const earthquakes = []
response.split("\n").forEach(line => {
	const earthquake = {}
	earthquake["date"] = line.slice(0, INDEX_DATES).trim()
	earthquake["hour"] = line.slice(INDEX_DATES, INDEX_HOURS).trim()
	earthquake["lat"] = line.slice(INDEX_HOURS, INDEX_LATS).trim()
	earthquake["long"] = line.slice(INDEX_LATS, INDEX_LONGS).trim()
	earthquake["depth"] = line.slice(INDEX_LONGS, INDEX_DEPTHS).trim()
	earthquake["md"] = line.slice(INDEX_DEPTHS, INDEX_MDS).trim()
	earthquake["ml"] = line.slice(INDEX_MDS, INDEX_MLS).trim()
	earthquake["mw"] = line.slice(INDEX_MLS, INDEX_MWS).trim()
	earthquake["place"] = line.slice(INDEX_MWS, INDEX_PLACES).trim()
	earthquake["resolution"] = line.slice(INDEX_PLACES, INDEX_RESOLUTIONS).trim()
	earthquakes.push(earthquake)
})
