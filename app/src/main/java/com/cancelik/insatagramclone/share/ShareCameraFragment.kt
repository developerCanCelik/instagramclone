package com.cancelik.insatagramclone.share

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.Gesture
import com.otaliastudios.cameraview.GestureAction
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_camera.*
import kotlinx.android.synthetic.main.fragment_share_camera.view.*
import kotlinx.android.synthetic.main.fragment_share_gallery.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileOutputStream


@Suppress("DEPRECATION")
class ShareCameraFragment : Fragment() {

    var cameraView : CameraView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =  inflater.inflate(R.layout.fragment_share_camera, container, false)
        cameraView = view.cameraView
        cameraView!!.mapGesture(Gesture.PINCH,GestureAction.ZOOM)
        view.takePhotoView.setOnClickListener {
            cameraView!!.capturePicture()
        }
        view.galleryFragmentImageClose.setOnClickListener{
            activity!!.onBackPressed()
        }

        cameraView!!.addCameraListener(object : CameraListener(){
            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)
                var takePhotoName = System.currentTimeMillis()
                var takePhoto = File(Environment.getExternalStorageDirectory().absolutePath+"/Pictures/Screenshots/"+takePhotoName+".jpg")
                //byte fotoğraf çevirir
                var fileCreate = FileOutputStream(takePhoto)
                fileCreate.write(jpeg)
                fileCreate.close()
                activity!!.shareActivityRootConstraintLayout.visibility =  View.GONE
                activity!!.shareActivityContainerFrameLayout.visibility = View.VISIBLE
                var transaction = activity!!.supportFragmentManager.beginTransaction()
                EventBus.getDefault().postSticky(EventbusDataEvents.PaylasilacakResmiGonder(takePhoto.absolutePath.toString(),true))
                transaction.replace(R.id.shareActivityContainerFrameLayout,ShareNextFragment())
                transaction.addToBackStack("ShareNextFragment")
                transaction.commit()
                println("Çekilen Fotoğraf Adı: ${takePhoto}")
            }
        })
        return view
    }
    override fun onResume() {
        super.onResume()
        cameraView!!.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView!!.stop();
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraView != null){
            cameraView!!.destroy()
        }
    }



}