package com.tdtu.androidfinal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tdtu.androidfinal.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val user = Firebase.auth.currentUser
        user?.let {
            val name = it.displayName
            val email = it.email
            val uid = it.uid
            binding.inputLayoutUser.editText?.setText(uid)
            binding.inputLayoutEmail.editText?.setText(email)
            binding.inputLayoutUsername.editText?.setText(name)
        }

        binding.btnChangePassword.setOnClickListener {
            startActivity(Intent(applicationContext, ChangePasswordActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Đăng xuất")
            builder.setMessage("Bạn chắc chắn muốn đăng xuất không?")

            builder.setPositiveButton("Đăng xuất") { dialog, which ->
                // Xác nhận đăng xuất
                Firebase.auth.signOut()

                // Đặt cờ để xóa history và tạo một ngăn xếp mới
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }

            builder.setNegativeButton("Hủy") { dialog, which ->
                // Người dùng đã chọn hủy bỏ, không làm gì cả
            }

            // Hiển thị AlertDialog
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }


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