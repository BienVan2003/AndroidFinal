package com.tdtu.androidfinal.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.activities.DetailTopicActivity
import com.tdtu.androidfinal.models.*

class CardStackAdapter(var context: Context?, private var cards: ArrayList<Card>) :
    RecyclerView.Adapter<CardStackAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icVolume: TextView = itemView.findViewById(R.id.icVolume)
        val tvCard: TextView = itemView.findViewById(R.id.tvCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flash_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentCard = cards[position]
        holder.tvCard.text = currentCard.term

    }

    fun release(){
        context = null
    }


}