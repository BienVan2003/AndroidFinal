package com.tdtu.androidfinal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.tdtu.androidfinal.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginNow.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        auth = Firebase.auth

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val rePassword = binding.edtRePassword.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Please enter email", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Please enter password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else if (rePassword.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Please enter rePassword", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (!isValidEmail(email)) {
                // Địa chỉ email không hợp lệ
                Toast.makeText(this@RegisterActivity, "Invalid email address", Toast.LENGTH_SHORT)
                    .show()
                binding.progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (password.length < 6) {
                // Mật khẩu quá ngắn
                Toast.makeText(
                    this@RegisterActivity,
                    "Password must be at least 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (password != rePassword) {
                // Mật khẩu và mật khẩu nhập lại không khớp
                Toast.makeText(this@RegisterActivity, "Passwords do not match", Toast.LENGTH_SHORT)
                    .show()
                binding.progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val username = extractUsernameFromEmail(email)
                        val user = Firebase.auth.currentUser
                        val profileUpdates = userProfileChangeRequest {
                            displayName = username
                        }
                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("TAG", "User profile updated.")
                                }
                            }
                        Toast.makeText(
                            this@RegisterActivity,
                            "Account created.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this@RegisterActivity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun extractUsernameFromEmail(email: String): String? {
        val atIndex = email.indexOf('@')

        // Kiểm tra xem có ký tự '@' trong địa chỉ email hay không
        if (atIndex != -1) {
            // Lấy phần trước ký tự '@' làm tên người dùng
            return email.substring(0, atIndex)
        }

        // Trả về null nếu không tìm thấy ký tự '@'
        return null
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

}