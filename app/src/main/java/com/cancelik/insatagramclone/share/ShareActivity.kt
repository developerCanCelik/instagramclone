package com.cancelik.insatagramclone.share

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.login.LoginActivity
import com.cancelik.insatagramclone.utils.SharePagerAdaptor
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_share.*
import java.util.jar.Manifest

class ShareActivity : AppCompatActivity() {
    lateinit var sharePagerAdaptor : SharePagerAdaptor
    lateinit var auth: FirebaseAuth
    lateinit var authListener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        auth = FirebaseAuth.getInstance()
        storageAndCameraPermissions()
        setupAuthListener()

    }
    //Burada yazdık
    private fun storageAndCameraPermissions() {
        //activityi istiyor
        Dexter.withContext(this)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    //Bütün izinler verilmiş ise
                    if (p0!!.areAllPermissionsGranted()) {
                        println("Tüm izinler verilmiş ise")
                        //yukardan alındı
                        sharePagerAdaptor()
                    }
                    if (p0!!.isAnyPermissionPermanentlyDenied) {
                        //Burda izin vermesi için açıklama yapıyoruz bir daha sorma derse
                        println("Bir daha sorma demiş")
                        println("Tüm izinlerden birini vermemiş ise yine açıklama yap")
                        //sürekli sorması için yazıyoruz
                        //p1!!.continuePermissionRequest()
                        //birdaha sormayacak
                        //p1!!.cancelPermissionRequest()
                        var builder = AlertDialog.Builder(this@ShareActivity)
                        builder.setTitle("İzin vermeniz gerekiyor")
                        builder.setMessage("Ayarlar kısmından izin vermeniz gerekiyor")
                        builder.setPositiveButton(
                            "Onay Ver",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.cancel()
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    var uri = Uri.fromParts("package",packageName,null)
                                    intent.setData(uri)
                                    startActivity(intent)
                                    finish()

                                }
                            })
                        builder.setNegativeButton(
                            "Onay Verme",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.cancel()
                                    finish()
                                }
                            })
                        builder.show()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    println("Tüm izinlerden birini vermemiş ise yine açıklama yap")
                    //sürekli sorması için yazıyoruz
                    //p1!!.continuePermissionRequest()
                    //birdaha sormayacak
                    //p1!!.cancelPermissionRequest()
                    var builder = AlertDialog.Builder(this@ShareActivity)
                    builder.setTitle("İzin vermeniz gerekiyor")
                    builder.setMessage("Uygulamaya izin vermeniz gerekmekte Onaylıyor musunuz?")
                    builder.setPositiveButton(
                            "Onay Ver",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.cancel()
                                    p1!!.continuePermissionRequest()
                                }
                            })
                    builder.setNegativeButton(
                            "Onay Verme",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.cancel()
                                    p1!!.cancelPermissionRequest()
                                    finish()
                                }
                            })
                    builder.show()
                }

            })
            .withErrorListener(object : PermissionRequestErrorListener{
                override fun onError(p0: DexterError?) {
                    println(p0.toString())
                }

            }).check()

    //buraya kadar

    }

    private fun sharePagerAdaptor() {
        var tabLayoutName = ArrayList<String>()
        tabLayoutName.add(0,"GALLERY")
        tabLayoutName.add(1,"FOTOĞRAF")
        tabLayoutName.add(2,"VİDEO")
        val viewPager : ViewPager = findViewById(R.id.shareViewPager)
        sharePagerAdaptor = SharePagerAdaptor(supportFragmentManager,tabLayoutName)
        sharePagerAdaptor.addFragment(ShareGalleryFragment())
        sharePagerAdaptor.addFragment(ShareCameraFragment())
        sharePagerAdaptor.addFragment(ShareVideoFragment())
        viewPager.adapter = sharePagerAdaptor
        shareTabLayout.setupWithViewPager(viewPager)
        viewPager.currentItem = 1

    }

    private fun setupAuthListener() {
        //Kullanıcının oturum açıp açmadığı ile ilgili verileri tutan bir listener
        authListener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null){
                    val intent = Intent(this@ShareActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()

                }
                else{

                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        if (authListener != null){
            auth.removeAuthStateListener(authListener)
        }
    }

    override fun onBackPressed() {
        shareActivityRootConstraintLayout.visibility = View.VISIBLE
        shareActivityContainerFrameLayout.visibility = View.GONE
        super.onBackPressed()
    }
}