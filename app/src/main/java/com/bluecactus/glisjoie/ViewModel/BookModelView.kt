package com.bluecactus.glisjoie.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluecactus.glisjoie.R

class BookModelView : ViewModel() {
    val imageID = MutableLiveData<Int>(R.drawable.image)
    init {

    }
}