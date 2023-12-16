package com.tdtu.androidfinal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.adapters.FlashCardAdapter
import com.tdtu.androidfinal.databinding.ActivityFlashCardBinding
import com.tdtu.androidfinal.models.Topic
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class FlashCardActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFlashCardBinding.inflate(layoutInflater) }
    private lateinit var  manager: CardStackLayoutManager
    private lateinit var adapter: FlashCardAdapter
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
        supportActionBar?.title = "1/${topic.cardList.size}"
        adapter = FlashCardAdapter(this, topic.cardList)
        manager =  CardStackLayoutManager(this, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
                val layoutManager = binding.cardStackView.layoutManager as CardStackLayoutManager
                val currentPosition = layoutManager.topPosition + 1
                supportActionBar?.title = "${currentPosition}/${topic.cardList.size}"
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View, position: Int) {
            }

            override fun onCardDisappeared(view: View, position: Int) {
            }
        })
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = adapter


        binding.ivRewind.setOnClickListener {
            binding.cardStackView.rewind()
            val layoutManager = binding.cardStackView.layoutManager as CardStackLayoutManager
            val currentPosition = layoutManager.topPosition + 1
            supportActionBar?.title = "${currentPosition}/${topic.cardList.size}"
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        adapter.release()
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