package com.tdtu.androidfinal.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.activities.AddFolderActivity
import com.tdtu.androidfinal.adapters.FolderAdapter
import com.tdtu.androidfinal.databinding.ActivityMainBinding
import com.tdtu.androidfinal.databinding.FragmentFolderBinding
import com.tdtu.androidfinal.models.Card
import com.tdtu.androidfinal.models.Folder
import com.tdtu.androidfinal.models.Topic

class FolderFragment : Fragment() {
    private lateinit var folders: ArrayList<Folder>
    private lateinit var folderAdapter: FolderAdapter
    private lateinit var binding: FragmentFolderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFolderBinding.inflate(inflater, container, false)
        requireActivity().title = "Thư viện"
        setHasOptionsMenu(true)

        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_folder_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miAddFolder -> {
                val intent = Intent(requireContext(), AddFolderActivity::class.java)
                startActivityForResult(intent, 1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        folders = ArrayList()
        Firebase.firestore.collection("folders")
            .whereEqualTo("userId", Firebase.auth.currentUser?.uid)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    val folderData = document.data

                    val dataTopicArray = folderData["topics"] as? ArrayList<Map<String, Any>>
                    var tmpTopicList :ArrayList<Topic> = ArrayList()

                    dataTopicArray?.let {
                        for (map in it) {
                            // Đọc dữ liệu từng map
                            val id = map["id"] as String
                            val userId = map["userId"] as String
                            val username = map["username"] as String
                            val title = map["title"] as String
                            val description = map["description"] as String
                            val public:Boolean = map["public"] as Boolean

                            val dataArray = map["cardList"] as? ArrayList<Map<String, Any>>
                            var tmpCardList :ArrayList<Card> = ArrayList()

                            dataArray?.let {innerIt->
                                for (map in innerIt) {
                                    val term = map["term"] as String
                                    val define = map["define"] as String
                                    val image = map["image"] as String
                                    tmpCardList.add(Card(term, define, image))
                                }
                            }

                            val topic = Topic(id,userId,username,title,description,tmpCardList,public)
                            tmpTopicList.add(topic)
                        }

                    }

                    val folder = Folder(
                        id = document.id,
                        userId = folderData["userId"] as String,
                        username = folderData["username"] as String,
                        title = folderData["title"] as String,
                        description = folderData["description"] as String,
                        topics = tmpTopicList
                    )
                    folders.add(folder)
                }
                folderAdapter = FolderAdapter(requireContext(), folders)
                binding.rvFolder.layoutManager = LinearLayoutManager(requireContext())
                binding.rvFolder.adapter = folderAdapter
                binding.progressBar.visibility = View.GONE
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }
}