package com.example.vijaya.earthquakeapp

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView
import java.util.*


class EarthquakeActivity : AppCompatActivity() {
    /**
     * Adapter for the list of earthquakes
     */
    private var mAdapter: EarthquakeAdapter? = null
    private var initialMag: Int = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setIcon(R.mipmap.ic_launcher);

        // Find a reference to the {@link ListView} in the layout
        val earthquakeListView = findViewById<View>(R.id.list) as ListView

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = EarthquakeAdapter(this, ArrayList())

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.adapter = mAdapter

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.onItemClickListener = OnItemClickListener { adapterView, view, position, l ->
            // Find the current earthquake that was clicked on
            val currentEarthquake = mAdapter!!.getItem(position)

            // Convert the String URL into a URI object (to pass into the Intent constructor)
            val earthquakeUri = Uri.parse(currentEarthquake!!.url)

            val i = Intent(Intent.ACTION_VIEW)
            i.data = earthquakeUri
            startActivity(i)

        }

        val magText = findViewById<TextView>(R.id.magnitude)


        findViewById<SeekBar>(R.id.slider).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                magText.text = p1.toString()
                initialMag = p1
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                val task = EarthquakeAsyncTask()
                task.execute("$USGS_REQUEST_URL$initialMag")
            }
        })

        // Start the AsyncTask to fetch the earthquake data
        val task = EarthquakeAsyncTask()
        task.execute("$USGS_REQUEST_URL$initialMag")

    }

    /**
     * [AsyncTask] to perform the network request on a background thread, and then
     * update the UI with the list of earthquakes in the response.
     *
     *
     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type. Our task will take a String URL, and return an Earthquake. We won't do
     * progress updates, so the second generic is just Void.
     *
     *
     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
     * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */
    private inner class EarthquakeAsyncTask : AsyncTask<String?, Void?, List<Earthquake>?>() {
        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * [Earthquake]s as the result.
         */
        override fun doInBackground(vararg urls: String?): List<Earthquake>? {
            return if (urls.isEmpty() || urls[0] == null) {
                null
            } else QueryUtils.fetchEarthquakeData(urls[0])
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of earthquake data from a previous
         * query to USGS. Then we update the adapter with the new list of earthquakes,
         * which will trigger the ListView to re-populate its list items.
         */
        override fun onPostExecute(data: List<Earthquake>?) {
            // Clear the adapter of previous earthquake data
            mAdapter!!.clear()

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && data.isNotEmpty()) {
                mAdapter!!.addAll(data)
            }
        }
    }

    companion object {
        private val LOG_TAG = EarthquakeActivity::class.java.name

        /**
         * URL for earthquake data from the USGS dataset
         */
        private const val USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&limit=15&minmag="
    }
}