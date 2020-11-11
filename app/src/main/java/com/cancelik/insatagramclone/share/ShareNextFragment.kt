package com.cancelik.insatagramclone.share

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.model.Post
import com.cancelik.insatagramclone.profile.UploadFragment
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import com.cancelik.insatagramclone.utils.FileOperations
import com.cancelik.insatagramclone.utils.UniversalImageLoader
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.iceteck.silicompressorr.SiliCompressor
import kotlinx.android.synthetic.main.fragment_share_next.view.*
import kotlinx.android.synthetic.main.fragment_upload.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File

@Suppress("DEPRECATION")
class ShareNextFragment : Fragment() {
    var selectImagePath : String? = null
    var selectVideoPath : String? = null
    var fileType : Boolean? = null
    lateinit var newFileUri : Uri
    lateinit var photoURI : Uri
    lateinit var videoURI : Uri
    lateinit var auth: FirebaseAuth
    lateinit var ref : DatabaseReference
    lateinit var storadgeRef : StorageReference
    lateinit var userInformation : FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        userInformation = auth.currentUser!!
        ref  = FirebaseDatabase.getInstance().reference
        storadgeRef = FirebaseStorage.getInstance().reference
        var view = inflater.inflate(R.layout.fragment_share_next, container, false)
        UniversalImageLoader.setImage(selectImagePath!!,view!!.nextImageView,null,"file://")
        //önceden photoUri kullanıyorduk
        photoURI = Uri.parse("file://"+selectImagePath)
        videoURI = Uri.parse("file://"+selectVideoPath)
        view.nextFragmentPaylas.setOnClickListener {
            uploadPhotoOrVideoToStorage()
        }
        return view
    }

    private fun uploadPhotoOrVideoToStorage() {
        val dialogUpload : UploadFragment
        if (fileType == true) {
            FileOperations.compressImageFile(this,photoURI)
        }
        else if(fileType == false){
            FileOperations.compressVideoFile(this,photoURI)

        }

    }


    private fun getDatabaseInFormation(downloadUri: String) {
        //ilk önce düğümü oluşturduk sonra bu düğüme eklemeler yapıcaz
        var postID = ref.child("posts").child(userInformation.uid).push().key
        var uploadPost = Post(userInformation.uid,postID,"",view!!.nextEditText.text.toString(),downloadUri)
        ref.child("posts").child(userInformation.uid).child(postID!!).setValue(uploadPost)
        //yüklenme tarihi için
        ref.child("posts").child(userInformation.uid).child(postID).child("upload_date").setValue(ServerValue.TIMESTAMP)

    }


    //Eventbus kısmı
    @Subscribe(sticky = true)
    internal fun onPaylasılacakResim(shareImage : EventbusDataEvents.PaylasilacakResmiGonder){
        selectImagePath = shareImage!!.chosenFilePath
        fileType = shareImage!!.fileTypeImage
        selectVideoPath = shareImage!!.chosenFilePath



    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    fun newUpload(newFileUri: Uri?) {
        var dialogUpload = UploadFragment()
        dialogUpload.show(activity!!.supportFragmentManager, "uploadFragment")
        dialogUpload.isCancelable = false
        var uploadTask = storadgeRef.child("users").child(userInformation.uid)
            .child(newFileUri!!.lastPathSegment!!)
            .putFile(newFileUri)
            .addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot> {
                override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                    if (p0.isSuccessful) {
                        var downloadUri = p0.getResult()!!.storage.downloadUrl.toString()
                        dialogUpload.dismiss()
                        getDatabaseInFormation(downloadUri)

                    }
                }
            })
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Toast.makeText(
                        activity,
                        "Yüklerken Bir Hata İle Karşılaştık : ${p0.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
            .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot> {
                override fun onProgress(snapshot: UploadTask.TaskSnapshot) {
                    var progress = 100 * snapshot.bytesTransferred / snapshot.totalByteCount
                    dialogUpload.uploadTextView.text = "%" + progress.toInt().toString() + "yüklendi..."
                }

            })
    }


}