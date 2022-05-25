package com.donews.main.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.donews.main.R
import me.majiajie.pagerbottomtabstrip.internal.RoundMessageView
import me.majiajie.pagerbottomtabstrip.internal.Utils
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem

/**
 * 自定义TabItem
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/16 13:54
 */
class MainBottomTanItem : BaseTabItem {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )


    private val mMessage: RoundMessageView
    private val mLabel: TextView
    private val mIcon: ImageView

    private var mDefaultDrawable: Drawable? = null
    private var mDefaultColor: Int = 0
    private var mCheckColor: Int = 0

    private var mSrcDrawable: Drawable? = null

    /** tab 是否被选中 */
    private var mChecked: Boolean = false

    /** 是否隐藏标题 */
    private var mHiddenTitle: Boolean = false

    /** 是否测量过 */
    private var mMeasured: Boolean = false

    /** 是否icon进行tint */
    private val mTintIcon = true

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.main_item_bottom_tab, this, true)
        mMessage = view.findViewById(R.id.messages)
        mLabel = view.findViewById(R.id.label)
        mIcon = view.findViewById(R.id.icon)
    }

    fun initialization(
        title: String,
        @DrawableRes drawableRes: Int,
        defaultColor: Int,
        checkColor: Int,
        @DrawableRes srcRes: Int,
    ) {
        mDefaultColor = defaultColor
        mCheckColor = checkColor

        val drawable = ContextCompat.getDrawable(context, drawableRes)
        mDefaultDrawable = Utils.tinting(drawable, defaultColor)
        val drawableSelect = ContextCompat.getDrawable(context, drawableRes)
        mSrcDrawable = Utils.tinting(drawableSelect, srcRes)
        mLabel.text = title
        mLabel.setTextColor(defaultColor)
        mIcon.setImageDrawable(mDefaultDrawable)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mMeasured = true
    }

    override fun setChecked(checked: Boolean) {
        if (mChecked == checked) {
            return
        }
        mChecked = checked
        if (mHiddenTitle) {
            mLabel.visibility = if (mChecked) VISIBLE else INVISIBLE
        }
        if (mChecked) {
            mLabel.setTextColor(mCheckColor)
        } else {
            mLabel.setTextColor(mDefaultColor)
        }

        if (mChecked) {
            mIcon.setImageDrawable(mSrcDrawable)
        } else {
            mIcon.setImageDrawable(mDefaultDrawable)
        }
    }

    override fun setMessageNumber(number: Int) {
        mMessage.visibility = VISIBLE
        mMessage.messageNumber = number
    }

    override fun setHasMessage(hasMessage: Boolean) {
        mMessage.visibility = VISIBLE
        mMessage.setHasMessage(hasMessage)
    }

    override fun setTitle(title: String?) {
        mLabel.text = title
    }

    override fun setDefaultDrawable(drawable: Drawable?) {
        mDefaultDrawable = if (mTintIcon) {
            Utils.tinting(drawable, mDefaultColor)
        } else {
            drawable
        }
        if (!mChecked) {
            mIcon.setImageDrawable(mDefaultDrawable)
        }
    }

    override fun setSelectedDrawable(drawable: Drawable?) {

    }

    override fun getTitle(): String {
        return mLabel.text.toString()
    }
}