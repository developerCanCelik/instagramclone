package com.cancelik.insatagramclone.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class RegisterFormFragment : Fragment() {

    var telNo = ""
    var verificationID = ""
    var gelenKod = ""
    var gelenEmail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_form, container, false)
        return  view
    }
    //Düzeltme
    @Subscribe(sticky = true)
    internal fun onKayitEvent(kayitBilgileri : EventbusDataEvents.KayıtBilgilerimiGönder){
        if (kayitBilgileri.emailRegister == true){
             gelenEmail = kayitBilgileri.email!!
            Toast.makeText(activity,"Email:${gelenEmail}",Toast.LENGTH_LONG).show()
        }
        else{
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