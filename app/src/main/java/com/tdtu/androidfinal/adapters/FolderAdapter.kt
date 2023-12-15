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
import com.tdtu.androidfinal.activities.DetailFolderActivity
import com.tdtu.androidfinal.activities.DetailTopicActivity
import com.tdtu.androidfinal.models.*

class FolderAdapter(var context: Context?, private var folders: ArrayList<Folder>) :
    RecyclerView.Adapter<FolderAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvTotalTopic: TextView = itemView.findViewById(R.id.tvTotalTopic)
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_folder, parent, false)
        return CardViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return folders.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentFolder = folders[position]
        holder.tvTitle.text = currentFolder.title
        holder.tvTotalTopic.text = currentFolder.topics.size.toString() + " học phần"
        holder.tvUsername.text = currentFolder.username

        holder.cardView.setOnClickListener {
            onClickGotoDetail(currentFolder)
        }
    }

    private fun onClickGotoDetail(folder: Folder) {
        val intent = Intent(context, DetailFolderActivity::class.java)

        intent.putExtra("FOLDER", folder)
        context?.startActivity(intent)
    }

    fun release(){
        context = null
        folders.clear()
    }


}