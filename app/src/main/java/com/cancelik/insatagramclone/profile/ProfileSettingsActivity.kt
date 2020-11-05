package com.cancelik.insatagramclone.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_profile_settings.*


class ProfileSettingsActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)
        setupNavigationView()
        backToolbar()
        fragmentNavigations()
    }

    private fun fragmentNavigations() {
        profili_duzenle.setOnClickListener {
            profileSettingsRoot.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsConteiner,ProfileEditFragment())
            transaction.addToBackStack("EditProfileFragmentEklendi")
            transaction.commit()
        }
        cikis_yap.setOnClickListener {
            var dialog = SignOutFragment()
            dialog.show(supportFragmentManager,"cikisYapDialog")
        }
    }

    private fun backToolbar() {
        backImage.setOnClickListener {
            val intent = Intent(this@ProfileSettingsActivity,ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        profileSettingsRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }

    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}