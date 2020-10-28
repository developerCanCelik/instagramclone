package com.cancelik.insatagramclone.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupNavigationView()
        setupToolbar()
    }
    fun setupToolbar(){
        profileOptionsImageView.setOnClickListener {
            val intent = Intent(this@ProfileActivity,ProfileSettingsActivity::class.java )
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }



}