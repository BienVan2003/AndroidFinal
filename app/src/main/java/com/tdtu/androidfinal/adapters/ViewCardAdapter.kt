package com.tdtu.androidfinal.adapters

import android.content.Context
import android.graphics.Color
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.databinding.ActivityDetailTopicBinding
import com.tdtu.androidfinal.models.Card
import java.util.Locale

class ViewCardAdapter(var context: Context?,
                      private var cardList: ArrayList<Card>, private var favoriteCards: ArrayList<Card>) :
    RecyclerView.Adapter<ViewCardAdapter.CardViewHolder>(), TextToSpeech.OnInitListener {
    private var textToSpeechEnglish: TextToSpeech = TextToSpeech(context, this)
    private var textToSpeechVietnamese: TextToSpeech = TextToSpeech(context, this)
    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTerm: TextView = itemView.findViewById(R.id.tvTerm)
        val tvDefine: TextView = itemView.findViewById(R.id.tvDefine)
        val icVolume: ImageView = itemView.findViewById(R.id.icVolume)
        var icStar: ImageView = itemView.findViewById(R.id.icStar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_card, parent, false)
        itemView.requestFocus()
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentCard: Card = cardList[position]

        holder.tvTerm.text = currentCard.term
        holder.tvDefine.text = currentCard.define
        val termToSpeak = holder.tvTerm.text.toString()
        val defineToSpeak = holder.tvDefine.text.toString()

        if(favoriteCards.contains(currentCard)){
            holder.icStar.setImageResource(R.drawable.ic_star_selected)
        }else{
            holder.icStar.setImageResource(R.drawable.ic_star_unselected)
        }

        val myTextColor = holder.tvTerm.textColors

        holder.tvTerm.setOnClickListener {
            holder.tvTerm.setTextColor(Color.YELLOW)
            textToSpeechEnglish.setOnUtteranceCompletedListener { utteranceId ->
                if (utteranceId == "your_unique_utterance_id") {
                    holder.tvTerm.setTextColor(myTextColor)
                }
            }

            val params = HashMap<String, String>()
            params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "your_unique_utterance_id"

            textToSpeechEnglish.speak(termToSpeak, TextToSpeech.QUEUE_FLUSH, params)
        }

        holder.tvDefine.setOnClickListener {
            holder.tvDefine.setTextColor(Color.YELLOW)

            textToSpeechVietnamese.setOnUtteranceCompletedListener { utteranceId ->
                if (utteranceId == "your_unique_utterance_id") {
                    holder.tvDefine.setTextColor(myTextColor)
                }
            }

            val params = HashMap<String, String>()
            params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "your_unique_utterance_id"

            textToSpeechVietnamese.speak(defineToSpeak, TextToSpeech.QUEUE_FLUSH, params)
        }
        holder.icVolume.setOnClickListener {
            holder.icVolume.setImageResource(R.drawable.ic_volume)

            holder.tvTerm.setTextColor(Color.YELLOW)
            textToSpeechEnglish.setOnUtteranceCompletedListener { utteranceId ->
                if (utteranceId == "your_unique_utterance_id") {
                    holder.tvTerm.setTextColor(myTextColor)

                    holder.tvDefine.setTextColor(Color.YELLOW)
                    textToSpeechVietnamese.setOnUtteranceCompletedListener { utteranceId ->
                        if (utteranceId == "your_unique_utterance_id") {
                            holder.tvDefine.setTextColor(myTextColor)
                            holder.icVolume.setImageResource(R.drawable.ic_volume_up)
                        }
                    }

                    val params2 = HashMap<String, String>()
                    params2[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "your_unique_utterance_id"

                    textToSpeechVietnamese.speak(defineToSpeak, TextToSpeech.QUEUE_FLUSH, params2)
                }
            }

            val params1 = HashMap<String, String>()
            params1[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "your_unique_utterance_id"
            textToSpeechEnglish.speak(termToSpeak, TextToSpeech.QUEUE_FLUSH, params1)
        }

        holder.icStar.setOnClickListener {
            if(favoriteCards.contains(currentCard)){
                holder.icStar.setImageResource(R.drawable.ic_star_unselected)
                favoriteCards.remove(currentCard)
            }else{
                holder.icStar.setImageResource(R.drawable.ic_star_selected)
                favoriteCards.add(currentCard)
            }
        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    fun release() {
        context = null
        cardList.clear()
        textToSpeechEnglish.stop()
        textToSpeechEnglish.shutdown()
        textToSpeechVietnamese.stop()
        textToSpeechVietnamese.shutdown()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Đặt ngôn ngữ cho TextToSpeech
            val languageResult = textToSpeechEnglish.setLanguage(Locale.ENGLISH)

            if (languageResult == TextToSpeech.LANG_MISSING_DATA || languageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TAG", "Language is not supported.")
            } else {
                // Ngôn ngữ Tiếng Anh (United States) được hỗ trợ, tiến hành cài đặt ngôn ngữ
                textToSpeechEnglish.language = Locale.ENGLISH
            }
        } else {
            Log.e("TAG", "Initialization failed.")
        }


        if (status == TextToSpeech.SUCCESS) {
            val langResult = textToSpeechVietnamese.setLanguage(Locale("vi"))
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TAG", "Vietnamese language is not supported.")
            } else {
                // Ngôn ngữ Tiếng Việt được hỗ trợ, tiến hành cài đặt ngôn ngữ
                textToSpeechVietnamese.language = Locale("vi")
            }
        } else {
            Log.e("TAG", "Initialization failed.")
        }
    }
}