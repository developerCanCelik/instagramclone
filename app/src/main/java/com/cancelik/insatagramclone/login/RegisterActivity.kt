package com.cancelik.insatagramclone.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.model.Users
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {
    lateinit var manager : FragmentManager
    lateinit var ref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //phoneTextView mailTextView
        ref = FirebaseDatabase.getInstance().reference
        //şu an ben databasenin en dışındayım
        manager = supportFragmentManager
        manager.addOnBackStackChangedListener(this)
        init()
    }

    private fun init() {
        mailTextView.setOnClickListener {
            phoneView.visibility = View.GONE
            mailView.visibility = View.VISIBLE
            //registerEditText
            registerEditText.setText("")
            registerEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            registerEditText.setHint("E-Posta")
            mailTextView.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.siyah))
            phoneTextView.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.gri))
            ileri.isEnabled= false
            ileri.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.gri))
            ileri.setBackgroundColor(ContextCompat.getColor(this@RegisterActivity,R.color.beyaz))

        }
        phoneTextView.setOnClickListener {
            mailView.visibility = View.GONE
            phoneView.visibility = View.VISIBLE
            registerEditText.setText("")
            registerEditText.inputType = InputType.TYPE_CLASS_NUMBER
            registerEditText.setHint("Telefon")
            mailTextView.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.gri))
            phoneTextView.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.siyah))
            ileri.isEnabled= false
            ileri.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.gri))
            ileri.setBackgroundColor(ContextCompat.getColor(this@RegisterActivity,R.color.beyaz))
        }
        //ileri buton
        registerEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Text Değiştirilmeden önce
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Text değiştirildiğinde
                //start+before+count yerine daha kısa kod yazdık
                if (s!!.length >= 10){
                    ileri.isEnabled= true
                    ileri.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.beyaz))
                    ileri.setBackgroundColor(ContextCompat.getColor(this@RegisterActivity,R.color.mavi))
                }
                else{
                    ileri.isEnabled= false
                    ileri.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.gri))
                    ileri.setBackgroundColor(ContextCompat.getColor(this@RegisterActivity,R.color.beyaz))
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //Text Değiştirildikten sonra
            }

        })
        ileri.setOnClickListener {
            //Telefon, E-Posta
            //Düzeltilen Yerler
            if (registerEditText.hint.toString().equals("Telefon")) {
                Toast.makeText(this, "Telefon Giriş Yöntemi Seçildi", Toast.LENGTH_LONG).show()
                if (isValidPhone(registerEditText.text.toString())) {
                    var telefonKullanimdaMi = false
                    ref.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                        }
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.getValue()!= null) {
                                for (user in snapshot.children) {
                                    var okunanKullanicilar = user.getValue(Users::class.java)
                                    if (okunanKullanicilar!!.phone_number!!.equals(registerEditText.text.toString())) {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "Telefon Daha Önceden Kullanılmış",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        telefonKullanimdaMi = true
                                        break
                                    }
                                }
                                if (telefonKullanimdaMi == false){
                                    registerActivityRoot.visibility = View.GONE
                                    registerActivityContainer.visibility = View.VISIBLE
                                    var transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.registerActivityContainer, PhoneCodeFragment())
                                    transaction.addToBackStack("PhoneCode")
                                    transaction.commit()
                                    //Event Bus
                                    //DÜZELTME
                                    EventBus.getDefault().postSticky(EventbusDataEvents.KayıtBilgilerimiGönder(registerEditText.text.toString(), null, null, null, false))

                                }
                            }
                        }
                    })
                }
                else{
                    Toast.makeText(this, "Doğru bir telefon numarası kullanınız", Toast.LENGTH_LONG).show()
                }
            }
            else {
                Toast.makeText(this, "E-posta Giriş Yöntemi Seçildi", Toast.LENGTH_LONG).show()
                if (isValidEmail(registerEditText.text.toString())) {
                    //addListenerForSingleValueEvent kavramı o anlık kotrol yapıyor
                    var emailKullanimdaMi = false
                    ref.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                        }
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.getValue()!= null){
                                for (user in snapshot.children){
                                    var okunanKullanicilar = user.getValue(Users :: class.java)
                                    if (okunanKullanicilar!!.email!!.equals(registerEditText.text.toString())){
                                        Toast.makeText(this@RegisterActivity,"Email Daha Önceden Kullanılmış",Toast.LENGTH_LONG).show()
                                        emailKullanimdaMi = true
                                        break
                                    }
                                }
                                if (emailKullanimdaMi == false){
                                    registerActivityRoot.visibility = View.GONE
                                    registerActivityContainer.visibility = View.VISIBLE
                                    var transaction = supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.registerActivityContainer,RegisterFormFragment())
                                    transaction.addToBackStack("EmailCode")
                                    transaction.commit()
                                    //DÜZELTME
                                    EventBus.getDefault().postSticky(EventbusDataEvents.KayıtBilgilerimiGönder(null,registerEditText.text.toString(),null,null,true))
                                }
                            }
                        }



                    })
                      }
                else{
                    Toast.makeText(this, "Doğru bir e-mail giriniz", Toast.LENGTH_LONG).show()

                }
            }
        }
    }
/*
    override fun onBackPressed() {
        registerActivityRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }

 */
    //Geri tuşuna basma kısmı
    override fun onBackStackChanged() {
        val elemanSayisi= manager.backStackEntryCount
        if (elemanSayisi == 0) {
            registerActivityRoot.visibility = View.VISIBLE
        }
    }
    fun isValidEmail(kontrolEdilecekMail: String):Boolean{
        if (kontrolEdilecekMail == null){
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(kontrolEdilecekMail).matches()
    }
    fun isValidPhone(kontrolEdilecekTelefon: String):Boolean{
        if (kontrolEdilecekTelefon == null || kontrolEdilecekTelefon.length>16){
            return false
        }
        return android.util.Patterns.PHONE.matcher(kontrolEdilecekTelefon).matches()
    }
}