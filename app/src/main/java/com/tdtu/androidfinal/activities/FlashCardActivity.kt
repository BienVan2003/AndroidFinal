package com.tdtu.androidfinal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.adapters.CardStackAdapter
import com.tdtu.androidfinal.databinding.ActivityFlashCardBinding
import com.tdtu.androidfinal.databinding.ActivityLoginBinding
import com.tdtu.androidfinal.models.Topic
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener

class FlashCardActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFlashCardBinding.inflate(layoutInflater) }
    private lateinit var  manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter
    private lateinit var topic: Topic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close)
        }

        (intent?.extras?.getSerializable("TOPIC") as Topic).let {
            topic = Topic(it.id, it.userId, it.username, it.title, it.description, it.cardList, it.isPublic)
        }
        supportActionBar?.title = ""
        adapter = CardStackAdapter(this, topic.cardList)
        manager = CardStackLayoutManager(this)
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("Trộn thẻ")
        menu.add("Phát âm thanh")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Xử lý khi nút "Back" được nhấn
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}