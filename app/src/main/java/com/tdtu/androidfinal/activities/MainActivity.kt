package com.tdtu.androidfinal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tdtu.androidfinal.R
import com.tdtu.androidfinal.databinding.ActivityMainBinding
import com.tdtu.androidfinal.fragments.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        if(Firebase.auth.currentUser == null){
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }else{
            binding.bottomNavigationView.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.home -> replaceFragment(HomeFragment())
                    R.id.topic -> replaceFragment(TopicFragment())
                    R.id.folder -> replaceFragment(FolderFragment())
                    R.id.profile -> replaceFragment(ProfileFragment())
                    R.id.add -> startActivity(Intent(applicationContext, AddTopicActivity::class.java))

                    else -> {

                    }
                }
                true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}