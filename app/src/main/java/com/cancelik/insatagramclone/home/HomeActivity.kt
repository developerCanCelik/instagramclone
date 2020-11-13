package com.cancelik.insatagramclone.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.login.LoginActivity
import com.cancelik.insatagramclone.utils.BottomNavigationViewHelper
import com.cancelik.insatagramclone.utils.HomePagerAdaptor
import com.cancelik.insatagramclone.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var homePagerAdaptor: HomePagerAdaptor
    private val ACTIVITY_NO = 0
    private var homeList : ArrayList<Fragment> = ArrayList()
    lateinit var auth: FirebaseAuth
    lateinit var authListener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = FirebaseAuth.getInstance()
        setupAuthListener()
        initImageLoader()
        homePagerAdaptor()

    }

    override fun onResume() {
        super.onResume()
        setupNavigationView()
    }
    private fun initImageLoader() {
        val universalImageLoader = UniversalImageLoader(this@HomeActivity)
        ImageLoader.getInstance().init(universalImageLoader.config)
    }
    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
    fun homePagerAdaptor(){
        val viewPager : ViewPager = findViewById(R.id.viewPagerHome)
        homePagerAdaptor = HomePagerAdaptor(this,supportFragmentManager)
        homePagerAdaptor.addFragment(CameraFragment())
        homePagerAdaptor.addFragment(HomeFragment())
        homePagerAdaptor.addFragment(MessagesFragment())
        viewPager.adapter = homePagerAdaptor
        viewPager.setCurrentItem(1)

    }
    private fun setupAuthListener() {
        //Kullanıcının oturum açıp açmadığı ile ilgili verileri tutan bir listener
        authListener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null){
                    val intent = Intent(this@HomeActivity,LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
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