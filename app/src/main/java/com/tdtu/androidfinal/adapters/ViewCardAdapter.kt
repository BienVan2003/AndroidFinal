package com.tdtu.androidfinal.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.models.*

class ViewCardAdapter(var context: Context?, private var cardList: ArrayList<Card>) :
    RecyclerView.Adapter<ViewCardAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTerm: TextView = itemView.findViewById(R.id.tvTerm)
        val tvDefine: TextView = itemView.findViewById(R.id.tvDefine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_card, parent, false)
        itemView.requestFocus()
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
//            Log.e("TAG", cardList.toString())
        val currentCard: Card = cardList[position]

        holder.tvTerm.text = currentCard.term
        holder.tvDefine.text = currentCard.define

    }
    override fun getItemCount(): Int {
        return cardList.size
    }
    fun release(){
        context = null
    }

}