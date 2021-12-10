package com.donews.common.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Message
import android.os.SystemClock
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import com.donews.common.R
import java.lang.ref.WeakReference
import java.util.*

class CountdownView : LinearLayout {

    private var period: Long = 0;

    private var frequency: Long = 20;

    private var complete: Boolean = false

    private var minute: TextView? = null;
    private var second: TextView? = null;
    private var millisecond: TextView? = null;


    private var colonOne: TextView? = null;
    private var colonTwo: TextView? = null;


    private var mHandler = CountdownViewHandler(this);
    private var mICountdownViewListener: ICountdownViewListener? = null

    private var elapsedRealtime: Long = 0L;
    private var defaultColor = Color.parseColor("#FFFFFF");

    constructor(context: Context?) : this(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}

    @SuppressLint("ResourceAsColor")
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {


        var view = LinearLayout.inflate(context, R.layout.common_countdown_layout, this);
        minute = view.findViewById(R.id.minute);
        second = view.findViewById(R.id.second);
        colonOne = view.findViewById(R.id.colon_one);
        colonTwo = view.findViewById(R.id.colon_two);
        millisecond = view.findViewById(R.id.millisecond);
        var typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CountdownView)
        if (typedArray != null) {
            var mAutomatically =
                typedArray.getBoolean(R.styleable.CountdownView_automatically, false);
            var timestamp = typedArray.getInt(R.styleable.CountdownView_time, 0);
            //设置背景
            var minuteBg = typedArray.getDrawable(R.styleable.CountdownView_text_view_minute_bg)
            minute?.background = minuteBg

            var secondBg = typedArray.getDrawable(R.styleable.CountdownView_text_view_second_bg)
            second?.background = secondBg
            var millisecondBg =
                typedArray.getDrawable(R.styleable.CountdownView_text_view_millisecond_bg)
            millisecond?.background = millisecondBg
            var txSize =
                typedArray.getDimension(R.styleable.CountdownView_c_text_size,0F)
            if(txSize != 0F){
                minute?.setTextSize(TypedValue.COMPLEX_UNIT_PX,txSize)
                second?.setTextSize(TypedValue.COMPLEX_UNIT_PX,txSize)
                millisecond?.setTextSize(TypedValue.COMPLEX_UNIT_PX,txSize)
            }
            //设置背景
            var minuteColor =
                typedArray.getColor(R.styleable.CountdownView_text_view_minute_color, defaultColor)

            minute?.setTextColor(minuteColor)
            var secondColor =
                typedArray.getColor(R.styleable.CountdownView_text_view_second_color, defaultColor)
            second?.setTextColor(secondColor)
            var millisecondColor = typedArray.getColor(
                R.styleable.CountdownView_text_view_millisecond_color,
                defaultColor
            )
            millisecond?.setTextColor(millisecondColor)
            //冒号颜色
            var colonColor =
                typedArray.getColor(R.styleable.CountdownView_colon_color, defaultColor)
            colonOne?.setTextColor(colonColor)
            colonTwo?.setTextColor(colonColor)

            //需要倒计时的毫秒
            period = timestamp * 60L * 1000L
            if (mAutomatically && timestamp > 0) {
                elapsedRealtime = SystemClock.elapsedRealtime();
                startTime()
            }

        }
        typedArray?.recycle()
    }


    private fun startTime() {
        if (period > 0) {
            var message = Message();
            message.what = 1
            mHandler.sendMessage(message)
        }
    }


    fun setCountdownViewListener(l: ICountdownViewListener) {
        mICountdownViewListener = l;
    }

    interface ICountdownViewListener {
        //倒计时完成
        fun onCountdownCompleted()

    }


    public class CountdownViewHandler internal constructor(context: CountdownView?) :
        android.os.Handler() {

        private var reference //
                : WeakReference<CountdownView?>? = null

        init {
            reference = WeakReference(context)
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> if (reference?.get() != null) {
                    //当前系统时间-去之前的时间 =经过的时间
                    var time =
                        SystemClock.elapsedRealtime() - reference?.get()?.elapsedRealtime!!;
                    //总倒计时时间- 经过的时间=剩余的时间
                    var timestamp = reference?.get()?.period!! - time;
                    if (timestamp > 0) {
                        reference?.get()?.complete = false
                        //修改时间
                        reference?.get()?.getTimeFormat(timestamp)
                        var message = Message();
                        message.what = 1
                        sendMessageDelayed(message, reference?.get()?.frequency!!)

                    } else {
                        reference?.get()?.complete = true
                        //倒计时结束
                        if (reference?.get()?.mICountdownViewListener != null) {
                            reference?.get()?.mICountdownViewListener!!.onCountdownCompleted()
                        }
                    }
                }
            }
        }


    }


    public fun pauseTimer() {
        if (mHandler != null) {
            mHandler.removeMessages(0)
            mHandler.removeMessages(1)
            mHandler.removeCallbacksAndMessages(null)
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pauseTimer();
    }

    public fun isComplete(): Boolean {
        return complete
    }

    @SuppressLint("SetTextI18n")
    private fun getTimeFormat(timestamp: Long) {
        //分钟
        var f = timestamp / 1000 / 60
        if (f < 10) {
            minute?.text = "0$f"
        } else {
            minute?.text = "$f"
        }
        //秒
        var m = timestamp / 1000 - (f * 60)
        if (m < 10) {
            second?.text = "0$m"
        } else {
            second?.text = "$m"
        }
        //毫秒
        var s = timestamp - ((f * 60 * 1000) + (m * 1000))
        var stateValue = s.toString()
        if (stateValue.length > 2) {
            stateValue = stateValue.substring(0, 2)
        }
        millisecond?.text = stateValue
    }
}