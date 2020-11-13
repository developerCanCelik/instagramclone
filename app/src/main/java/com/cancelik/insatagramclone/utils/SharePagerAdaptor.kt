package com.cancelik.insatagramclone.utils

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SharePagerAdaptor( fm: FragmentManager , tabLayoutName : ArrayList<String>) : FragmentPagerAdapter(fm) {
    private val myFragmentList : ArrayList<Fragment> = ArrayList()
    private val myTabLayoutName : ArrayList<String> = tabLayoutName
    override fun getCount(): Int {
        return myFragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return myFragmentList[position]
    }
    fun addFragment(fragment: Fragment){
        myFragmentList.add(fragment)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return myTabLayoutName.get(position)
    }
    fun selectFragmentViewPagerClear(viewGroup: ViewGroup,position: Int){
        var clearFragment = this.instantiateItem(viewGroup,position)
        this.destroyItem(viewGroup,position,clearFragment)
    }
    fun selectFragmentViewPagerAdd(viewGroup: ViewGroup,position: Int){
        this.instantiateItem(viewGroup,position)
    }
}