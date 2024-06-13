package com.example.horoscopeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var horoscopeList: List<Horoscope>

    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        horoscopeList = HoroscopeProvider.findAll()

        val adapter = HoroscopeAdapter(horoscopeList) { position ->
            navigateToDetail(horoscopeList[position])
        }


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun navigateToDetail (horoscope:Horoscope) {
        val intent: Intent = Intent (this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_HOROSCOPE_ID, horoscope.id);
        startActivity(intent)
    }

}

