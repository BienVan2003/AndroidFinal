package com.tdtu.androidfinal.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tdtu.androidfinal.adapters.ViewCardAdapter
import com.tdtu.androidfinal.databinding.ActivityDetailTopicBinding
import com.tdtu.androidfinal.models.Card
import com.tdtu.androidfinal.models.Topic

class DetailTopicActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailTopicBinding.inflate(layoutInflater) }
    private lateinit var topic: Topic
    private lateinit var cardAdapter: ViewCardAdapter
    private var favoriteCards: ArrayList<Card> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        (intent?.extras?.getSerializable("TOPIC") as Topic).let {
            topic = Topic(
                it.id,
                it.userId,
                it.username,
                it.title,
                it.description,
                it.cardList,
                it.isPublic
            )
        }

        setupView()
        var isButtonAll = true

        binding.btnFlashCard.setOnClickListener {
            if(isButtonAll){
                val intent = Intent(this, FlashCardActivity::class.java)
                intent.putExtra("TOPIC", topic)
                startActivity(intent)
            }else{
                var tmpTopic = Topic(topic.id,topic.userId,topic.username, topic.title, topic.description, favoriteCards, topic.isPublic)
                val intent = Intent(this, FlashCardActivity::class.java)
                intent.putExtra("TOPIC", tmpTopic)
                startActivity(intent)
            }

        }
        binding.btnTest.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            intent.putExtra("TOPIC", topic)
            startActivity(intent)
        }
        binding.btnTypeWord.setOnClickListener {
            val intent = Intent(this, WordFillingActivity::class.java)
            intent.putExtra("TOPIC", topic)
            startActivity(intent)
        }

        binding.btnAll.setOnClickListener {
            if (!isButtonAll) {
                isButtonAll = true
                Log.e("TAG", "right")
                binding.btnAll.setTextColor(Color.WHITE)
                binding.btnAll.setBackgroundColor(Color.parseColor("#FF6200EE"))

                binding.btnFavorite.setTextColor(Color.parseColor("#FF6200EE"))
                binding.btnFavorite.setBackgroundColor(Color.WHITE)

                cardAdapter = ViewCardAdapter(this, topic.cardList, favoriteCards)
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                binding.recyclerView.adapter = cardAdapter
            }
        }
        binding.btnFavorite.setOnClickListener {
            if (favoriteCards.isNotEmpty()) {
                if(isButtonAll){
                    isButtonAll = false
                    Log.e("TAG", "left")
                    binding.btnFavorite.setTextColor(Color.WHITE)
                    binding.btnFavorite.setBackgroundColor(Color.parseColor("#FF6200EE"))

                    binding.btnAll.setTextColor(Color.parseColor("#FF6200EE"))
                    binding.btnAll.setBackgroundColor(Color.WHITE)

                    cardAdapter = ViewCardAdapter(this, favoriteCards, favoriteCards)
                    binding.recyclerView.layoutManager = LinearLayoutManager(this)
                    binding.recyclerView.adapter = cardAdapter
                }
            }
        }


    }

    private fun setupView() {
        binding.tvTitle.text = topic.title
        binding.tvTotalCard.text = topic.cardList.size.toString() + " thuật ngữ"
        if (topic.description.isNotEmpty()) {
            binding.tvDescription.text = topic.description
        } else {
            binding.tvDescription.visibility = View.GONE
        }

        binding.recyclerView.isNestedScrollingEnabled = false;
        cardAdapter = ViewCardAdapter(this, topic.cardList, favoriteCards)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = cardAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                (data?.extras?.getSerializable("TOPIC") as Topic).let {
                    topic = Topic(
                        it.id,
                        it.userId,
                        it.username,
                        it.title,
                        it.description,
                        it.cardList,
                        it.isPublic
                    )
                }
                setupView()
                Toast.makeText(this, "Cập nhật topic thành công!", Toast.LENGTH_SHORT).show()
            } else {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menu.add("Thêm vào thư mục")
        menu.add("Sữa học phần")
        menu.add("Xóa học phần")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val title = item.title.toString()

        if (title == "Sữa học phần") {
            val intent = Intent(this, EditTopicActivity::class.java)
            intent.putExtra("TOPIC", topic)
            startActivityForResult(intent, 1)
        }
        if (title == "Xóa học phần") {
            showConfirmationDialog()
        }

        return when (item.itemId) {
            android.R.id.home -> {
                setResult(RESULT_OK)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        cardAdapter.release()
        super.onDestroy()
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Xác nhận xóa")
            .setMessage("Bạn chắc chắn muốn xóa học phần này?")

        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setPositiveButton("Xóa") { dialog, _ ->
            handleDeleteTopic()
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun handleDeleteTopic() {

    }

}