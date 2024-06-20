package com.example.horoscopeapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.horoscopeapp.data.Horoscope
import com.example.horoscopeapp.data.HoroscopeProvider
import com.example.horoscopeapp.R
import com.example.horoscopeapp.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.InputStreamReader
import java.io.BufferedReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_HOROSCOPE_ID = "HOROSCOPE_ID"
    }

    lateinit var horoscope: Horoscope

    lateinit var imageView: ImageView
    lateinit var textView: TextView

    lateinit var navigationBar: BottomNavigationView
    lateinit var progressIndicator: LinearProgressIndicator
    lateinit var dailyHoroscopeTextView: TextView

    lateinit var favorite_menuItem: MenuItem
    lateinit var sessionManager: SessionManager

    var isFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        sessionManager = SessionManager(this)

        val id : String = intent.getStringExtra(EXTRA_HOROSCOPE_ID)!!

        horoscope = HoroscopeProvider.findById(id!!)!!

        isFavorite = sessionManager.isFavorite(horoscope.id)

        textView = findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)
        navigationBar = findViewById(R.id.navigationBar)
        progressIndicator = findViewById(R.id.progressIndicator)
        dailyHoroscopeTextView = findViewById(R.id.dailyHoroscopeTextView)


        //textView.setText(horoscope.name)
        imageView.setImageResource(horoscope.logo)

        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.description)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_daily -> {
                    getHoroscopeLuck("daily")
                }

                R.id.menu_weekly -> {
                    getHoroscopeLuck("weekly")
                }

                R.id.menu_monthly -> {
                    getHoroscopeLuck("monthly")
                }
            }

            return@setOnItemSelectedListener true
        }

        navigationBar.selectedItemId = R.id.menu_daily
    }

    fun setFavoriteIcon() {
        if (isFavorite) {
            favorite_menuItem.setIcon(R.drawable.ic_favorite_selected)
        } else {
            favorite_menuItem.setIcon(R.drawable.ic_favorite)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detailactivity, menu)
        favorite_menuItem = menu.findItem(R.id.menu_favorite)
        setFavoriteIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.menu_favorite -> {
                if (isFavorite) {
                    sessionManager.setFavoriteHoroscope("")
                } else {
                    sessionManager.setFavoriteHoroscope(horoscope.id)
                }
                isFavorite = !isFavorite
                setFavoriteIcon()
                true
            }

            R.id.menu_share -> {
                val sendIntent = Intent()
                sendIntent.setAction(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                sendIntent.setType("text/plain")

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getHoroscopeLuck(method: String) {
        progressIndicator.visibility = View.VISIBLE
        // Se llama al hilo secundario para extrar la información diaria del horóscopo
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // URL base de la API y construcción de la URL completa
                val baseUrl = "https://horoscope-app-api.vercel.app/api/v1/get-horoscope/"
                val endpoint = when (method) {
                    "daily" -> "daily"
                    "weekly" -> "weekly"
                    "monthly" -> "monthly"
                    else -> throw IllegalArgumentException("Método de horóscopo no válido")
                }
                val url = URL("$baseUrl$endpoint?sign=${horoscope.id}&day=TODAY")
                val con = url.openConnection() as HttpsURLConnection
                con.requestMethod = "GET"
                val responseCode = con.responseCode
                Log.i("HTTP", "Response Code :: $responseCode")

                // Preguntamos si hubo error o no
                if (responseCode == HttpsURLConnection.HTTP_OK) { // Ha ido bien
                    // Metemos el cuerpo de la respuesta en un BurfferedReader
                    val bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
                    var inputLine: String?
                    val response = StringBuffer()
                    while (bufferedReader.readLine().also { inputLine = it } != null) {
                        response.append(inputLine)
                    }
                    bufferedReader.close()

                    // Parsear el fichero JSON para que nos muestre lo que necesitamos
                    val json = JSONObject(response.toString())
                    val result = json.getJSONObject("data").getString("horoscope_data")

                    runOnUiThread {
                        dailyHoroscopeTextView.text = result
                        progressIndicator.visibility = View.GONE
                    }

                } else { // Hubo un error
                    Log.w("HTTP", "Response :: Hubo un error")
                }
            } catch (e: Exception) {
                Log.e("HTTP", "Response Error :: ${e.stackTraceToString()}")
            }
        }
    }
}








