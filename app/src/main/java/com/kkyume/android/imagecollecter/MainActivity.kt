package com.kkyume.android.imagecollecter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.kkyume.android.imagecollecter.adapter.ViewPagerAdapter
import com.kkyume.android.imagecollecter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()

    }
    private fun init() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(SearchFragment(), "검색")
        adapter.addFragment(StorageFragment(), "즐겨찾기")
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }
}