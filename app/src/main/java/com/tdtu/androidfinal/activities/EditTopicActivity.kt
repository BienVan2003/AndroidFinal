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
import android.widget.Switch
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.adapters.AddCardAdapter
import com.tdtu.androidfinal.models.Card
import com.tdtu.androidfinal.models.Topic
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class EditTopicActivity : AppCompatActivity() {
    private lateinit var cardAdapter: AddCardAdapter
    private lateinit var editTopic: Topic

    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    private lateinit var tvCsv: TextView
    private lateinit var switchPublic: Switch
    private lateinit var fab: View
    private lateinit var recyclerView: RecyclerView
    private val REQUEST_CODE = 1
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

        (intent?.extras?.getSerializable("TOPIC") as Topic).let {
            editTopic = Topic(it.id, it.userId, it.username, it.title, it.description, it.cardList, it.isPublic)
        }

        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        tvCsv = findViewById(R.id.tvCsv)
        switchPublic = findViewById(R.id.switchPublic)
        fab = findViewById(R.id.fab)
        recyclerView = findViewById(R.id.recyclerView)

        edtTitle.setText(editTopic.title)
        edtDescription.setText(editTopic.description)
        switchPublic.isChecked = editTopic.isPublic

        recyclerView.isNestedScrollingEnabled = false;
        cardAdapter = AddCardAdapter(this, editTopic.cardList, supportActionBar) // cardList là danh sách các đối tượng Card
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cardAdapter

        val itemTouchHelper = ItemTouchHelper(object : SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                editTopic.cardList.removeAt(position)
                cardAdapter.notifyItemRemoved(position)

            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)

        fab.setOnClickListener {
            editTopic.cardList.add(Card("", "", ""))
            val itemPosition = editTopic.cardList.size
            cardAdapter.notifyItemInserted(itemPosition)
            supportActionBar?.title = "${itemPosition}/${itemPosition}"
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
        switchPublic.setOnCheckedChangeListener { buttonView, isChecked ->
            editTopic.isPublic = isChecked
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
                handleEditTopic()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleEditTopic() {
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
            val title = edtTitle.text.toString()
            val description = edtDescription.text.toString()

            editTopic.title = title
            editTopic.description = description

            Firebase.firestore.collection("topics")
                .document(editTopic.id)
                .set(editTopic)
                .addOnSuccessListener {
                    setResult(RESULT_OK, Intent().putExtra("TOPIC", editTopic))
                    finish()
                }
                .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
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
            editTopic.cardList.clear()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                // Phân tách dữ liệu dựa trên dấu phẩy
                val columns = line!!.split(",")
                if (columns.size == 2) {
                    val card = Card(columns[0], columns[1], "")
                    editTopic.cardList.add(card)
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

    override fun onDestroy() {
        super.onDestroy()
        cardAdapter.release()
        recyclerView.adapter = null
    }

}