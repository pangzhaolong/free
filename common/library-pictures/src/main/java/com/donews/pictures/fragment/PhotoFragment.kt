package com.donews.pictures.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.donews.pictures.R
import com.github.chrisbanes.photoview.PhotoView

/**
 * 图片显示fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/1 19:50
 */
class PhotoFragment : Fragment() {

    companion object {
        const val PARAMS_URL = "url"

        fun newInstance(url: String): PhotoFragment {
            val args = Bundle()
            args.putString(PARAMS_URL, url)
            val fragment = PhotoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mPhotoView: PhotoView
    private var url: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        url = arguments?.getString(PARAMS_URL, "").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.picture_frag_image_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPhotoView = view.findViewById(R.id.photoView)
        mPhotoView.scaleType = ImageView.ScaleType.CENTER
        Glide.with(view)
            .load(url)
            .fitCenter()
            .into(mPhotoView)
    }

}