package com.kkyume.android.imagecollecter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.kkyume.android.imagecollecter.model.image.ImageRequest

class MainActivity : AppCompatActivity() {
    private var mViewModel: ImageViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        init()
        mViewModel?.requestImageResponse(ImageRequest("IU","recency",10, 10))

        initLiveData()
    }
    private fun init(){
        mViewModel =  (this as? Activity)?.let { ImageViewModel(it.application) }

    }
    private fun initLiveData() {
        mViewModel?.let { viewModel ->
            viewModel.imageLiveData.observe(this as LifecycleOwner) { res ->
                res?.let {
                    println(it.documents.get(0))
                }
            }
        }
    }
}