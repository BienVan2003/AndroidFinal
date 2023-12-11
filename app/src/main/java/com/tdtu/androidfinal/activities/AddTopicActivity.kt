package com.tdtu.androidfinal.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.adapters.CardAdapter
import com.tdtu.androidfinal.models.Card
import com.tdtu.androidfinal.models.Topic
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class AddTopicActivity : AppCompatActivity() {
    private lateinit var cardList: ArrayList<Card>
    private lateinit var cardAdapter: CardAdapter
    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    private lateinit var tvCsv: TextView
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedFileUri = data?.data
                selectedFileUri?.let { readFileFromUri(it) }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cardList = ArrayList()
        cardList.add(Card("", "", ""))
        cardList.add(Card("", "", ""))
        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        tvCsv = findViewById(R.id.tvCsv)
        val fab: View = findViewById(R.id.fab)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.isNestedScrollingEnabled = false;
        cardAdapter = CardAdapter(this, cardList, supportActionBar!!) // cardList là danh sách các đối tượng Card
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cardAdapter



        fab.setOnClickListener {
            cardList.add(Card("", "", ""))
            val itemPosition = cardList.size
            cardAdapter.notifyItemChanged(itemPosition)
            supportActionBar?.title = "${itemPosition}/${itemPosition}"
            Log.e("TAG", cardList.toString())
        }

        // Khởi tạo ActivityResultLauncher để yêu cầu quyền
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Quyền được cấp, bạn có thể thực hiện các hành động liên quan đến quyền ở đây
                openFilePicker()
            } else {
                // Người dùng từ chối quyền
            }
        }
        tvCsv.setOnClickListener {
            requestStoragePermission()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.add_topic_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Xử lý khi nút "Back" được nhấn
                finish()
                true
            }
            R.id.miSave -> {
                createTopic()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createTopic() {
        if (edtTitle.text.toString().isEmpty()) {
            // Hiển thị AlertDialog khi tiêu đề trống
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Thêm tiêu đề để hoàn thành học phần.")
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                // Đóng dialog khi người dùng nhấn OK
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
            Firebase.firestore.collection("topics")
                .add(Topic(uid, username, title, description, cardList))
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                }
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                // Kiểm tra xem quyền đã được cấp hay chưa
                PackageManager.PERMISSION_GRANTED == checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // Quyền đã được cấp, mở trình quản lý tệp tin
                    openFilePicker()
                }
                else -> {
                    // Yêu cầu quyền
                    requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*" // Chỉ cho phép chọn file có định dạng CSV
        filePickerLauncher.launch(intent)
    }

    private fun readFileFromUri(uri: Uri) {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))
//            val dataList: MutableList<Pair<String, String>> = ArrayList()
            cardList.clear()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                // Phân tách dữ liệu dựa trên dấu phẩy
                val columns = line!!.split(",")
                if (columns.size == 2) {
                    val card = Card(columns[0], columns[1], "")
                    cardList.add(card)
                }
            }

            // Đóng các nguồn dữ liệu
            reader.close()
            inputStream?.close()
            cardAdapter.notifyDataSetChanged()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}