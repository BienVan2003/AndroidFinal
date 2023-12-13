package com.tdtu.androidfinal.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.activities.DetailTopicActivity
import com.tdtu.androidfinal.models.*
import java.io.Serializable
import kotlin.reflect.typeOf

class TopicAdapter(var context: Context?, private var topicList: ArrayList<Topic>) :
    RecyclerView.Adapter<TopicAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvTotalCard: TextView = itemView.findViewById(R.id.tvTotalCard)
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topic, parent, false)
        return CardViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return topicList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentTopic = topicList[position]
        holder.tvTitle.text = currentTopic.title
        holder.tvTotalCard.text = currentTopic.cardList.size.toString() + " thuật ngữ"
        holder.tvUsername.text = currentTopic.username

        holder.cardView.setOnClickListener {
            onClickGotoDetail(currentTopic)
        }

    }

    private fun onClickGotoDetail(topic: Topic) {
        val intent = Intent(context, DetailTopicActivity::class.java)

        intent.putExtra("TOPIC", topic)
        context?.startActivity(intent)
    }

    fun release(){
        context = null
        topicList.clear()
    }


}