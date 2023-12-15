package com.tdtu.androidfinal.activities

import android.app.ActionBar
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.adapters.AddTopicToFolderAdapter
import com.tdtu.androidfinal.databinding.ActivityLoginBinding
import com.tdtu.androidfinal.models.Card
import com.tdtu.androidfinal.models.Folder
import com.tdtu.androidfinal.models.Topic

class AddFolderActivity : AppCompatActivity() {
    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_folder)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close)
            title = "Thư mục mới"
        }

        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)



    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.save_folder_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.miSaveFolder -> {
                createFolder()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createFolder() {
        Log.w("TAG", "Error adding document")

        if (edtTitle.text.toString().isEmpty()) {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Thêm tiêu đề để hoàn thành thư mục.")
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
            return
        }

        val user = Firebase.auth.currentUser
        user?.let {
            val uid = it.uid
            val username = it.displayName.toString()
            val title = edtTitle.text.toString()
            val description = edtDescription.text.toString()
            var newFolder = Folder("", uid,username,title,description, ArrayList())

            Firebase.firestore.collection("folders")
                .add(newFolder)
                .addOnSuccessListener { _ ->
                    val intent = Intent(this, DetailFolderActivity::class.java)
                    intent.putExtra("FOLDER", newFolder)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                }
        }
    }
}