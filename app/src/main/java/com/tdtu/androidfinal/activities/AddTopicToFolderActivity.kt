package com.tdtu.androidfinal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.adapters.AddTopicToFolderAdapter
import com.tdtu.androidfinal.models.Card
import com.tdtu.androidfinal.models.Folder
import com.tdtu.androidfinal.models.Topic

class AddTopicToFolderActivity : AppCompatActivity() {
    private lateinit var topics: ArrayList<Topic>
    private lateinit var checkedTopics: ArrayList<Boolean>
    private lateinit var folder: Folder
    private lateinit var addTopicToFolderAdapter: AddTopicToFolderAdapter

    private lateinit var rvTopic: RecyclerView

    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic_to_folder)
        supportActionBar?.title = "Thêm học phần"

        (intent?.extras?.getSerializable("FOLDER") as Folder).let {
            folder = Folder(it.id, it.userId, it.username, it.title, it.description, it.topics)
        }

        rvTopic = findViewById(R.id.rvTopic)
        progressBar = findViewById(R.id.progressBar)

        topics = ArrayList()

        Firebase.firestore.collection("topics")
            .whereEqualTo("userId", Firebase.auth.currentUser?.uid)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    val topicData = document.data

                    val dataArray = topicData["cardList"] as? ArrayList<Map<String, Any>>
                    var tmpCardList :ArrayList<Card> = ArrayList()

                    dataArray?.let {
                        for (map in it) {
                            val term = map["term"]
                            val define = map["define"]
                            val image = map["image"]
                            tmpCardList.add(Card(term.toString(), define.toString(), image.toString()))
                        }
                    }

                    val topic = Topic(
                        id = document.id,
                        userId = topicData["userId"] as String,
                        username = topicData["username"] as String,
                        title = topicData["title"] as String,
                        description = topicData["description"] as String,
                        cardList = tmpCardList,
                        isPublic = topicData["isPublic"] as? Boolean ?: false
                    )
                    topics.add(topic)
                }
                checkedTopics = List(topics.size) { false } as ArrayList<Boolean>

                topics.forEachIndexed { index, topic ->
                    folder.topics.forEachIndexed { _, topicFolder ->
                        if(topic.id == topicFolder.id){
                            checkedTopics[index] = true
                        }
                    }
                }
                addTopicToFolderAdapter = AddTopicToFolderAdapter(this, topics, checkedTopics)
                rvTopic.layoutManager = LinearLayoutManager(this)
                rvTopic.adapter = addTopicToFolderAdapter
                progressBar.visibility = View.GONE
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("Xong").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val title = item.title.toString()

        if(title == "Xong"){
            folder.topics.clear()
            checkedTopics.forEachIndexed { index, item ->
                if(item){
                    folder.topics.add(topics[index])
                    Log.e("TAG", topics[index].toString())
                }
            }

            val user = Firebase.auth.currentUser
            user?.let {
                Firebase.firestore.collection("folders")
                    .document(folder.id)
                    .set(folder)
                    .addOnSuccessListener {
                        setResult(RESULT_OK, Intent().putExtra("FOLDER", folder))
                        finish()
                    }
                    .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
            }
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

}