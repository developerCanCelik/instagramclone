package com.cancelik.insatagramclone.login

import android.content.Context
import android.os.Bundle
import android.util.EventLog
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_phone_code.*
import kotlinx.android.synthetic.main.fragment_phone_code.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit
import kotlin.math.log

class PhoneCodeFragment : Fragment() {

    var gelenTelNo = ""
    lateinit var callbacks :PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var auth = FirebaseAuth.getInstance()
    var verificationID = ""
    var gelenKod = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_phone_code,container,false)
        println(gelenTelNo.toString())
        view.phoneNoTextView.text = gelenTelNo

        setupCallback()

        view.codeButton.setOnClickListener {
            if (gelenKod.equals(view.numberCode.text.toString())){
                //kayıt yapmamız için
                EventBus.getDefault().postSticky(EventbusDataEvents.KayıtBilgilerimiGönder(gelenTelNo,null,verificationID,gelenKod,false))
                Toast.makeText(activity,"Onaylandı ilerleyebilirsin",Toast.LENGTH_LONG).show()
                var transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.registerActivityContainer,RegisterFormFragment())
                transaction.addToBackStack("RegisterFragmentForm")
                transaction.commit()
            }
            else{
                Toast.makeText(activity,"Kod Hatalı",Toast.LENGTH_LONG).show()

            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(gelenTelNo)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this.activity!!)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        return view
    }


    private fun setupCallback() {
         callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //eğer daha önce onaylanmamış isem kod gelsin
                if (!credential.smsCode!!.isNotEmpty()){
                    gelenKod = credential.smsCode!!
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                println(e.message)

            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                verificationID = verificationId
            }
        }
    }
    //DUZELTME
    @Subscribe (sticky = true)
    internal fun onPhoneNoEvent(phoneNo : EventbusDataEvents.KayıtBilgilerimiGönder) {
        gelenTelNo = phoneNo.telNo!!

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