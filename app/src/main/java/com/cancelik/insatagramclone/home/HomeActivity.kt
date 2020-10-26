package com.cancelik.insatagramclone.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.BottomNavigationViewHelper
import com.cancelik.insatagramclone.utils.HomePagerAdaptor
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var homePagerAdaptor: HomePagerAdaptor
    private val ACTIVITY_NO = 0
    private var homeList : ArrayList<Fragment> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupNavigationView()
        homePagerAdaptor()
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
}