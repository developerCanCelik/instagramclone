package com.cancelik.insatagramclone.share

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_video.view.*
import org.greenrobot.eventbus.EventBus
import java.io.File

@Suppress("DEPRECATION")
class ShareVideoFragment : Fragment() {
    var videoView : CameraView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_share_video, container, false)
        videoView = view.videoView
        var createVideoFileName = System.currentTimeMillis()
        var createVideoFile = File(Environment.getExternalStorageDirectory().absolutePath+"/Pictures/Screenshots/"+createVideoFileName+".mp4")
        videoView!!.addCameraListener(object : CameraListener(){
            override fun onVideoTaken(video: File?) {
                super.onVideoTaken(video)
                activity!!.shareActivityRootConstraintLayout.visibility =  View.GONE
                activity!!.shareActivityContainerFrameLayout.visibility = View.VISIBLE
                var transaction = activity!!.supportFragmentManager.beginTransaction()
                EventBus.getDefault().postSticky(EventbusDataEvents.PaylasilacakResmiGonder(video!!.absolutePath.toString(),false))
                transaction.replace(R.id.shareActivityContainerFrameLayout,ShareNextFragment())
                transaction.addToBackStack("ShareNextFragment")
                transaction.commit()

            }

        })
        view.takeVideoView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event!!.action == MotionEvent.ACTION_DOWN) {
                    //parmağım basılı durduğu an video çekim işlemi
                    //video çekmek işlemi yapıyorum
                    videoView!!.startCapturingVideo(createVideoFile)
                    Toast.makeText(activity, "video Kayıt işlemi başladı", Toast.LENGTH_SHORT)
                        .show()
                    return true
                } else if (event!!.action == MotionEvent.ACTION_UP) {
                    //parmağimi kaldırdığımda da video çekmeyi bırakmak için
                    videoView!!.stopCapturingVideo()
                    Toast.makeText(activity, "video kayededildi", Toast.LENGTH_SHORT).show()
                    return true
                }
                //false gördüğü an onTouch dan çıkılacak
                return false
            }

        })
        view.galleryFragmentImageClose.setOnClickListener{
            activity!!.onBackPressed()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        videoView!!.start()
    }

    override fun onPause() {
        super.onPause()
        videoView!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (videoView != null){
            videoView!!.destroy()
        }
    }

}