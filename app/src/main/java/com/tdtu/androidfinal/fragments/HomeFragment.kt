package com.tdtu.androidfinal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.tdtu.androidfinal.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        requireActivity().title = "Trang chủ"
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}