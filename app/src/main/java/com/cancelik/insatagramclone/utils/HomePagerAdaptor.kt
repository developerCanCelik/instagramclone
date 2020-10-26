package com.cancelik.insatagramclone.utils


import android.content.Context
import androidx.constraintlayout.widget.Placeholder
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter



class HomePagerAdaptor(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val myFragmentList : ArrayList<Fragment> = ArrayList()
    override fun getCount(): Int {
        return myFragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return myFragmentList[position]
    }
    fun addFragment(fragment: Fragment){
        myFragmentList.add(fragment)
    }


}






