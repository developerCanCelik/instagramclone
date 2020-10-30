package com.cancelik.insatagramclone.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.BottomNavigationViewHelper
import com.cancelik.insatagramclone.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupNavigationView()
        setupToolbar()
        setupProfilePhoto()
    }

    private fun setupProfilePhoto() {
        //https://www.apple.com/newsroom/images/product/os/ios/standard/Apple_iphone11_ios14-springboard-widgets_09162020_inline.jpg.large.jpg
        val ilk ="https://"
        val imgUrl ="www.apple.com/newsroom/images/product/os/ios/standard/Apple_iphone11_ios14-springboard-widgets_09162020_inline.jpg.large.jpg"
        UniversalImageLoader.setImage(imgUrl,profile_image,progressBar,ilk)
    }

    fun setupToolbar(){
        profileOptionsImageView.setOnClickListener {
            val intent = Intent(this@ProfileActivity,ProfileSettingsActivity::class.java )
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        editProfile.setOnClickListener {
            profileRoot.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileConteiner,ProfileEditFragment())
            transaction.addToBackStack("editFragment")
            transaction.commit()
        }
    }
    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    override fun onBackPressed() {
        profileRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }




}