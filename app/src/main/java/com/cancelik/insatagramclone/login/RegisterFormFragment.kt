package com.cancelik.insatagramclone.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.model.UserDetails
import com.cancelik.insatagramclone.model.Users
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_register_form.*
import kotlinx.android.synthetic.main.fragment_register_form.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class RegisterFormFragment : Fragment() {

    var telNo = ""
    var verificationID = ""
    var gelenKod = ""
    var gelenEmail = ""
    var emailRegistrationProcess = true
    lateinit var progressBar : ProgressBar
    lateinit var auth : FirebaseAuth
    //Veritabanı işlemlerimizi referance üzerinden gerçekleştiriyoruz
    lateinit var ref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_form, container, false)
        view.loginTextView.setOnClickListener {
            val intent = Intent(activity,LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        progressBar = view.registerFormFragmentProgressBar
        auth = FirebaseAuth.getInstance()
        //şimdilik kullanıcıyı sistemden çıkarma

        ref = FirebaseDatabase.getInstance().reference

        view.nameSurnameEditText.addTextChangedListener(watcher)
        view.userNameEditText.addTextChangedListener(watcher)
        view.passwordEditText.addTextChangedListener(watcher)

        view.buttonLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            var userNameKullanildiMi = false
            ref.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.getValue() != null) {
                            for (user in snapshot.children) {
                                var userRegister = user.getValue(Users::class.java)
                                if (userRegister!!.user_name!!.equals(view.userNameEditText.text.toString())) {
                                    Toast.makeText(activity, "Kullanıcı adı daha önceden alınmış", Toast.LENGTH_SHORT).show()
                                    userNameKullanildiMi = true
                                    progressBar.visibility = View.INVISIBLE
                                    break
                                }

                            }
                            if (userNameKullanildiMi == false){
                                //email ile kayıt işlemi
                                if (emailRegistrationProcess == true){
                                    var password = view.passwordEditText.text.toString()
                                    var nameSurname = view.nameSurnameEditText.text.toString()
                                    var userName = view.userNameEditText.text.toString()
                                    auth.createUserWithEmailAndPassword(gelenEmail,password).addOnCompleteListener(
                                        object : OnCompleteListener<AuthResult> {
                                            override fun onComplete(p0: Task<AuthResult>) {
                                                if (p0.isSuccessful) {
                                                    var userID = auth.currentUser!!.uid.toString()
                                                    Toast.makeText(
                                                        activity,
                                                        "Oturum Başarılı Bir Şekilde Açıldı",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    //oturum açılmış kullanıcının verilerini kaydetme
                                                    var userDetails = UserDetails("0","0","0","","","")
                                                    var kaydedilecekKullanıcı = Users(gelenEmail,password,nameSurname,userName,"","",userID,userDetails)
                                                    ref.child("users").child(userID).setValue(kaydedilecekKullanıcı)
                                                        .addOnCompleteListener(object : OnCompleteListener<Void> {
                                                            override fun onComplete(p0: Task<Void>) {
                                                                if (p0.isSuccessful) {
                                                                    progressBar.visibility = View.INVISIBLE
                                                                    Toast.makeText(
                                                                        activity,
                                                                        "Kullanıcı Başarılı Bir Şekilde Kaydedildi",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()

                                                                } else {
                                                                    auth.currentUser!!.delete()
                                                                        .addOnCompleteListener(object :
                                                                            OnCompleteListener<Void> {
                                                                            override fun onComplete(p0: Task<Void>) {
                                                                                if (p0.isSuccessful) {
                                                                                    Toast.makeText(activity,"Kullanıcı Veri Tabanına Kaydedilirken" +
                                                                                            "Bir Sorun Yaşandı Tekrar Kayıt Olunuz", Toast.LENGTH_SHORT).show()
                                                                                }
                                                                            }
                                                                        })

                                                                }
                                                            }

                                                        })

                                                } else {
                                                    Toast.makeText(activity, "Oturum Açılamadı", Toast.LENGTH_LONG)
                                                        .show()
                                                }
                                            }

                                        })

                                }
                                else{
                                    var password = view.passwordEditText.text.toString()
                                    var fakePhone = telNo +"@telefongiris.com"
                                    var nameSurname = view.nameSurnameEditText.text.toString()
                                    var userName = view.userNameEditText.text.toString()
                                    var phoneNumber = telNo
                                    auth.createUserWithEmailAndPassword(fakePhone,password).addOnCompleteListener(object :
                                        OnCompleteListener<AuthResult> {
                                        override fun onComplete(p0: Task<AuthResult>) {
                                            if (p0.isSuccessful) {
                                                Toast.makeText(
                                                    activity,
                                                    "Oturum Başarılı Bir Şekilde Telefondan Açıldı",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                var userID = auth.currentUser!!.uid.toString()
                                                //oturum açan kullanıcının detayları
                                                var userDetails = UserDetails("0","0","0","","","")
                                                var kaydedilecekKullanıcı = Users("",password,nameSurname,userName,phoneNumber,fakePhone,userID,userDetails)
                                                ref.child("users").child(userID).setValue(kaydedilecekKullanıcı)
                                                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                                                        override fun onComplete(p0: Task<Void>) {
                                                            if (p0.isSuccessful) {
                                                                progressBar.visibility = View.INVISIBLE
                                                                Toast.makeText(
                                                                    activity,
                                                                    "Telefondan Kullanıcı Başarılı Bir Şekilde Kaydedildi",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } else {
                                                                auth.currentUser!!.delete()
                                                                    .addOnCompleteListener(object :
                                                                        OnCompleteListener<Void> {
                                                                        override fun onComplete(p0: Task<Void>) {
                                                                            if (p0.isSuccessful) {
                                                                                Toast.makeText(activity,"Kullanıcı Veri Tabanına Kaydedilirken" +
                                                                                        "Bir Sorun Yaşandı Tekrar Kayıt Olunuz", Toast.LENGTH_SHORT).show()
                                                                            }
                                                                        }
                                                                    })

                                                            }
                                                        }

                                                    })

                                            } else {
                                                Toast.makeText(activity, "Oturum Açılamadı", Toast.LENGTH_LONG).show()
                                            }
                                        }

                                    })
                                }
                            }
                        }

                    }


            })

        }
        return  view
    }

    var watcher : TextWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length > 5){
                if(nameSurnameEditText.text.toString().length>5 && userNameEditText.text.toString().length >5 && passwordEditText.text.toString().length>5){
                    buttonLogin.isEnabled = true
                    buttonLogin.setTextColor(ContextCompat.getColor(activity!!,R.color.beyaz))
                    buttonLogin.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.mavi))
                }else{
                    buttonLogin.isEnabled = false
                    buttonLogin.setTextColor(ContextCompat.getColor(activity!!,R.color.siyah))
                    buttonLogin.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.beyaz))
                }


            }else{
                buttonLogin.isEnabled = false
                buttonLogin.setTextColor(ContextCompat.getColor(activity!!,R.color.siyah))
                buttonLogin.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.beyaz))
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    @Subscribe(sticky = true)
    internal fun onKayitEvent(kayitBilgileri : EventbusDataEvents.KayıtBilgilerimiGönder){
        if (kayitBilgileri.emailRegister == true){
            emailRegistrationProcess = true
             gelenEmail = kayitBilgileri.email!!
            Toast.makeText(activity,"Email:${gelenEmail}",Toast.LENGTH_LONG).show()
        }
        else{
            emailRegistrationProcess = false
            telNo = kayitBilgileri.telNo!!
            verificationID = kayitBilgileri.verificationID!!
            gelenKod = kayitBilgileri.code!!
            Toast.makeText(activity,"Telefon No:${telNo},verification id:${verificationID},code:${gelenKod}",Toast.LENGTH_LONG).show()
            println("Telefon No:${telNo},verification id:${verificationID},code:${gelenKod}")

        }
  

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

}