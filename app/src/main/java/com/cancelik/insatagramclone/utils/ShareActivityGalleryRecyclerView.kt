package com.cancelik.insatagramclone.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.cancelik.insatagramclone.R
import kotlinx.android.synthetic.main.single_column_grid_image.view.*
import kotlinx.coroutines.awaitCancellation
import org.greenrobot.eventbus.EventBus

class ShareActivityGalleryRecyclerView(var folderFiles : ArrayList<String>, var context : Context) :
    RecyclerView.Adapter<ShareActivityGalleryRecyclerView.ViewHolder>() {
    lateinit var inflater : LayoutInflater
    init {
        inflater = LayoutInflater.from(context)
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        //gelen layoutun parçalarına ayırma işlemi yapıcaz
        //as constraint layout yapmamızın nedeni ise sınıfı özelleştirmekten dolayı oluyor
        var singleColumnFile = view as ConstraintLayout
        var fileImage = singleColumnFile.columImageGridView
        var videoTime = singleColumnFile.textViewSure
        var progressBar = singleColumnFile.single_column_grid_image_progressBar


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //ilgili positiona göre arraylistten atamaları yapıcağız
        var filePaths = folderFiles[position]
        var fileType = filePaths.substring(filePaths.lastIndexOf("."))
        if (fileType.equals(".mp4")){
            holder.videoTime.visibility = View.VISIBLE
            var retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, Uri.parse("file://"+filePaths))
            var videoTime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            var videoTimeLong = videoTime.toLong()
            holder.videoTime.text = convertDuraction(videoTimeLong)
            var imgUrl = folderFiles.get(position)
            UniversalImageLoader.setImage(filePaths,holder.fileImage,holder.progressBar,"file:/")
        }else{
            holder.videoTime.visibility = View.GONE
            var imgUrl = folderFiles.get(position)
            UniversalImageLoader.setImage(filePaths,holder.fileImage,holder.progressBar,"file:/")
        }

        holder.singleColumnFile.setOnClickListener{
           // Toast.makeText(context, "SeçilenVeriYolu ${folderFiles}", Toast.LENGTH_SHORT).show()
            EventBus.getDefault().postSticky(EventbusDataEvents.GaleriSecilenDosyaYolunuGonder(filePaths))
        }





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //öğelerin ilk defa kullanılıp kullanılmadığını kontrol ediyoruz
        var view =inflater.inflate(R.layout.single_column_grid_image,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return folderFiles.size
    }

    fun convertDuraction ( duration: Long): String{
        val second = duration / 1000 % 60
        val minute = duration / (1000 * 60) % 60
        val hour = duration / (1000 * 60 * 60) % 24

        var time=""
        if(hour>0){
            time = String.format("%02d:%02d:%02d", hour, minute, second)
        }else {
            time = String.format("%02d:%02d", minute, second)
        }

        return time
    }


}