package com.example.horoscopeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopeapp.R
import com.example.horoscopeapp.data.Horoscope
import com.example.horoscopeapp.utils.SessionManager

class HoroscopeAdapter (private var dataSet: List<Horoscope>, private val onItemClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<HoroscopeViewHolder>() {
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

    //Este método sirve para actualizar los datos.
    fun updateData(newDataSet: List<Horoscope>){
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}

class HoroscopeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val nameTextView: TextView
    private val descTextView: TextView
    private val logoImageView: ImageView
    private val favoriteImageView : ImageView

    init {
        nameTextView = view.findViewById(R.id.nameTextView)
        descTextView = view.findViewById(R.id.descrTextView)
        logoImageView = view.findViewById(R.id.logoImageView)
        favoriteImageView = view.findViewById(R.id.favoriteImageView)
    }

    fun render(horoscope: Horoscope) {
        nameTextView.setText(horoscope.name)
        descTextView.setText(horoscope.description)
        logoImageView.setImageResource(horoscope.logo)

        val context = itemView.context
        var isFavorite = SessionManager(context).isFavorite(horoscope.id)
        if (isFavorite) {
            favoriteImageView.visibility = View.VISIBLE
        } else {
            favoriteImageView.visibility = View.GONE
        }
    }
}



