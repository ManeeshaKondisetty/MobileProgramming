package com.example.vijaya.earthquakeapp

import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object QueryUtils {
    /**
     * Tag for the log messages
     */
    private val LOG_TAG = QueryUtils::class.java.simpleName

    /**
     * Query the USGS dataset and return a list of [Earthquake] objects.
     */
    fun fetchEarthquakeData(requestUrl: String?): List<Earthquake> {
        // An empty ArrayList that we can start adding earthquakes to

        val earthquakes: MutableList<Earthquake> = arrayListOf()
        //  URL object to store the url for a given string
        var url: URL? = null
        // A string to store the response obtained from rest call in the form of string
        try {
            url = URL(requestUrl)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"

                inputStream.bufferedReader().use {
                    val bufferdata = it.readText()
                    val jsonResponse = JSONObject(bufferdata)
                    val featuresArray = jsonResponse.getJSONArray("features")
                    for (i in 0 until featuresArray.length() - 1) {
                        val mag = ((featuresArray.get(i) as JSONObject).get("properties") as JSONObject).getDouble("mag")
                        val place = ((featuresArray.get(i) as JSONObject).get("properties") as JSONObject).getString("place")
                        val time = ((featuresArray.get(i) as JSONObject).get("properties") as JSONObject).getLong("time")
                        val url: String = ((featuresArray.get(i) as JSONObject).get("properties") as JSONObject).getString("url")
                        val dataset = Earthquake(mag, place, time, url)
                        earthquakes.add(dataset)
                    }
                }
            }

        } catch (e: Exception) {
        }
        // Return the list of earthquakes
        return earthquakes
    }
}