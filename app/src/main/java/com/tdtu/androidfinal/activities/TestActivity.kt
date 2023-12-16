package com.tdtu.androidfinal.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.google.android.material.button.MaterialButton
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.databinding.ActivityTestBinding
import com.tdtu.androidfinal.models.Answer
import com.tdtu.androidfinal.models.Card
import com.tdtu.androidfinal.models.Question
import com.tdtu.androidfinal.models.Topic

class TestActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }
    private lateinit var mListQuestion: List<Question>
    private var currentQuestion = 0
    private lateinit var mQuestion: Question
    private lateinit var selectedAnswer: String
    private lateinit var topic: Topic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close)
            title = "Lượt 1"
        }

        (intent?.extras?.getSerializable("TOPIC") as Topic).let {
            topic = Topic(it.id, it.userId, it.username, it.title, it.description, it.cardList, it.isPublic)
        }

        mListQuestion = getListQuestion()

        if (mListQuestion.isEmpty()) {
            return
        }

        setDataQuestion(mListQuestion[currentQuestion])
    }

    private fun setDataQuestion(question: Question) {
        if (question == null) {
            return
        }

        mQuestion = question

        binding.btnAnswer1.setBackgroundColor(Color.WHITE)
        binding.btnAnswer2.setBackgroundColor(Color.WHITE)
        binding.btnAnswer3.setBackgroundColor(Color.WHITE)
        binding.btnAnswer4.setBackgroundColor(Color.WHITE)

        supportActionBar?.title = "${question.number}/${topic.cardList.size}"
        binding.tvQuestion.text = question.content
        binding.btnAnswer1.text = question.answers[0].content
        binding.btnAnswer2.text = question.answers[1].content
        binding.btnAnswer3.text = question.answers[2].content
        binding.btnAnswer4.text = question.answers[3].content

        binding.btnAnswer1.setOnClickListener(this)
        binding.btnAnswer2.setOnClickListener(this)
        binding.btnAnswer3.setOnClickListener(this)
        binding.btnAnswer4.setOnClickListener(this)
    }

    private fun getListQuestion(): List<Question> {
        var list = ArrayList<Question>()
        var cards = topic.cardList
        cards.forEachIndexed { index, card ->
            val cardsToSelectFrom = cards - card

            // Chọn 3 định nghĩa ngẫu nhiên từ các thẻ khác
            val randomCards = cardsToSelectFrom.shuffled().take(3) as ArrayList<Card>

            // Thêm định nghĩa của thẻ hiện tại vào danh sách các định nghĩa
            randomCards.add(card)

            // Xáo trộn danh sách các định nghĩa để ngăn chặn sự định rõ vị trí của đáp án đúng
            val shuffledOptions = randomCards.shuffled()

            var answerList = ArrayList<Answer>()

            answerList.add(Answer(shuffledOptions[0].define, shuffledOptions[0].define==card.define))
            answerList.add(Answer(shuffledOptions[1].define, shuffledOptions[1].define==card.define))
            answerList.add(Answer(shuffledOptions[2].define, shuffledOptions[2].define==card.define))
            answerList.add(Answer(shuffledOptions[3].define, shuffledOptions[3].define==card.define))

            list.add(Question(index+1, card.term, answerList))
        }
        return list
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.test_menu, menu)
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAnswer1 -> {
                selectedAnswer = binding.btnAnswer1.text.toString()
                binding.btnAnswer1.setBackgroundColor(Color.LTGRAY)
                checkAnswer(binding.btnAnswer1, mQuestion, mQuestion.answers[0])
            }

            R.id.btnAnswer2 -> {
                selectedAnswer = binding.btnAnswer2.text.toString()
                binding.btnAnswer2.setBackgroundColor(Color.LTGRAY)
                checkAnswer(binding.btnAnswer2, mQuestion, mQuestion.answers[1])
            }

            R.id.btnAnswer3 -> {
                selectedAnswer = binding.btnAnswer3.text.toString()
                binding.btnAnswer3.setBackgroundColor(Color.LTGRAY)
                checkAnswer(binding.btnAnswer3, mQuestion, mQuestion.answers[2])
            }

            R.id.btnAnswer4 -> {
                selectedAnswer = binding.btnAnswer4.text.toString()
                binding.btnAnswer4.setBackgroundColor(Color.LTGRAY)
                checkAnswer(binding.btnAnswer4, mQuestion, mQuestion.answers[3])
            }
        }
    }

    private fun checkAnswer(button: MaterialButton, question: Question, answer: Answer) {
        Handler(Looper.getMainLooper())
            .postDelayed({
                if (answer.isCorrect) {
                    button.setBackgroundColor(Color.GREEN)
                    nextQuestion()
                } else {
                    button.setBackgroundColor(Color.YELLOW)
                    showAnswerCorrect(question)
                }
            }, 1000)
    }

    private fun showAnswerCorrect(question: Question) {
        if (question?.answers == null || question.answers.isEmpty()) {
            return
        }

        if (question.answers[0].isCorrect) {
            binding.btnAnswer1.setBackgroundColor(Color.GREEN)
        } else if (question.answers[1].isCorrect) {
            binding.btnAnswer2.setBackgroundColor(Color.GREEN)
        } else if (question.answers[2].isCorrect) {
            binding.btnAnswer3.setBackgroundColor(Color.GREEN)
        } else if (question.answers[3].isCorrect) {
            binding.btnAnswer4.setBackgroundColor(Color.GREEN)
        }
        answerCorrectDialog()
    }

    private fun nextQuestion() {
        answerDialog()
    }

    private fun answerCorrectDialog() {
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

        tvQuestion.text = mQuestion.content
        if (mQuestion.answers[0].isCorrect) {
            tvAnswer.text = mQuestion.answers[0].content
        } else if (mQuestion.answers[1].isCorrect) {
            tvAnswer.text = mQuestion.answers[1].content
        } else if (mQuestion.answers[2].isCorrect) {
            tvAnswer.text = mQuestion.answers[2].content
        } else if (mQuestion.answers[3].isCorrect) {
            tvAnswer.text = mQuestion.answers[3].content
        }
        tvAnswerCorrect.text = selectedAnswer

        binding.btnAnswer1.background.clearColorFilter()
        dialog.setOnDismissListener {
            if (currentQuestion == mListQuestion.size - 1) {
                // hết câu hoi
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    currentQuestion++
                    setDataQuestion(mListQuestion[currentQuestion])
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

    private fun answerDialog() {
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

        if (currentQuestion == mListQuestion.size - 1) {
            // hết câu hoi
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
                currentQuestion++
                setDataQuestion(mListQuestion[currentQuestion])
            }, 500)
        }
    }
}