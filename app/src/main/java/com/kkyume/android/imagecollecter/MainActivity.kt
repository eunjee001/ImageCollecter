package com.kkyume.android.imagecollecter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.kkyume.android.imagecollecter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var mViewModel: ImageViewModel? = null
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()

        initLiveData()
    }
    private fun init(){
        mViewModel =  (this as? Activity)?.let { ImageViewModel(it.application) }
        mViewModel?.requestImageResponse()

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(SearchFragment(), "검색결과")
        adapter.addFragment(StorageFragment(), "내 보관함")
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
    private fun initLiveData() {
        mViewModel?.let { viewModel ->
            viewModel.imageLiveData.observe(this as LifecycleOwner) { res ->
                res?.let {
                    println(">>>" + it.meta)
                }
            }
        }
    }
}