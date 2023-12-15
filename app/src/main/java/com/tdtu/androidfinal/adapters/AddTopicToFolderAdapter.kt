package com.tdtu.androidfinal.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.models.Topic

class AddTopicToFolderAdapter(
    var context: Context?,
    private var topicList: ArrayList<Topic>,
    private var checkedTopics: ArrayList<Boolean>
) :
    RecyclerView.Adapter<AddTopicToFolderAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvTotalCard: TextView = itemView.findViewById(R.id.tvTotalCard)
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
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
        Log.e("TAG", "adapter " + checkedTopics.size.toString())
        if (checkedTopics[position]) {
            holder.cardView.strokeWidth = 5
            holder.cardView.strokeColor = Color.parseColor("#303F9F")
        }
        holder.cardView.setOnClickListener {
            checkedTopics[position] = !checkedTopics[position]
            if (checkedTopics[position]) {
                holder.cardView.strokeWidth = 5
                holder.cardView.strokeColor = Color.parseColor("#303F9F")
            } else {
                holder.cardView.strokeWidth = 0
                holder.cardView.strokeColor = 0
            }
        }

    }

    fun release() {
        context = null
        topicList.clear()
    }


}