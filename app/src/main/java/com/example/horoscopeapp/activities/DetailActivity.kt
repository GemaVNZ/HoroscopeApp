package com.example.horoscopeapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.horoscopeapp.data.Horoscope
import com.example.horoscopeapp.data.HoroscopeProvider
import com.example.horoscopeapp.R
import com.example.horoscopeapp.utils.SessionManager

class DetailActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_HOROSCOPE_ID = "HOROSCOPE_ID"
    }

    lateinit var horoscope : Horoscope

    lateinit var textView: TextView
    lateinit var imageView: ImageView
    lateinit var favorite_menuItem: MenuItem
    lateinit var sessionManager: SessionManager

    var isFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        sessionManager = SessionManager(this)

        val id = intent.getStringExtra(EXTRA_HOROSCOPE_ID)

        horoscope = HoroscopeProvider.findById(id!!)!!

        isFavorite = sessionManager.getFavoriteHoroscope()?.equals(horoscope.id) ?: false

        textView = findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)
        findViewById<TextView>(R.id.desctextView).setText(horoscope.description)

        textView.setText(horoscope.name)
        imageView.setImageResource(horoscope.logo)
    }

    fun setFavoriteIcon () {
        if (isFavorite) {
            favorite_menuItem.setIcon(R.drawable.ic_favorite_selected)
        } else {
            favorite_menuItem.setIcon(R.drawable.ic_favorite)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detailactivity,menu)
        favorite_menuItem = menu.findItem(R.id.menu_favorite)
        setFavoriteIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
                    val sendIntent = Intent ()
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
}



