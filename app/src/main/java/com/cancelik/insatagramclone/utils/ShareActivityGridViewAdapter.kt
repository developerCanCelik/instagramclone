package com.cancelik.insatagramclone.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.cancelik.insatagramclone.R
import kotlinx.android.synthetic.main.single_column_grid_image.view.*
import java.lang.Exception
import java.time.Duration

/*
class ShareActivityGridViewAdapter(context: Context, resource: Int, var folderFiles : ArrayList<String>) : ArrayAdapter<String>(context, resource, folderFiles) {

    var inflater : LayoutInflater
    lateinit var viewHolder : ViewHolder
    var single_column_image : View? = null
    init {
        inflater = LayoutInflater.from(context)
    }
    inner class ViewHolder(){
        lateinit var image : GridPrivateImageView
        lateinit var progressBar : ProgressBar
        lateinit var timeTextView : TextView
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        single_column_image = convertView
        if (single_column_image == null){
            single_column_image = inflater.inflate(R.layout.single_column_grid_image,parent,false)
            viewHolder = ViewHolder()
            viewHolder.image = single_column_image!!.columImageGridView
            viewHolder.progressBar = single_column_image!!.single_column_grid_image_progressBar
            viewHolder.timeTextView = single_column_image!!.textViewSure
            single_column_image!!.setTag(viewHolder)
        }
        else{
            viewHolder = single_column_image!!.getTag() as ViewHolder

        }
        //video süre için çalıştık
        var file_paths = folderFiles[position]
        var fileType :String? = file_paths.substring(file_paths.lastIndexOf("."))
        if (fileType.equals(".mp4")){
            viewHolder.timeTextView.visibility = View.VISIBLE
            var retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, Uri.parse("file://"+file_paths))
            var videoTime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            var videoTimeLong = videoTime.toLong()
            viewHolder.timeTextView.text = convertDuraction(videoTimeLong)
            var imgUrl = folderFiles.get(position)
            UniversalImageLoader.setImage(imgUrl,viewHolder.image,viewHolder.progressBar,"file:/")
        }else{
            viewHolder.timeTextView.visibility = View.GONE
            var imgUrl = folderFiles.get(position)
            UniversalImageLoader.setImage(imgUrl,viewHolder.image,viewHolder.progressBar,"file:/")
        }


        return single_column_image!!

    }
    //şarkı video süresini bulma
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

 */