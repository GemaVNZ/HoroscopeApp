package com.example.horoscopeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    val horoscopeList : List<Horoscope> = listOf(
        Horoscope(id="aries",name="Aries",logo=0),
        Horoscope(id="taurus",name="Taurus",logo=1),
        Horoscope(id="gemini",name="Gemini",logo=2),
        Horoscope(id="cancer",name="Cancer",logo=3),
        Horoscope(id="leo",name="Leo",logo=4),
        Horoscope(id="virgo",name="Virgo",logo=5),
        Horoscope(id="libra",name="Libra",logo=6),
        Horoscope(id="scorpio",name="Scorpio",logo=7),
        Horoscope(id="aries",name="",logo=8),
        Horoscope(id="aries",name="",logo=9),
        Horoscope(id="aries",name="",logo=10),
        Horoscope(id="aquarius",name="Aquarius",logo=11),
        Horoscope(id="pisces",name="Pisces",logo=12),

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}