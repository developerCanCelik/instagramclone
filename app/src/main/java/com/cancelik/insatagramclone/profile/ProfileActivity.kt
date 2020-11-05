package com.cancelik.insatagramclone.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.login.LoginActivity
import com.cancelik.insatagramclone.model.Users
import com.cancelik.insatagramclone.utils.BottomNavigationViewHelper
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import com.cancelik.insatagramclone.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*
import org.greenrobot.eventbus.EventBus


class ProfileActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 4
    lateinit var auth: FirebaseAuth
    lateinit var authListener : FirebaseAuth.AuthStateListener
    lateinit var ref : DatabaseReference
    lateinit var userInformation : FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupAuthListener()
        auth = FirebaseAuth.getInstance()
        userInformation = auth.currentUser!!
        ref = FirebaseDatabase.getInstance().reference
        setupNavigationView()
        setupToolbar()
        userInformation()
        setupProfilePhoto()

    }
    //addListenerForSingleValueEvent yerine addValueEventListener
    private fun userInformation() {
        ref.child("users").child(userInformation.uid).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue() != null){
                    var userParsed = snapshot.getValue(Users::class.java)

                    EventBus.getDefault().postSticky(EventbusDataEvents.KullaniciBilgileriniGonder(userParsed))

                    userToolbarText.text = userParsed!!.user_name
                    profileUserText.text = userParsed!!.name_surname
                    postsText.text = userParsed!!.userDetails!!.post
                    followersText.text = userParsed!!.userDetails!!.follower
                    followingText.text = userParsed!!.userDetails!!.following
                    var imgUrl : String = userParsed!!.userDetails!!.profile_picture!!
                    UniversalImageLoader.setImage(imgUrl,profile_image,progressBar, "")
                    if (!userParsed!!.userDetails!!.biography.isNullOrEmpty()){
                        biographyText.visibility = View.VISIBLE
                        biographyText.text = userParsed!!.userDetails!!.biography
                    }
                    if (!userParsed!!.userDetails!!.web_site.isNullOrEmpty()){
                        internetText.visibility = View.VISIBLE
                        internetText.text = userParsed!!.userDetails!!.web_site
                    }
                }
            }
        })
    }
    private fun setupProfilePhoto() {
        //https://www.apple.com/newsroom/images/product/os/ios/standard/Apple_iphone11_ios14-springboard-widgets_09162020_inline.jpg.large.jpg
        /*
        val ilk ="https://"
        val imgUrl ="www.apple.com/newsroom/images/product/os/ios/standard/Apple_iphone11_ios14-springboard-widgets_09162020_inline.jpg.large.jpg"
        UniversalImageLoader.setImage(imgUrl,profile_image,progressBar,ilk)

         */
    }

    fun setupToolbar(){
        profileOptionsImageView.setOnClickListener {
            val intent = Intent(this@ProfileActivity,ProfileSettingsActivity::class.java )
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        editProfile.setOnClickListener {
            profileRoot.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileConteiner,ProfileEditFragment())
            transaction.addToBackStack("editFragment")
            transaction.commit()
        }
    }
    fun setupNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu = bottomNavigationView.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }

    override fun onBackPressed() {
        profileRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }
    private fun setupAuthListener() {
        //Kullanıcının oturum açıp açmadığı ile ilgili verileri tutan bir listener
        authListener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null){
                    val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()

                }
                else{
                    //Eğer oturum açmış bir kullanıcı var ise bilgilerine erişmek için

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