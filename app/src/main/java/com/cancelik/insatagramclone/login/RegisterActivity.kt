package com.cancelik.insatagramclone.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //phoneTextView mailTextView
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
                if (start+before+count >= 10){
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
            if (registerEditText.hint.toString().equals("Telefon")){
                Toast.makeText(this,"Telefon Giriş Yöntemi Seçildi",Toast.LENGTH_LONG).show()
                registerActivityRoot.visibility = View.GONE
                registerActivityContainer.visibility = View.VISIBLE
                var transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.registerActivityContainer,PhoneCodeFragment())
                transaction.addToBackStack("PhoneCode")
                transaction.commit()
                //Event Bus
                EventBus.getDefault().postSticky(EventbusDataEvents.TelefonNoGonder(registerEditText.text.toString()))
            }
            else{
                Toast.makeText(this,"E-posta Giriş Yöntemi Seçildi",Toast.LENGTH_LONG).show()
                registerActivityRoot.visibility = View.GONE
                registerActivityContainer.visibility = View.VISIBLE
                var transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.registerActivityContainer,EmailCodeFragment())
                transaction.addToBackStack("EmailCode")
                transaction.commit()
                EventBus.getDefault().postSticky(EventbusDataEvents.EmailGonder(registerEditText.text.toString()))
            }
        }
    }

    override fun onBackPressed() {
        registerActivityRoot.visibility = View.VISIBLE
        super.onBackPressed()
    }
}