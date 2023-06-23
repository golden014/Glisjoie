package edu.bluejack22_2.Glisjoie.ViewModel

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.bluejack22_2.Glisjoie.Model.ImageModel
import edu.bluejack22_2.glisjoie.R
import edu.bluejack22_2.Glisjoie.View.books.CreateBookFragment

class ImageSelectionViewModel(private val fragment: CreateBookFragment) : ViewModel() {


    private val emptyImage = R.drawable.image
    private var defaultModel = ImageModel(emptyImage, null, null)
    private lateinit var pickImage : ActivityResultLauncher<String>
    var imageData : MutableLiveData<ImageModel> = MutableLiveData<ImageModel>(defaultModel)

    init {
        pickImage = fragment.registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            imageData.value = ImageModel(R.drawable.image, uri, null)
        }
    }

    fun selectImage() {
        pickImage.launch("image/*")
    }

    fun setView(imageView : ImageView){
        Log.wtf("ImageSelection", "ID: " + imageData.value?.imageId.toString())
        Log.wtf("ImageSelection", "Bitmap: " +imageData.value?.imageBitMap.toString())
        Log.wtf("ImageSelection", "URI: " +imageData.value?.imageUri.toString())
        if(imageData.value?.imageBitMap != null){
            Log.wtf("ImageSelection", imageData.value?.imageBitMap.toString())
            imageData.value?.imageBitMap.let { imageView.setImageBitmap(it) }
        }else if(imageData.value?.imageUri != null){
            imageData.value?.imageUri.let { imageView.setImageURI(it) }
        }else{
            imageData.value?.imageId?.let { imageView.setImageResource(it) }
        }
    }

    class Factory(private val fragment: CreateBookFragment) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ImageSelectionViewModel::class.java)) {
                return ImageSelectionViewModel(fragment) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}