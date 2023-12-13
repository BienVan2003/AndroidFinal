package com.tdtu.androidfinal.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.adapters.TopicAdapter
import com.tdtu.androidfinal.databinding.ActivityDetailTopicBinding
import com.tdtu.androidfinal.databinding.FragmentTopicBinding
import com.tdtu.androidfinal.models.Card
import com.tdtu.androidfinal.models.Topic

class TopicFragment : Fragment() {
    private lateinit var topicList: ArrayList<Topic>
    private lateinit var topicAdapter: TopicAdapter

    private lateinit var rvTopic: RecyclerView
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding by lazy { FragmentTopicBinding.inflate(layoutInflater, container, false) }
        // Inflate the layout for this fragment
        val view = binding.root
        requireActivity().title = "Topic"

        spinner = view.findViewById(R.id.spinner)
        rvTopic = view.findViewById(R.id.rvTopic)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        topicList = ArrayList()
        Firebase.firestore.collection("topics")
            .whereEqualTo("userId",Firebase.auth.currentUser?.uid)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    val topicData = document.data

                    val dataArray = topicData["cardList"] as? ArrayList<Map<String, Any>>
                    var tmpCardList :ArrayList<Card> = ArrayList()

                    dataArray?.let {
                        for (map in it) {
                            // Đọc dữ liệu từng map
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
                    topicList.add(topic)
                }
                topicAdapter = TopicAdapter(requireContext(), topicList)
                rvTopic.layoutManager = LinearLayoutManager(requireContext())
                rvTopic.adapter = topicAdapter
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
        return view
    }


//    override fun onDestroy() {
//        super.onDestroy()
//        rvTopic.adapter = null
//        topicAdapter.release()
//    }
}