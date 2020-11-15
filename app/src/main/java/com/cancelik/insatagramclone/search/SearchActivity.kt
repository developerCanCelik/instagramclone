package com.cancelik.insatagramclone.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.login.LoginActivity
import com.cancelik.insatagramclone.utils.BottomNavigationViewHelper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 1
    lateinit var auth: FirebaseAuth
    lateinit var authListener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        auth = FirebaseAuth.getInstance()
        setupAuthListener()
        setupNavigationView()
    }
    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationSearchView)
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationSearchView)
        var menu = bottomNavigationSearchView.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
        //Hepsine bottom navigation Menu ekle

    }

    private fun setupAuthListener() {
        //Kullanıcının oturum açıp açmadığı ile ilgili verileri tutan bir listener
        authListener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null){
                    val intent = Intent(this@SearchActivity, LoginActivity::class.java)
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