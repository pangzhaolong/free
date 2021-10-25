package com.module.lottery.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.router.RouterFragmentPath;
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp;
import com.donews.main.utils.ExitInterceptUtils;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.exception.ApiException;
import com.donews.network.request.GetRequest;
import com.module.lottery.utils.ClickDoubleUtil;
import com.module.lottery.utils.ImageUtils;
import com.module_lottery.R;
import com.module_lottery.databinding.NoDrawDialogLayoutBinding;

//没有抽奖码时的dialog
public class  NoDrawDialog extends BaseDialog implements View.OnClickListener{
    private Context mContext;
    private ExitDialogRecommendGoodsResp mExitDialogRecommendData;
    int limitNumber=1;
    public NoDrawDialog( Context context) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
    }


    NoDrawDialogLayoutBinding mNoDrawDialogLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoDrawDialogLayout= DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.no_draw_dialog_layout,null,false);
        setContentView(mNoDrawDialogLayout.getRoot());
        requestGoodsInfo();
        mNoDrawDialogLayout.replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGoodsInfo();
            }
        });

        mNoDrawDialogLayout.jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExitDialogRecommendData!=null&& ClickDoubleUtil.isFastClick()){

                    ClickDoubleUtil.isFastClick();

                    dismiss();
                    ARouter.getInstance()
                            .build(RouterFragmentPath.Lottery.PAGER_LOTTERY).withString("goods_id",mExitDialogRecommendData.getList().get(0).getGoodsId()).withString("action","newAction")
                            .navigation();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
    }



    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if(mOnFinishListener!=null){
            mOnFinishListener.onFinish();
        }
        return super.onKeyDown(keyCode, event);
    }

    OnFinishListener mOnFinishListener;



    private void requestGoodsInfo() {
        EasyHttp.get(ExitInterceptUtils.INSTANCE.getRecommendGoodsUrl()).cacheMode(CacheMode.NO_CACHE)
                .params("limit", limitNumber+"").execute(new com.donews.network.callback.SimpleCallBack<ExitDialogRecommendGoodsResp>(){
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(ExitDialogRecommendGoodsResp exitDialogRecommendGoodsResp) {
           if(mNoDrawDialogLayout!=null&&exitDialogRecommendGoodsResp!=null&&exitDialogRecommendGoodsResp.getList().size()>0){
               mExitDialogRecommendData=exitDialogRecommendGoodsResp;
               ImageUtils.setImage(mContext,mNoDrawDialogLayout.commodity,exitDialogRecommendGoodsResp.getList().get(0).getMainPic(),5);
             }
            }
        });
    }




    public void setFinishListener(OnFinishListener l) {
        mOnFinishListener=l;
    }
    public interface OnFinishListener {
        /**
         * 此时可以关闭Activity了
         *
         */
        void onFinish( );
    }
}