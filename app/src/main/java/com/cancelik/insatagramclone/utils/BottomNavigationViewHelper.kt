package com.cancelik.insatagramclone.utils

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.core.content.ContextCompat.startActivity
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.home.HomeActivity
import com.cancelik.insatagramclone.like.LikeActivity
import com.cancelik.insatagramclone.profile.ProfileActivity
import com.cancelik.insatagramclone.search.SearchActivity
import com.cancelik.insatagramclone.share.ShareActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx

class BottomNavigationViewHelper {
    companion object{
        fun setupBottomNavigationView(bottomNavigationViewEx: BottomNavigationViewEx){
            bottomNavigationViewEx.enableAnimation(false)
            bottomNavigationViewEx.enableItemShiftingMode(false)
            bottomNavigationViewEx.enableShiftingMode(false)
            bottomNavigationViewEx.setTextVisibility(false)
        }
        //Öğelerin tıklanmasını yaptık
        fun setupNavigation(context: Context,bottomNavigationViewEx: BottomNavigationViewEx){
            bottomNavigationViewEx.onNavigationItemSelectedListener=object : BottomNavigationView.OnNavigationItemSelectedListener{
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when(item.itemId){
                        R.id.icon_home ->{
                            val intent = Intent(context, HomeActivity::class.java)
                            context.startActivity(intent)
                            return true

                        }
                        R.id.icon_search ->{
                            val intent = Intent(context, SearchActivity::class.java)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.icon_share ->{
                            val intent = Intent(context, ShareActivity::class.java)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.icon_like ->{
                            val intent = Intent(context, LikeActivity::class.java)
                            context.startActivity(intent)
                            return true
                        }
                        R.id.icon_profile ->{
                            val intent = Intent(context, ProfileActivity::class.java)
                            context.startActivity(intent)
                            return true
                        }
                    }
                    return false
                }


            }
        }
    }
}