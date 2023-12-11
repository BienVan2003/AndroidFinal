package com.tdtu.androidfinal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.models.Topic

class TopicAdapter(val context: Context, private val topicList: ArrayList<Topic>) :
    RecyclerView.Adapter<TopicAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvTotalCard: TextView = itemView.findViewById(R.id.tvTotalCard)
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topic, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentTopic = topicList[position]
        holder.tvTitle.text = currentTopic.title
        holder.tvTotalCard.text = topicList.size.toString()
        holder.tvUsername.text = currentTopic.username
    }

    override fun getItemCount(): Int {
        return topicList.size
    }
}