package com.tdtu.androidfinal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.tdtu.androidfinal.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val user = FirebaseAuth.getInstance().currentUser

        binding.btnSave.setOnClickListener {
            val currentPassword = binding.edtCurrentPassword.text.toString().trim()
            val newPassword = binding.edtNewPassword.text.toString().trim()
            val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Vui lòng nhập đầy đủ thông tin",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (newPassword.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Mật khẩu mới phải chứa ít nhất 6 kí tự",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(
                    applicationContext,
                    "Mật khẩu mới và xác nhận mật khẩu mới không khớp",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val credential = EmailAuthProvider.getCredential(user!!.email!!, currentPassword)

            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { passwordUpdateTask ->
                                if (passwordUpdateTask.isSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Thay đổi mật khẩu thành công",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    // Lỗi khi thay đổi mật khẩu
                                    Toast.makeText(
                                        applicationContext,
                                        "Lỗi thay đổi mật khẩu",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Lỗi xác thực người dùng",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
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