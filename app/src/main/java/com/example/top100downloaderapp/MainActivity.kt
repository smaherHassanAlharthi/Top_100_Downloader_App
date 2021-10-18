package com.example.top100downloaderapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var myRv: RecyclerView
    lateinit var rvAdapter: RVAdapter
    lateinit var feeds : ArrayList<Feeds>
    val parser = XMLParser()
    val top10="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"
    val top100="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=100/xml"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //supportActionBar?.hide()
        myRv = findViewById(R.id.rvFeeds)

        getFeeds(top10)

    }

    private fun getFeeds(url:String) {
        CoroutineScope(Dispatchers.IO).launch {

             feeds = async {
                fetch(url)
            }.await()

            withContext(Dispatchers.Main) {
                setRV()
            }

        }
    }

    fun setRV() {
        rvAdapter = RVAdapter(feeds, this@MainActivity)
        myRv.adapter = rvAdapter
        myRv.layoutManager = LinearLayoutManager(applicationContext)
    }

    fun fetch(url:String): ArrayList<Feeds> {
        val url =
            URL(url)
        val urlConnection = url.openConnection() as HttpURLConnection
        feeds =

            urlConnection.getInputStream()?.let {
                parser.parse(it)
            }
                    as ArrayList<Feeds>
        return feeds
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.top10app -> {
                findViewById<TextView>(R.id.tvTop).setText("Top 10 Apps in App Store")
                getFeeds(top10)
                val fab = findViewById<FloatingActionButton>(R.id.fabFetch)
                fab.setOnClickListener {
                    getFeeds(top10)
                }
                return true
            }
            R.id.top100app -> {
                findViewById<TextView>(R.id.tvTop).setText("Top 100 Apps in App Store")
                getFeeds(top100)
                val fab = findViewById<FloatingActionButton>(R.id.fabFetch)
                fab.setOnClickListener {
                    getFeeds(top100)
                }

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}