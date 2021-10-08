package com.donews.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.donews.common.R;


/**
 * @Author: honeylife
 * @CreateDate: 2020/4/20 12:45
 * @Description: 加载的页面，无数据
 */
public class LoadingView extends LinearLayout {


    private ImageView mEmptyImageView;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    protected int getLayoutId() {
        return R.layout.common_loading_view;
    }

    protected void initView(final Context context) {
        LayoutInflater.from(context).inflate(getLayoutId(), this);
        mEmptyImageView = ((ImageView) findViewById(R.id.empty_image));

        Glide.with(context).load(R.drawable.icon_login_gif).listener(new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    //加载一次
                    ((GifDrawable) resource).setLoopCount(Integer.MAX_VALUE);
                }
                return false;
            }


        }).into(mEmptyImageView);
    }
}
