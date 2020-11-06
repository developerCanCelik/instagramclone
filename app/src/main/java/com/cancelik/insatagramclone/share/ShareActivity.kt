package com.cancelik.insatagramclone.share

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.login.LoginActivity
import com.cancelik.insatagramclone.utils.SharePagerAdaptor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {
    lateinit var sharePagerAdaptor : SharePagerAdaptor
    lateinit var auth: FirebaseAuth
    lateinit var authListener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        auth = FirebaseAuth.getInstance()
        sharePagerAdaptor()
        setupAuthListener()

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
}