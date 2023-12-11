package com.tdtu.androidfinal.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.adapters.TopicAdapter
import com.tdtu.androidfinal.models.*

class TopicFragment : Fragment() {
    private lateinit var topicList: ArrayList<Topic>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_topic, container, false)
        requireActivity().title = "Topic"

        val spinner: Spinner = view.findViewById(R.id.spinner)
        val rvTopic: RecyclerView = view.findViewById(R.id.rvTopic)

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
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    val topicData = document.data
                    val topic = Topic(
                        userId = topicData["userId"] as String,
                        username = topicData["username"] as String,
                        title = topicData["title"] as String,
                        description = topicData["description"] as String,
                        // Đối với cardList và isPublic, thực hiện kiểm tra null và chuyển đổi an toàn
                        cardList = (topicData["cardList"] as? ArrayList<*>)?.filterIsInstance<Card>()?.toCollection(ArrayList()) ?: ArrayList(),
                        isPublic = topicData["isPublic"] as? Boolean ?: false
                    )
                    topicList.add(topic)
                    Log.d("TAG", "${document.id} => ${document.data}")
                    Log.d("TAG", topicList.toString())
                    val topicAdapter = TopicAdapter(requireContext(), topicList)
                    rvTopic.layoutManager = LinearLayoutManager(requireContext())
                    rvTopic.adapter = topicAdapter
                }
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
        return view
    }
}