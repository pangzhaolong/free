package com.donews.main.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
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
    private val mLottie: LottieAnimationView


    private var mDefaultDrawable: Drawable? = null
    private var mDefaultColor: Int = 0
    private var mCheckColor: Int = 0

    /** tab 是否被选中 */
    private var mChecked: Boolean = false

    /** 是否隐藏标题 */
    private var mHiddenTitle: Boolean = false

    /** 是否测量过 */
    private var mMeasured: Boolean = false

    /** 是否icon进行tint */
    private val mTintIcon = true


    private var mAnimator: ValueAnimator? = null
    private var mAnimatorValue = 1f

    private var mTranslation = 0f
    private var mTranslationHideTitle = 0f

    private var mTopMargin = 0
    private var mTopMarginHideTitle = 0

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.main_item_bottom_tab, this, true)
        mMessage = view.findViewById(R.id.messages)
        mLabel = view.findViewById(R.id.label)
        mIcon = view.findViewById(R.id.icon)
        mLottie = view.findViewById(R.id.lottie)

        val scale = context.resources.displayMetrics.density
        mTranslation = scale * 2
        mTranslationHideTitle = scale * 10
        mTopMargin = (scale * 8).toInt()
        mTopMarginHideTitle = (scale * 16).toInt()
    }

    fun initialization(
        title: String,
        @DrawableRes drawableRes: Int,
        defaultColor: Int,
        checkColor: Int,
        lottieJson: String
    ) {

        mDefaultColor = defaultColor
        mCheckColor = checkColor

        val drawable = ContextCompat.getDrawable(context, drawableRes)
        mDefaultDrawable = Utils.tinting(drawable, defaultColor)

        mLottie.setAnimation(lottieJson)
        mLottie.progress = 1f

//        val filter = SimpleColorFilter(checkColor)
//        mLottie.addValueCallback(KeyPath("**"), LottieProperty.COLOR_FILTER, LottieValueCallback<ColorFilter>(filter))

        mLabel.text = title
        mLabel.setTextColor(defaultColor)
        mIcon.setImageDrawable(mDefaultDrawable)

        mAnimator = ValueAnimator.ofFloat(1f).apply {
            duration = 115L
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                mAnimatorValue = it.animatedValue as Float
                if (mHiddenTitle) {
                    mIcon.translationY = -mTranslationHideTitle * mAnimatorValue
                    mLottie.translationY = -mTranslationHideTitle * mAnimatorValue
                } else {
                    mIcon.translationY = -mTranslation * mAnimatorValue
                    mLottie.translationY = -mTranslation * mAnimatorValue
                }
                mLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f + mAnimatorValue * 2f)
            }
        }
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
            mIcon.visibility = INVISIBLE
            mLottie.visibility = VISIBLE
            mLabel.setTextColor(mCheckColor)
        } else {
            mIcon.visibility = VISIBLE
            mLottie.visibility = INVISIBLE
            mLabel.setTextColor(mDefaultColor)
        }

        if (mMeasured) {
            // 切换动画
            if (mChecked) {
                mLottie.progress = 0f
                mLottie.playAnimation()
                mAnimator!!.start()
            } else {
                mLottie.cancelAnimation()
                mLottie.progress = 1f
                mAnimator!!.reverse()
            }
        } else if (mChecked) { // 布局还未测量时选中，直接转换到选中的最终状态
            if (mHiddenTitle) {
                mIcon.translationY = -mTranslationHideTitle
                mLottie.translationY = -mTranslationHideTitle
            } else {
                mIcon.translationY = -mTranslation
                mLottie.translationY = -mTranslation
            }
            mLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        } else { // 布局还未测量并且未选中，保持未选中状态
            mIcon.translationY = 0f
            mLottie.translationY = 0f
            mLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
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