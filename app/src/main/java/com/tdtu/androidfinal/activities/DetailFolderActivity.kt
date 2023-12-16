package com.tdtu.androidfinal.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.adapters.TopicAdapter
import com.tdtu.androidfinal.databinding.ActivityDetailFolderBinding
import com.tdtu.androidfinal.models.Folder

class DetailFolderActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailFolderBinding.inflate(layoutInflater) }
    private lateinit var folder: Folder
    private lateinit var topicAdapter: TopicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        (intent?.extras?.getSerializable("FOLDER") as Folder).let {
            folder = Folder(it.id,it.userId,it.username,it.title,it.description,it.topics)
            setupView()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_folder_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.miAddTopic -> {
                addTopicToFolder()
                true
            }
            R.id.miEditFolder -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun addTopicToFolder() {
        val intent = Intent(this, AddTopicToFolderActivity::class.java)
        intent.putExtra("FOLDER", folder)
        startActivityForResult(intent, 1)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                (data?.extras?.getSerializable("FOLDER") as Folder).let {
                    folder = Folder(it.id, it.userId, it.username, it.title, it.description, it.topics)
                }
                setupView()
                Toast.makeText(this,"Cập nhật folder thành công!", Toast.LENGTH_SHORT).show()
            } else {
            }
        }
    }

    private fun setupView() {
        binding.tvTitle.text = folder.title
        binding.tvTotalCard.text = folder.topics.size.toString() + " học phần"
        binding.tvUsername.text = folder.username

        if(folder.topics.isEmpty()){
            binding.btnLearn.visibility = View.GONE
        }else{
            binding.cardView.visibility = View.GONE
        }

        binding.rvTopic.isNestedScrollingEnabled = false;
        topicAdapter = TopicAdapter(this, folder.topics)
        binding.rvTopic.layoutManager = LinearLayoutManager(this)
        binding.rvTopic.adapter = topicAdapter
    }

}