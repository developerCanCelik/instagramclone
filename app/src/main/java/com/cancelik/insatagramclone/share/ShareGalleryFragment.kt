package com.cancelik.insatagramclone.share

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.size
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import com.cancelik.insatagramclone.utils.FileOperations
import com.cancelik.insatagramclone.utils.ShareActivityGridViewAdapter
import com.cancelik.insatagramclone.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_gallery.*
import kotlinx.android.synthetic.main.fragment_share_gallery.view.*
import org.greenrobot.eventbus.EventBus

@Suppress("DEPRECATION")
class ShareGalleryFragment : Fragment() {
    lateinit var foldersPaths : ArrayList<String>
    //DEğişiklik var**************************************************************
    var selectImagePaths : String? = null
    var fileTypeImage : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Bunun amacı constraint layout'un 10 6 sını kullan dedik sını kullan diyoruz veya yüzde 60 dedik
        //app:layout_constraintHeight_percent="0.6"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_share_gallery, container, false)
        //spinner da klasörlerimizi gösteriyoruz
        foldersPaths = ArrayList<String>()
        val foldersName = ArrayList<String>()
        //sd karta uşabilmek için yapılan klasör yolu
        var root = Environment.getExternalStorageDirectory().path
        //println("konum ${root}")
        var cameraImage = root+"/DCIM/Camera"
        var downloadImage = root+"/Download"
        var whatsappImage = root+"/Whatsapp/Media/WhatsApp Images"
        var screenshots = root+"/Pictures/Screenshots"

        foldersPaths.add(cameraImage)
        foldersPaths.add(downloadImage)
        foldersPaths.add(whatsappImage)
        foldersPaths.add(screenshots)


        foldersName.add("Kamera")
        foldersName.add("Indirilenler")
        foldersName.add("Whatsapp")
        foldersName.add("Ekran Görüntüleri")



        var spinnerArrayAdaptor = ArrayAdapter(activity!!,android.R.layout.simple_spinner_item,foldersName)
        spinnerArrayAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.gallerySpinner.adapter = spinnerArrayAdaptor
        view.gallerySpinner.setSelection(0)
        view.gallerySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    setupGridView( FileOperations.incomingFolderFiles(foldersPaths[position]))
            }
            //
        }
        view.galleryIleriTextView.setOnClickListener {
            activity!!.shareActivityRootConstraintLayout.visibility =  View.GONE
            activity!!.shareActivityContainerFrameLayout.visibility = View.VISIBLE
            var transaction = activity!!.supportFragmentManager.beginTransaction()

            EventBus.getDefault().postSticky(EventbusDataEvents.PaylasilacakResmiGonder(selectImagePaths,fileTypeImage))

            transaction.replace(R.id.shareActivityContainerFrameLayout,ShareNextFragment())
            transaction.addToBackStack("ShareNextFragment")
            transaction.commit()

        }




        return view
    }
    //değişen

    fun setupGridView(file_path : ArrayList<String>){
        var gridAdapter = ShareActivityGridViewAdapter(activity!!,R.layout.single_column_grid_image,file_path)
        galleryImageGridView.adapter = gridAdapter
        //ilk açılan videoyu getiriyor
        //eğer içi boş değil ise çalışıyor
        if (gridAdapter.folderFiles.size > 0){
            selectImagePaths = file_path[0]
            imageOrVideoIn(file_path[0])
        }
        galleryImageGridView.setOnItemClickListener(object  : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //UniversalImageLoader.setImage(file_path[position],galleryImageView,null,"file:/")
                selectImagePaths = file_path[position]
                imageOrVideoIn(file_path[position])
            }

        })

    }
    //değişen
    private fun imageOrVideoIn(filePaths: String) {
        var fileType :String? = filePaths.substring(filePaths.lastIndexOf("."))
        if (fileType != null){
            if (fileType.equals(".mp4")){
                fileTypeImage = false
                view!!.videoView.visibility = View.VISIBLE
                view!!.imageCropView!!.visibility = View.GONE
                videoView.setVideoURI(Uri.parse("file://"+filePaths))
                videoView.start()
            }
            else{
                fileTypeImage = true
                view!!.videoView.visibility = View.GONE
                view!!.imageCropView!!.visibility = View.VISIBLE
                UniversalImageLoader.setImage(filePaths,imageCropView,null,"file:/")
            }
        }

    }


}