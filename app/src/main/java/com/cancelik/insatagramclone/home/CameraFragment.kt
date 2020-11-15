package com.cancelik.insatagramclone.home

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.share.ShareNextFragment
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import com.otaliastudios.cameraview.*
import kotlinx.android.synthetic.main.activity_like.*
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File
import java.io.FileOutputStream

class CameraFragment : Fragment() {
    var cameraView : CameraView? = null
    var cameraAskForPermission : Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_camera,container,false)
        cameraView = view.homeCameraFragmentCameraView
        cameraView!!.mapGesture(Gesture.PINCH,GestureAction.ZOOM)
        cameraView!!.mapGesture(Gesture.TAP,GestureAction.FOCUS_WITH_MARKER)
        cameraView!!.addCameraListener(object  : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)
                var takePhotoName = System.currentTimeMillis()
                var takePhoto = File(Environment.getExternalStorageDirectory().absolutePath+"/Pictures/Screenshots/"+takePhotoName+".jpg")
                //byte fotoğraf çevirir
                var fileCreate = FileOutputStream(takePhoto)
                fileCreate.write(jpeg)
                fileCreate.close()
                println("Çekilen Fotoğraf Adı: ${takePhoto}")
            }
        })

        view.homeCameraFragmentRefreshView.setOnClickListener {
            if (cameraView!!.facing == Facing.BACK){
                //Eğer arka kamera açıksa ön butona geç
                cameraView!!.facing = Facing.FRONT
            }
            else{
                cameraView!!.facing = Facing.BACK
            }
        }
        view.homeCameraFragmentTakePhoto.setOnClickListener {
            if (cameraView!!.facing == Facing.BACK){
                cameraView!!.capturePicture()
            }
            else{
                cameraView!!.captureSnapshot()
            }
        }

        return view
    }
    @Subscribe(sticky = true)
    internal fun onKameraIzinBilgisi(cameraPermission: EventbusDataEvents.KameraIzinBilgisiGonder){
        cameraAskForPermission = cameraPermission.cameraPermissionInformation!!
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }
    override fun onResume() {
        super.onResume()
        if (cameraAskForPermission == true) {
            cameraView!!.start()
        }


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