package com.example.horoscopeapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopeapp.data.Horoscope
import com.example.horoscopeapp.adapters.HoroscopeAdapter
import com.example.horoscopeapp.data.HoroscopeProvider
import com.example.horoscopeapp.R

class MainActivity : AppCompatActivity() {

    //Variables donde iniciamos cada componente

    lateinit var horoscopeList: List<Horoscope>
    lateinit var recyclerView : RecyclerView
    lateinit var adapter: HoroscopeAdapter

    //Método que inicializa la vista por defecto.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        horoscopeList = HoroscopeProvider.findAll()
        adapter = HoroscopeAdapter(horoscopeList) { position ->
            navigateToDetail(horoscopeList[position])
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    //Método que actualiza la vista tras haber cambiado o ejecutado alguna acción en otra.
    override fun onResume() {
        super.onResume()
        adapter.updateData(horoscopeList)
    }

    //Método que incorpora el menú de Android en la vista.Dentro de dicho menú, se realiza diferentes métodos.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activitymain, menu)

        val searchViewItem = menu.findItem(R.id.menu_search)
        val searchView = searchViewItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    horoscopeList = HoroscopeProvider.findAll().filter {
                        getString(it.name).contains(newText, true) }
                    adapter.updateData(horoscopeList)
                }
                return true
            }
        })

        return true

    }

    fun navigateToDetail (horoscope: Horoscope) {
        val intent: Intent = Intent (this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_HOROSCOPE_ID, horoscope.id);
        startActivity(intent)
    }

}

