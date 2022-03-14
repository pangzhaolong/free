package com.donews.pictures.ui

import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.donews.common.base.MvvmBaseLiveDataActivity
import com.donews.common.router.RouterActivityPath
import com.donews.pictures.R
import com.donews.pictures.adapter.BigImagePageAdapter
import com.donews.pictures.databinding.PictureBigImageBinding
import com.donews.pictures.viewmodel.BigImageViewModel
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle

/**
 * 大图查看activity
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/1 19:28
 */
@Route(path = RouterActivityPath.Pictures.BIG_IMAGE_ACTIVITY)
class BigImageActivity : MvvmBaseLiveDataActivity<PictureBigImageBinding, BigImageViewModel>() {

    @Autowired
    @JvmField
    var imageList: ArrayList<String>? = null

    @Autowired
    @JvmField
    var position = 0

    override fun getLayoutId(): Int {
        ImmersionBar.with(this)
            .navigationBarColor(R.color.black)
            .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
            .autoDarkModeEnable(true)
            .init()
        return R.layout.picture_big_image
    }


    override fun initView() {
        ARouter.getInstance().inject(this)
        val adapter = BigImagePageAdapter(supportFragmentManager, imageList)
        mDataBinding.vpContainer.adapter = adapter
        mDataBinding.vpContainer.setCurrentItem(position, false)

        val size = imageList?.size ?: 0

        mDataBinding.indicatorView.apply {
            setSliderColor(
                ContextCompat.getColor(context, R.color.common_AEAEAE),
                ContextCompat.getColor(context, R.color.white)
            )
            setSliderWidth(resources.getDimension(R.dimen.dp_10))
            setSliderHeight(resources.getDimension(R.dimen.dp_10))
            setSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            setPageSize(size)
            notifyDataChanged()
        }

        mDataBinding.vpContainer.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                mDataBinding.indicatorView.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                mDataBinding.indicatorView.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        mDataBinding.ivBack.setOnClickListener {
            finish()
        }
    }
}