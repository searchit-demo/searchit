package com.demo.searchit

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.demo.searchit.model.SearchResponse
import com.demo.searchit.model.SearchResult
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var resultView: RecyclerView
    private var searchResult: JSONArray = JSONArray()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        resultView = findViewById(R.id.result_view)
        resultView.setHasFixedSize(true)
        resultView.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter(searchResult)
        resultView.adapter = searchAdapter

        searchView = findViewById(R.id.search_view)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getSearchResults(newText)
                return true
            }

        })

    }

    private fun getSearchResults(query: String?) {
        val url = URL("https://fileupload.rick-and-friends.site/search?keyword=$query").readText()
        val s = JSONObject(url).getJSONArray("results")
        searchAdapter.updateData(s)
        searchAdapter.notifyDataSetChanged()

    }

    class SearchAdapter(private var searchResult: JSONArray): RecyclerView.Adapter<ViewHolder>() {
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val textView : TextView = itemView.findViewById(R.id.textView)
            val subTitleTextView : TextView = TextView(itemView.context)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view =ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_result_item, parent, false))
            return view
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.textView).text = searchResult[position].toString()
        }

        override fun getItemCount(): Int {
            return searchResult.length()
        }

        fun updateData(data: JSONArray) {
            searchResult = data
            notifyDataSetChanged()
        }


    }


}