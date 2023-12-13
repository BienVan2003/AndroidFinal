package com.tdtu.androidfinal.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.models.Card

class AddCardAdapter(var context: Context?, private var cardList: ArrayList<Card>, private var actionBar: ActionBar?) :
    RecyclerView.Adapter<AddCardAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val editTextTerm: EditText = itemView.findViewById(R.id.edtTerm)
        val editTextDefine: EditText = itemView.findViewById(R.id.edtDefine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_card, parent, false)
        itemView.requestFocus()
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        val currentCard: Card = cardList[position]
        var editTextTerm = holder.editTextTerm
        var editTextDefine = holder.editTextDefine
        editTextTerm.setText(currentCard.term)
        editTextDefine.setText(currentCard.define)

        editTextTerm.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                actionBar?.title = "${cardList.indexOf(currentCard) + 1}/${cardList.size}"
            }
        }

        editTextDefine.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                actionBar?.title = "${cardList.indexOf(currentCard) + 1}/${cardList.size}"
            }
        }

        editTextTerm.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                currentCard.term = editTextTerm.text.toString()
            }
        })

        editTextDefine.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                currentCard.define = editTextDefine.text.toString()
            }
        })

    }
    override fun getItemCount(): Int {
        return cardList.size
    }
    fun release(){
        context = null
        actionBar = null
    }
}