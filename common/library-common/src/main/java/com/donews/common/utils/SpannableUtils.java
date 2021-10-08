package com.donews.common.utils;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * @author by SnowDragon
 * Date on 2021/1/8
 * Description:
 */
public class SpannableUtils {

    public static void setPositionParamColor(TextView textView, String txt, int color) {
        if (textView == null || TextUtils.isEmpty(txt)) {
            return;
        }
        String content = textView.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }

        int index = content.indexOf(txt);
        if (index == -1) {
            return;
        }
        SpannableString span = new SpannableString(content);
        span.setSpan(new ForegroundColorSpan(color),
                index, index + txt.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(span);
    }
}
