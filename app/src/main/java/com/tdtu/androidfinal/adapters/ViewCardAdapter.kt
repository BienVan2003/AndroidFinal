package com.tdtu.androidfinal.adapters

import android.content.Context
import android.graphics.Color
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.models.*
import java.util.Locale

class ViewCardAdapter(var context: Context?, private var cardList: ArrayList<Card>) :
    RecyclerView.Adapter<ViewCardAdapter.CardViewHolder>(), TextToSpeech.OnInitListener {
    private var textToSpeechEnglish: TextToSpeech = TextToSpeech(context, this)
    private var textToSpeechVietnamese: TextToSpeech = TextToSpeech(context, this)

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTerm: TextView = itemView.findViewById(R.id.tvTerm)
        val tvDefine: TextView = itemView.findViewById(R.id.tvDefine)
        val icVolume: MaterialTextView = itemView.findViewById(R.id.icVolume)
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

        val myTextColor = holder.tvTerm.textColors

        holder.tvTerm.setOnClickListener {
            holder.tvTerm.setTextColor(Color.YELLOW)
            speakEnglish(termToSpeak)
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
            val myColorIcon = holder.icVolume.textColors

            holder.tvTerm.setTextColor(Color.YELLOW)
            speakEnglish(termToSpeak)
            textToSpeechEnglish.setOnUtteranceCompletedListener { utteranceId ->
                if (utteranceId == "your_unique_utterance_id") {
                    holder.tvTerm.setTextColor(myTextColor)

                    holder.tvDefine.setTextColor(Color.YELLOW)
                    textToSpeechVietnamese.setOnUtteranceCompletedListener { utteranceId ->
                        if (utteranceId == "your_unique_utterance_id") {
                            holder.tvDefine.setTextColor(myTextColor)
                            holder.icVolume.setTextColor(myColorIcon)
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
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    fun release() {
        context = null
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Đặt ngôn ngữ cho TextToSpeech
            val languageResult = textToSpeechEnglish.setLanguage(Locale.US)

            if (languageResult == TextToSpeech.LANG_MISSING_DATA || languageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TAG", "Language is not supported.")
            }
        } else {
            Log.e("TAG", "Initialization failed.")
        }


        if (status == TextToSpeech.SUCCESS) {
            val langResult = textToSpeechVietnamese.setLanguage(Locale("vi"))
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TAG", "Vietnamese language is not supported.")
            }
        } else {
            Log.e("TAG", "Initialization failed.")
        }

    }

    private fun speakEnglish(text: String) {
        textToSpeechEnglish.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
    private fun speakVietnamese(text: String) {
        textToSpeechVietnamese.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}