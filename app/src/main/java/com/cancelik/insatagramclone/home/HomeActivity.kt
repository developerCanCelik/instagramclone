package com.cancelik.insatagramclone.home

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.login.LoginActivity
import com.cancelik.insatagramclone.utils.BottomNavigationViewHelper
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import com.cancelik.insatagramclone.utils.HomePagerAdaptor
import com.cancelik.insatagramclone.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.*
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

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
    /*
    override fun onResume() {
        super.onResume()
        setupNavigationView()
    }
     */
    private fun initImageLoader() {
        val universalImageLoader = UniversalImageLoader(this@HomeActivity)
        ImageLoader.getInstance().init(universalImageLoader.config)
    }
    /*
    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

     */
    fun homePagerAdaptor(){
        val viewPager : ViewPager = findViewById(R.id.viewPagerHome)
        homePagerAdaptor = HomePagerAdaptor(this,supportFragmentManager)
        homePagerAdaptor.addFragment(CameraFragment())
        homePagerAdaptor.addFragment(HomeFragment())
        homePagerAdaptor.addFragment(MessagesFragment())
        viewPager.adapter = homePagerAdaptor
        viewPager.setCurrentItem(1)
        homePagerAdaptor.selectFragmentViewPagerClear(viewPager,0)
        homePagerAdaptor.selectFragmentViewPagerClear(viewPager,2)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageSelected(position: Int) {
                if (position == 0){
                    this@HomeActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    this@HomeActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                    cameraAskForPermission()
                    homePagerAdaptor.selectFragmentViewPagerClear(viewPager,1)
                    homePagerAdaptor.selectFragmentViewPagerClear(viewPager,2)
                    homePagerAdaptor.selectFragmentViewPagerAdd(viewPager,0)
                }
                if (position == 1){
                    this@HomeActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                    this@HomeActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    homePagerAdaptor.selectFragmentViewPagerClear(viewPager,0)
                    homePagerAdaptor.selectFragmentViewPagerClear(viewPager,2)
                    homePagerAdaptor.selectFragmentViewPagerAdd(viewPager,1)
                }
                if (position == 2){
                    this@HomeActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                    this@HomeActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    homePagerAdaptor.selectFragmentViewPagerClear(viewPager,1)
                    homePagerAdaptor.selectFragmentViewPagerClear(viewPager,0)
                    homePagerAdaptor.selectFragmentViewPagerAdd(viewPager,2)
                }

            }

        })

    }
    private fun cameraAskForPermission() {
        //activityi istiyor
        Dexter.withContext(this)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        EventBus.getDefault().postSticky(EventbusDataEvents.KameraIzinBilgisiGonder(true))
                    }
                    if (p0!!.isAnyPermissionPermanentlyDenied) {
                        viewPagerHome.setCurrentItem(1)
                        var builder = AlertDialog.Builder(this@HomeActivity)
                        builder.setTitle("İzin vermeniz gerekiyor")
                        builder.setMessage("Ayarlar kısmından izin vermeniz gerekiyor")
                        builder.setPositiveButton(
                            "Onay Ver",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.cancel()
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    var uri = Uri.fromParts("package",packageName,null)
                                    intent.setData(uri)
                                    startActivity(intent)
                                    finish()

                                }
                            })
                        builder.setNegativeButton(
                            "Onay Verme",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.cancel()
                                    finish()
                                    viewPagerHome.setCurrentItem(1)
                                }
                            })
                        builder.show()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    println("Tüm izinlerden birini vermemiş ise yine açıklama yap")
                    var builder = AlertDialog.Builder(this@HomeActivity)
                    builder.setTitle("İzin vermeniz gerekiyor")
                    builder.setMessage("Uygulamaya izin vermeniz gerekmekte Onaylıyor musunuz?")
                    builder.setPositiveButton(
                        "Onay Ver",
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.cancel()
                                p1!!.continuePermissionRequest()
                                viewPagerHome.setCurrentItem(1)
                            }
                        })
                    builder.setNegativeButton(
                        "Onay Verme",
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.cancel()
                                viewPagerHome.setCurrentItem(1)
                                finish()
                            }
                        })
                    builder.show()
                }

            })
            .withErrorListener(object : PermissionRequestErrorListener {
                override fun onError(p0: DexterError?) {
                    println(p0.toString())
                }

            }).check()

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