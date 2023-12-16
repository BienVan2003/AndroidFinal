package com.tdtu.androidfinal.adapters

import android.animation.AnimatorInflater
import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.models.*
import java.util.Locale

class FlashCardAdapter(var context: Context?, private var cards: ArrayList<Card>) :
    RecyclerView.Adapter<FlashCardAdapter.CardViewHolder>() , TextToSpeech.OnInitListener {
    private var textToSpeechEnglish: TextToSpeech? = context?.let { TextToSpeech(it, this) }
    private var frontAnim = AnimatorInflater.loadAnimator(context,R.animator.front_animator)
    private var backAnim = AnimatorInflater.loadAnimator(context,R.animator.back_animator)
    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icVolume: TextView = itemView.findViewById(R.id.icVolume)
        val tvTerm: TextView = itemView.findViewById(R.id.tvTerm)
        val tvDefine: TextView = itemView.findViewById(R.id.tvDefine)
        val frontCard: LinearLayout = itemView.findViewById(R.id.front_card)
        val backCard: LinearLayout = itemView.findViewById(R.id.back_card)

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
        holder.tvTerm.text = currentCard.term
        holder.tvDefine.text = currentCard.define
        var isFront = true
        val scale: Float? = context?.resources?.displayMetrics?.density
        holder.tvTerm.cameraDistance = 8000 * scale!!
        holder.tvDefine.cameraDistance = 8000 * scale!!

        holder.frontCard.setOnClickListener {
            isFront = if(isFront){
                frontAnim.setTarget(holder.frontCard)
                backAnim.setTarget(holder.backCard)
                frontAnim.start()
                backAnim.start()
                false
            }else{
                frontAnim.setTarget(holder.backCard)
                backAnim.setTarget(holder.frontCard)
                backAnim.start()
                frontAnim.start()
                true
            }
        }

        holder.icVolume.setOnClickListener {
            textToSpeechEnglish?.speak(holder.tvTerm.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun release(){
        textToSpeechEnglish?.stop()
        textToSpeechEnglish?.shutdown()
        textToSpeechEnglish = null
        cards.clear()
        context = null
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val languageResult = textToSpeechEnglish?.setLanguage(Locale.US)

            if (languageResult == TextToSpeech.LANG_MISSING_DATA || languageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TAG", "Language is not supported.")
            }
        } else {
            Log.e("TAG", "Initialization failed.")
        }
    }
}