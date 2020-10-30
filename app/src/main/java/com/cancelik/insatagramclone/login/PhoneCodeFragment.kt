package com.cancelik.insatagramclone.login

import android.content.Context
import android.os.Bundle
import android.util.EventLog
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.EventbusDataEvents
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.math.log

class PhoneCodeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_phone_code,container,false)
        return view
    }
    @Subscribe (sticky = true)
    internal fun onPhoneNoEvent(phoneNo : EventbusDataEvents.TelefonNoGonder) {
        var gelenTelNo = phoneNo.telNo
        Log.e("can", "onPhoneNoEvent:"+gelenTelNo )

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