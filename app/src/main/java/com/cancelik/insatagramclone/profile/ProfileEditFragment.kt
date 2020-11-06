package com.cancelik.insatagramclone.profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.model.Users
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import com.cancelik.insatagramclone.utils.UniversalImageLoader
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.nostra13.universalimageloader.core.ImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.biographyText
import kotlinx.android.synthetic.main.fragment_profile_edit.view.profile_image
import kotlinx.android.synthetic.main.fragment_register_form.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception

class ProfileEditFragment : Fragment() {
    //lateinit var circleImageViewFragment: CircleImageView
    lateinit var incomingUsersInformation : Users
    val ImageAdd = 1
    lateinit var databaseRef : DatabaseReference

    lateinit var storageRef : StorageReference
    var profilePhotoURI : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //elemanlara erişeceğimden dolayı return kısmına view yolladık daha sonra eleman erişme durumunu yapacağız
        val view = inflater.inflate(R.layout.fragment_profile_edit,container,false)
        databaseRef = FirebaseDatabase.getInstance().reference

        storageRef = FirebaseStorage.getInstance().reference

        setupUserInformation(view)
        /*
        circleImageViewFragment = view.findViewById(R.id.profile_image)
        setupProfilePicture()
         */
        view.image_close.setOnClickListener {
            activity?.onBackPressed()

        }
        view.imageChange.setOnClickListener {
            var intent = Intent()
            intent.setType("image/")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent,ImageAdd)
        }
        view.image_check.setOnClickListener {
            if (profilePhotoURI != null){
                var dialogUpload = UploadFragment()
                dialogUpload.show(activity!!.supportFragmentManager,"uploadFragment")
                //aşağıdakinin amacı resim firebase ye yüklenesiye kadar iptal edilememsi için
                dialogUpload.isCancelable = false
                //storadgeyi kaydediyoruz
                storageRef.child("users").child(incomingUsersInformation.user_id!!)
                    .child(profilePhotoURI!!.lastPathSegment!!)
                    .putFile(profilePhotoURI!!)
                    .addOnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener {
                            val downloadUri : String = it.toString()
                            databaseRef.child("users").child(incomingUsersInformation.user_id!!).child("userDetails")
                                .child("profile_picture").setValue(downloadUri).addOnCompleteListener {
                                    if (it.isSuccessful){
                                        Toast.makeText(activity, "Yükleme Başarılı oldu", Toast.LENGTH_SHORT).show()
                                        dialogUpload.dismiss()
                                        //true güncellenip güncellendiğini söyledik
                                        userNameUpdate(view,true)
                                    }
                                    else{
                                        Toast.makeText(activity, "Yükleme Başarısız oldu", Toast.LENGTH_SHORT).show()
                                        userNameUpdate(view,false)

                                    }
                                }
                        }
                    }
            }else{
                userNameUpdate(view,null)
            }
        }
        return view
    }

    private fun userNameUpdate(view: View, profileImageUpdate: Boolean?) {
        if (!incomingUsersInformation.user_name!!.equals(view.userNameText.text.toString())){
            databaseRef.child("users").orderByChild("user_name").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var usedUserName = false
                        for (userNameControl in snapshot.children) {
                            var userParsed =
                                userNameControl.getValue(Users::class.java)!!.user_name
                            if (userParsed!!.equals(view.userNameText.text.toString())) {
                                //eğer for döngüsünden gelen user name ile yazılan eşit ise
                                usedUserName = true
                                Toast.makeText(activity, "Kullanıcı adı daha önce alınmış", Toast.LENGTH_SHORT).show()
                                profileInformationUpdate(view, profileImageUpdate , false)
                                break
                            }
                        }
                        if (usedUserName == false){
                            databaseRef.child("users").child(incomingUsersInformation.user_id!!).child("user_name").setValue(view.userNameText.text.toString())
                            profileInformationUpdate(view, profileImageUpdate , true)
                        }
                    }


                })
        }
        else{
            profileInformationUpdate(view,profileImageUpdate,null)
        }
    }

    private fun profileInformationUpdate(view: View, profileImageUpdate: Boolean?, profileUserNameUpdate: Boolean?) {
        var profileUpdate : Boolean?  = null
        if (!incomingUsersInformation.name_surname!!.equals(view.nameText.text.toString())){
            databaseRef.child("users").child(incomingUsersInformation.user_id!!).child("name_surname").setValue(view.nameText.text.toString())
            profileUpdate = true
        }
        if (!incomingUsersInformation.userDetails!!.biography.equals(view.biographyText.text.toString())){
            databaseRef.child("users").child(incomingUsersInformation.user_id!!).child("userDetails").child("biography").setValue(view.biographyText.text.toString())
            profileUpdate = true
        }
        if (!incomingUsersInformation.userDetails!!.web_site.equals(view.InternetText.text.toString())){
            databaseRef.child("users").child(incomingUsersInformation.user_id!!).child("userDetails").child("web_site").setValue(view.InternetText.text.toString())
            profileUpdate = true
        }
        if (profileImageUpdate == null && profileUserNameUpdate == null && profileUpdate == null){
            Toast.makeText(activity, "Hicbir Değişiklik Yapılmadı", Toast.LENGTH_LONG).show()
        }
        else if (profileUserNameUpdate == false && (profileImageUpdate== true || profileUpdate == true)){
            Toast.makeText(activity, "Bilgiler güncellendi lakin kullanıcı adı kullanımda", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(activity, "Kullanıcı Güncellendi", Toast.LENGTH_SHORT).show()
            activity!!.onBackPressed()
        }


    }


    private fun setupUserInformation(view: View) {
        view.nameText.setText(incomingUsersInformation.name_surname)
        view.userNameText.setText(incomingUsersInformation.user_name)
        if (!incomingUsersInformation.userDetails!!.biography.isNullOrEmpty()){
            view.biographyText.setText(incomingUsersInformation.userDetails!!.biography)
        }
        if (!incomingUsersInformation.userDetails!!.web_site.isNullOrEmpty()){
            view.InternetText.setText(incomingUsersInformation.userDetails!!.web_site)
        }
        val imgUrl : String? = incomingUsersInformation.userDetails!!.profile_picture
        if (!incomingUsersInformation.userDetails!!.profile_picture.isNullOrEmpty()){
            UniversalImageLoader.setImage(imgUrl!!,view.profile_image,null,"")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImageAdd && resultCode == Activity.RESULT_OK && data!!.data != null){
            profilePhotoURI = data!!.data
            view!!.profile_image.setImageURI(profilePhotoURI)
        }
    }


    private fun setupProfilePicture() {
        /*
        //https://resimdiyari.com/upload/2014/06/18/20140618121439-338f255e.jpg
        val ilk="https://"
        val imgUrl= "resimdiyari.com/upload/2014/06/18/20140618121439-338f255e.jpg"
        UniversalImageLoader.setImage(imgUrl,circleImageViewFragment,null,ilk)

         */
    }
    //Eventbus kısmı
    @Subscribe(sticky = true)
    internal fun onKullaniciBilgileriEvent(usersInformation : EventbusDataEvents.KullaniciBilgileriniGonder){
        incomingUsersInformation = usersInformation!!.kullanici!!



    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

}