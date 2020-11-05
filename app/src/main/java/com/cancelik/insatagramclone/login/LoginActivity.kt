package com.cancelik.insatagramclone.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.home.HomeActivity
import com.cancelik.insatagramclone.model.Users
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var ref: DatabaseReference
    lateinit var authListener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        registerTextView.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()

        ref = FirebaseDatabase.getInstance().reference
        init()
        setupAuthListener()
    }


    private fun init() {
        emailPhoneUserNameEditText.addTextChangedListener(watcher)
        sifre.addTextChangedListener(watcher)
        girisButton.setOnClickListener {
            controlReport(emailPhoneUserNameEditText.text.toString(), sifre.text.toString())
        }
    }

    private fun controlReport(emailPhoneUser: String, password: String) {
        ref.child("users").orderByChild("email")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.getValue() != null) {
                        for (mailPasswordUser in snapshot.children) {
                            var userParsed = mailPasswordUser.getValue(Users::class.java)
                            if (userParsed!!.email!!.equals(emailPhoneUserNameEditText.text.toString())) {
                                signIn(userParsed, password, false)
                                break

                            } else if (userParsed!!.user_name!!.equals(emailPhoneUserNameEditText.text.toString())) {
                                signIn(userParsed, password, false)
                                break

                            } else if (userParsed!!.phone_number!!.equals(emailPhoneUserNameEditText.text.toString())) {
                                signIn(userParsed, password, true)
                                break

                            }
                        }
                    }
                }


            })
    }

    private fun signIn(userParsed: Users, password: String, phoneSignIn: Boolean) {
        var signInEmail = ""
        if (phoneSignIn == true) {
            signInEmail = userParsed.email_phone_number.toString()
        } else {
            signInEmail = userParsed.email.toString()
        }
        auth.signInWithEmailAndPassword(signInEmail, password).addOnCompleteListener(object :
            OnCompleteListener<AuthResult> {
            override fun onComplete(p0: Task<AuthResult>) {
                if (p0!!.isSuccessful) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Oturum açildi" + userParsed.user_name,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Şifre veya Kullanıcı adını kontrol ediniz",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }


    private var watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //button activleştirmek için yapıyoruz
            if (s!!.length > 5) {
                if (emailPhoneUserNameEditText.text.toString().length >= 6 && sifre.text.toString().length >= 6) {
                    girisButton.isEnabled = true
                    girisButton.setTextColor(
                        ContextCompat.getColor(
                            this@LoginActivity,
                            R.color.beyaz
                        )
                    )
                    girisButton.setBackgroundColor(
                        ContextCompat.getColor(
                            this@LoginActivity,
                            R.color.mavi
                        )
                    )


                } else {
                    girisButton.isEnabled = false
                    girisButton.setTextColor(
                        ContextCompat.getColor(
                            this@LoginActivity,
                            R.color.siyah
                        )
                    )
                    girisButton.setBackgroundColor(
                        ContextCompat.getColor(
                            this@LoginActivity,
                            R.color.beyaz
                        )
                    )
                }
            } else {
                girisButton.isEnabled = false
                girisButton.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.siyah))
                girisButton.setBackgroundColor(
                    ContextCompat.getColor(
                        this@LoginActivity,
                        R.color.beyaz
                    )
                )
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }


    private fun setupAuthListener() {
        //Kullanıcının oturum açıp açmadığı ile ilgili verileri tutan bir listener
        authListener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user != null){
                    val intent = Intent(this@LoginActivity,HomeActivity::class.java)
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