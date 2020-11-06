package com.cancelik.insatagramclone.profile

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.cancelik.insatagramclone.R
import kotlinx.android.synthetic.main.fragment_upload.view.*

class UploadFragment : DialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_upload, container, false)
        view.progressBar2.indeterminateDrawable.setColorFilter(ContextCompat.getColor(activity!!,R.color.siyah),PorterDuff.Mode.SRC_IN)
        return view
    }


}