package com.cancelik.insatagramclone.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cancelik.insatagramclone.R
import com.cancelik.insatagramclone.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*

class ProfileEditFragment : Fragment() {
    lateinit var circleImageViewFragment: CircleImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //elemanlara erişeceğimden dolayı return kısmına view yolladık daha sonra eleman erişme durumunu yapacağız
        val view = inflater.inflate(R.layout.fragment_profile_edit,container,false)
        circleImageViewFragment = view.findViewById(R.id.profile_image)

        setupProfilePicture()
        view.image_close.setOnClickListener {
            activity?.onBackPressed()

        }
        return view
    }

    private fun setupProfilePicture() {
        //https://resimdiyari.com/upload/2014/06/18/20140618121439-338f255e.jpg
        val ilk="https://"
        val imgUrl= "resimdiyari.com/upload/2014/06/18/20140618121439-338f255e.jpg"
        UniversalImageLoader.setImage(imgUrl,circleImageViewFragment,null,ilk)
    }

}