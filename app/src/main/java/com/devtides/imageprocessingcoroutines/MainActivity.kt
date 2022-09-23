package com.devtides.imageprocessingcoroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.devtides.imageprocessingcoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var binding: ActivityMainBinding
    companion object {
        private const val ImageUrl = "https://raw.githubusercontent.com/DevTides/JetpackDogsApp/master/app/src/main/res/drawable/dog.png"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        coroutineScope.launch {
            val originalDeferred = coroutineScope.async(Dispatchers.IO) { getOriginalBitmap()  }

            val originalBitmap = originalDeferred.await()

            val filteredDeferred = coroutineScope.async(Dispatchers.Default) {applayFilter(originalBitmap)  }

            val filteredBitmap = filteredDeferred.await()

            loadImage(filteredBitmap)
        }
    }

    private fun getOriginalBitmap()=
        URL(ImageUrl).openStream().use{
            BitmapFactory.decodeStream(it)
        }

    private fun applayFilter(originalBitmap: Bitmap) = Filter.apply(originalBitmap)

    private fun loadImage(bmp: Bitmap){
        binding.progressBar.visibility = View.GONE
        binding.imageView.setImageBitmap(bmp)
        binding.imageView.visibility = View.VISIBLE
    }



}
