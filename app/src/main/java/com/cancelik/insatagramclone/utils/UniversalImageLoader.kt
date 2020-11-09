package com.cancelik.insatagramclone.utils

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import com.cancelik.insatagramclone.R
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener

class UniversalImageLoader(val mContext: FragmentActivity?) {
    val config : ImageLoaderConfiguration
        get() {
            val options = DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                    //görseller getirilirken daha az kalitede gelmesini söylüyoruz
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(FadeInBitmapDisplayer(1200)).build()

            return ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(options)
                .memoryCache(WeakMemoryCache())
                .diskCacheSize(100*1024*1024).build()
        }
    companion object {
            private val defaultImage = R.drawable.jpg
            //Şu anlık drawable dan aldığımdan dolayı imgURL kısmını integer yaptık.
            fun setImage(imgURL: String, imageView: ImageView, mProgressBar: ProgressBar?, ilk:String?){
                val imageLoader = ImageLoader.getInstance()
                val url = ilk+imgURL
                imageLoader.displayImage(url,imageView,object : ImageLoadingListener{
                    override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                        if(mProgressBar != null) mProgressBar.visibility = View.GONE
                    }
                    override fun onLoadingStarted(imageUri: String?, view: View?) {
                        if(mProgressBar != null){
                            mProgressBar.visibility = View.VISIBLE
                        }
                    }
                    override fun onLoadingCancelled(imageUri: String?, view: View?) {
                        if(mProgressBar != null){
                            mProgressBar.visibility = View.GONE
                        }                    }
                    override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
                        if(mProgressBar != null){
                            mProgressBar.visibility = View.GONE
                        }
                    }

                })
            }
        }
}