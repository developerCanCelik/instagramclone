package com.cancelik.insatagramclone.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    private val ACTIVITY_NO = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        return view
    }
    override fun onResume() {
        super.onResume()
        setupNavigationView()
    }
    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(view!!.bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this.activity!!,view!!.bottomNavigationView)
        var menu = view!!.bottomNavigationView.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}