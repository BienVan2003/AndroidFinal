package com.tdtu.androidfinal.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.databinding.ActivityWordFillingBinding
import com.tdtu.androidfinal.models.Card
import com.tdtu.androidfinal.models.Topic

class WordFillingActivity : AppCompatActivity() , View.OnClickListener{
    private val binding by lazy { ActivityWordFillingBinding.inflate(layoutInflater) }
    private lateinit var topic: Topic
    private lateinit var currentCard: Card
    private lateinit var cards: ArrayList<Card>
    private var currentQuestion = 0
    private lateinit var selectedAnswer: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close)
        }

        (intent?.extras?.getSerializable("TOPIC") as Topic).let {
            topic = Topic(it.id, it.userId, it.username, it.title, it.description, it.cardList, it.isPublic)
            topic.let {
                cards = topic.cardList
            }
        }
        if (cards.isEmpty()) {
            return
        }

        setDataQuestion(cards[currentQuestion])

        binding.edtAnswer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // Được gọi trước khi nội dung thay đổi
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // Được gọi khi nội dung thay đổi
            }

            override fun afterTextChanged(editable: Editable?) {
                // Được gọi sau khi nội dung đã thay đổi
                val text = editable.toString()
                if (text.isNotEmpty()) {
                    binding.btnSend.text = "Gửi"
                } else {
                    binding.btnSend.text = "Don't know"
                }
            }
        })

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.word_filling_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.miSaveFolder -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun setDataQuestion(card: Card) {
        if (card == null) {
            return
        }

        currentCard = card

        binding.edtAnswer.text.clear()

        supportActionBar?.title = "${currentQuestion+1}/${cards.size}"
        binding.tvQuestion.text = card.term

        binding.btnSend.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSend -> {
                if (binding.edtAnswer.text.isNotEmpty()) {
                    selectedAnswer = binding.edtAnswer.text.toString()
                    checkAnswer()
                } else {
                    // có nghĩa bằng don't know
                    if (currentQuestion == cards.size - 1) {
                        // hết câu hoi
                    } else {
                        Handler(Looper.getMainLooper()).postDelayed({
                            currentQuestion++
                            setDataQuestion(cards[currentQuestion])
                        }, 500)
                    }
                }
            }
        }
    }

    private fun checkAnswer() {
        if (selectedAnswer.equals(currentCard.define, ignoreCase = true)){
            showAnswerDialog()
        }else{
            showAnswerCorrectDialog()
        }
    }

    private fun showAnswerDialog() {
        val gravity = Gravity.CENTER
        var dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_answer)

        var window: Window? = dialog.window

        if (window == null) {
            return
        }

        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var windowAttributes: WindowManager.LayoutParams = window.attributes
        windowAttributes.gravity = gravity
        window.attributes = windowAttributes

        var tvAnswer: TextView = dialog.findViewById(R.id.tvAnswer)
        tvAnswer.text = selectedAnswer

        dialog.setCancelable(true)
        dialog.show()

        if (currentQuestion == cards.size - 1) {
            // hết câu hoi
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
                currentQuestion++
                setDataQuestion(cards[currentQuestion])
            }, 500)
        }
    }

    private fun showAnswerCorrectDialog() {
        val gravity = Gravity.CENTER
        var dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_answer_correct)

        var window: Window? = dialog.window

        if (window == null) {
            return
        }

        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var windowAttributes: WindowManager.LayoutParams = window.attributes
        windowAttributes.gravity = gravity
        window.attributes = windowAttributes

        var btnOk: Button = dialog.findViewById(R.id.btnOk)
        var tvQuestion: TextView = dialog.findViewById(R.id.tvQuestion)
        var tvAnswer: TextView = dialog.findViewById(R.id.tvAnswer)
        var tvAnswerCorrect: TextView = dialog.findViewById(R.id.tvAnswerCorrect)


        tvQuestion.text = currentCard.term
        tvAnswer.text = currentCard.define
        tvAnswerCorrect.text = selectedAnswer

        dialog.setOnDismissListener {
            if (currentQuestion == cards.size - 1) {
                // hết câu hoi
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    currentQuestion++
                    setDataQuestion(cards[currentQuestion])
                }, 500)
            }
        }
        btnOk.setOnClickListener {
            // Tự động chạy vào dialog.setOnDismissListener
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.show()
    }
}