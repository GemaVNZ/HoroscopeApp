package com.example.horoscopeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class HoroscopeAdapter (private val dataSet: List<Horoscope>, private val onItemClickListener: (Int) -> Unit)
    : RecyclerView.Adapter<HoroscopeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horoscope, parent, false)

        return HoroscopeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        val horoscope = dataSet[position]
        holder.render(horoscope)
        holder.itemView.setOnClickListener{
            onItemClickListener(position)
        }
    }

    override fun getItemCount() : Int {
        return dataSet.size
    }
}

class HoroscopeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val nameTextView: TextView
    val descTextView: TextView
    val logoImageView: ImageView

    init {
        nameTextView = view.findViewById(R.id.nameTextView)
        descTextView = view.findViewById(R.id.descrTextView)
        logoImageView = view.findViewById(R.id.logoImageView)
    }

    fun render(horoscope: Horoscope) {
        nameTextView.setText(horoscope.name)
        descTextView.setText(horoscope.description)
        logoImageView.setImageResource(horoscope.logo)
    }
}



