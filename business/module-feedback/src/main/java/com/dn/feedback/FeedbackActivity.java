package com.dn.feedback;

import static com.donews.common.router.RouterActivityPath.Feedback.PAGER_ACTIVITY_FEEDBACK;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.UriUtils;
import com.dn.feedback.databinding.ActivityFeedbackBinding;
import com.dn.feedback.reqs.FeedbackReq;
import com.dn.feedback.request.PhotoAlbumResultContract;
import com.dn.feedback.resp.FeedbackUploadImgResp;
import com.donews.base.utils.ToastUtil;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.adapter.ScreenAutoAdapter;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.contract.BaseCustomViewModel;
import com.donews.network.EasyHttp;
import com.donews.network.body.ProgressResponseCallBack;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.CallBack;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DeviceUtils;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 意见反馈页面
 * <p>
 */
@Route(path = PAGER_ACTIVITY_FEEDBACK)
public class FeedbackActivity extends MvvmBaseLiveDataActivity<ActivityFeedbackBinding, BaseLiveDataViewModel> {

    private List<CheckBox> radList = new ArrayList<>();
    //选择的图片集合
    private String[] selectImgs;
    //上传网络之后的图片
    private String[] httpImgs;
    private View currentClickImageView = null;
    //结果处理集合
    ActivityResultLauncher launcher = registerForActivityResult(new PhotoAlbumResultContract(), resultList -> {
        try {
            if (resultList.isEmpty()) {
                return;
            }
            updateSelectImgUI(resultList);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
                throw e;
            }
        }
    });

    @Override
    protected int getLayoutId() {
        ScreenAutoAdapter.match(this, 375.0f);
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        return R.layout.activity_feedback;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if(!(v instanceof EditText)){
                KeyboardUtils.hideSoftInput(this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void initView() {
        selectImgs = new String[mDataBinding.feedUploadImgVp.getChildCount()];
        radList.add(mDataBinding.rad0);
        radList.add(mDataBinding.rad1);
        radList.add(mDataBinding.rad2);
        radList.add(mDataBinding.rad3);
        //设置单选
        for (CheckBox checkBox : radList) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    //设置其他选择为未选中
                    for (CheckBox cBox : radList) {
                        if (buttonView != cBox) {
                            cBox.setChecked(false);
                        }
                    }
                }
            });
        }
        //联系客服
        mDataBinding.feedCallKf.setOnClickListener(v -> {
            ToastUtil.showShort(this,"联系客服");
        });
        //设置关闭
        mDataBinding.feedSubmit.setOnClickListener(v -> {
            submitData();
        });
        //设置关闭
        mDataBinding.feedBack.setOnClickListener(v -> {
            finish();
        });
        //设置
        for (int i = 0; i < selectImgs.length; i++) {
            final int pos = i;
            View itemView = mDataBinding.feedUploadImgVp.getChildAt(i);
            View uploadVp = itemView.findViewById(R.id.item_upload_layout);
            View showVp = itemView.findViewById(R.id.item_icon_show_layout);
            View showCloseIv = itemView.findViewById(R.id.item_icon_close);
            TextView showTv = itemView.findViewById(R.id.item_icon_num);
            ImageView showImgIv = itemView.findViewById(R.id.item_icon_iv);

            itemView.setTag(i); //设置下标
            showTv.setText((i + 1) + "/" + selectImgs.length);
            //添加图片
            uploadVp.setOnClickListener(v -> {
                if (showVp.getVisibility() == View.VISIBLE) {
                    return;//图片已经显示。则不处理上传时事件
                }
                //选择相片需要上传的照片
                currentClickImageView = itemView;
                launcher.launch(true);
            });
            //显示只有的清除按钮
            showCloseIv.setOnClickListener(v -> {
                selectImgs[pos] = null;
                if (httpImgs != null &&
                        httpImgs.length == selectImgs.length) {
                    //清除指定已上传的文件
                    httpImgs[pos] = null;
                }
                selectImgs[pos] = null;
                showImgIv.setImageResource(0);
                showVp.setVisibility(View.GONE);//隐藏
                showCloseIv.setVisibility(View.GONE);//隐藏
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //只是怕万一内存泄露。理论上应该内部会自己绑定生命周期组件
        launcher.unregister();
    }

    //更新选择图片之后的UI
    private void updateSelectImgUI(List<String> selectImgs) {
        httpImgs = null;//数据已变更。缓存上传记录自动重置
        if (selectImgs.isEmpty()) {
            for (int i = 0; i < this.selectImgs.length; i++) {
                View itemView = mDataBinding.feedUploadImgVp.getChildAt(i);
                View showVp = itemView.findViewById(R.id.item_icon_show_layout);
                ImageView showImgIv = itemView.findViewById(R.id.item_icon_iv);
                ImageView closeIv = itemView.findViewById(R.id.item_icon_close);
                closeIv.setVisibility(View.GONE);
                showImgIv.setImageResource(0);
                showVp.setVisibility(View.GONE);

            }
            return;
        }
        if (selectImgs.size() == 1) {
            String path = getPath(selectImgs.get(0));
            if (path == null || path.isEmpty()) {
                ToastUtil.showShort(this, "图片已损坏或不存在");
                return;
            }
            View showVp = currentClickImageView.findViewById(R.id.item_icon_show_layout);
            ImageView showImgIv = currentClickImageView.findViewById(R.id.item_icon_iv);
            ImageView closeIv = currentClickImageView.findViewById(R.id.item_icon_close);
            closeIv.setVisibility(View.VISIBLE);
            showVp.setVisibility(View.VISIBLE);
            GlideUtils.loadImageView(this, path, showImgIv);
            //报错数据
            if (currentClickImageView.getTag() != null) {
                this.selectImgs[Integer.parseInt(currentClickImageView.getTag().toString())] = path;
            } else {
                this.selectImgs[0] = path;
            }
            return;//自由一张。更新自己
        }
        boolean isToast = false;
        for (int i = 0; i < this.selectImgs.length; i++) {
            String path = null;
            if (i < selectImgs.size()) {
                path = getPath(selectImgs.get(i));
            }else{
                //此处表示超过了选择内容。控制提示不在展示
                isToast = true;
            }
            View itemView = mDataBinding.feedUploadImgVp.getChildAt(i);
            View showVp = itemView.findViewById(R.id.item_icon_show_layout);
            ImageView showImgIv = itemView.findViewById(R.id.item_icon_iv);
            ImageView closeIv = itemView.findViewById(R.id.item_icon_close);
            if (path == null || path.isEmpty()) {
                if (!isToast) {
                    ToastUtil.showShort(this, "图片已损坏或不存在");
                    isToast = true;
                }
                showImgIv.setImageResource(0);
                showVp.setVisibility(View.GONE);
                closeIv.setVisibility(View.GONE);
                continue;
            }
            showVp.setVisibility(View.VISIBLE);
            closeIv.setVisibility(View.VISIBLE);
            GlideUtils.loadImageView(this, path, showImgIv);
            //保存数据
            this.selectImgs[i] = path;
        }
    }

    //获取路径
    public String getPath(String contentPath) {
        if (contentPath.startsWith("content")) {
            return UriUtils.uri2File(Uri.parse(contentPath)).getPath();
        } else {
            return contentPath;
        }
    }

    int flgCount = -1;

    //提交数据
    private void submitData() {
        FeedbackReq req = new FeedbackReq();
        String userId = AppInfo.getUserId();
        if (!TextUtils.isEmpty(userId)) {
            try {
                req.user_id = Long.getLong(userId);
            } catch (Exception e) {
                req.user_id = 0L;
                e.printStackTrace();
            }
        }
        for (CheckBox checkBox : radList) {
            if (checkBox.isChecked()) {
                req.types = new ArrayList<>();
                req.types.add(checkBox.getText().toString());
                break;
            }
        }
        if (req.types == null || req.types.isEmpty()) {
            ToastUtil.showShort(this, "请选择反馈类型");
            return;
        }
        if (mDataBinding.feedTel.getText() == null || mDataBinding.feedTel.getText().toString().isEmpty()) {
            ToastUtil.showShort(this, "请输入手机号");
            return;
        }
        if (mDataBinding.feedTel.getText().toString().replace(" ", "").trim().length() < 11) {
            ToastUtil.showShort(this, "请输入正确的手机号");
            return;
        }
        if(!RegexUtils.isMobileSimple(mDataBinding.feedTel.getText().toString())){
            ToastUtil.showShort(this, "手机号码格式不正确");
            return;
        }
        req.mobile = mDataBinding.feedTel.getText().toString().replace(" ", "").trim();
        if (mDataBinding.feedDesc.getText() == null ||
                mDataBinding.feedDesc.getText().toString().isEmpty()) {
            ToastUtil.showShort(this, "请输入反馈内容");
            return;
        }
        if (mDataBinding.feedDesc.getText().toString().trim().length() < 10) {
            ToastUtil.showShort(this, "反馈内容至少不能低于10个字");
            return;
        }
        req.content = mDataBinding.feedDesc.getText().toString().replace(" ", "").trim();
        //上传图片
        flgCount = -1;
        File[] files = new File[selectImgs.length];
        if (httpImgs == null) {
            httpImgs = new String[selectImgs.length];
        }
        boolean isUploadFile = false;
        for (int i = 0; i < selectImgs.length; i++) {
            if (i == 0) {
                flgCount = 0;
            }
            if (selectImgs[i] != null && !selectImgs[i].isEmpty()) {
                if (httpImgs[i] == null) {
                    isUploadFile = true;
                    files[i] = new File(selectImgs[i]);
                    flgCount++;//没有上传过。记录需要上传的统计
                }
            }
        }
        if (isUploadFile) {
            showLoading("上传图片中");
            for (int i = 0; i < files.length; i++) {
                if (files[i] == null) {
                    continue;//不需要上传的
                }
                final int cuttPos = i;
                List<File> uploadFile = new ArrayList<>();
                uploadFile.add(files[i]);
                EasyHttp.post(BuildConfig.LOGIN_URL + "upload")
                        .cacheMode(CacheMode.NO_CACHE)
                        .addFileParams("file", uploadFile, (bytesWritten, contentLength, done) -> {
                        })
                        .execute(new SimpleCallBack<FeedbackUploadImgResp>() {
                            @Override
                            public void onError(ApiException e) {
                                flgCount--;
                                if (flgCount == 0) {
                                    hideLoading();
                                }
                                ToastUtil.showShort(FeedbackActivity.this, "图片上传失败,请稍后重试");
                            }

                            @Override
                            public void onSuccess(FeedbackUploadImgResp resp) {
                                httpImgs[cuttPos] = resp.url;
                                flgCount--;
                                if (flgCount == 0) {
                                    submitFeedbackData(req, httpImgs);
                                }
                            }
                        });
            }
        } else {
            //没有图片
            submitFeedbackData(req, httpImgs);
        }
    }

    int con = 0;

    //提交反馈数据
    private void submitFeedbackData(FeedbackReq req, String[] httpImgUrls) {
        if (httpImgUrls != null && httpImgUrls.length > 0) {
            List<String> resuList = new ArrayList<>();
            for (String httpImgUrl : httpImgUrls) {
                if (httpImgUrl == null || httpImgUrl.isEmpty()) {
                    continue;
                }
                resuList.add(httpImgUrl);
            }
            req.attachments = resuList;
        }
        con++;
        if (con < 10) {
            hideLoading();
            return;
        }
        showLoading("提交中");
        EasyHttp.post(BuildConfig.LOGIN_URL + "xtasks/v2/feedback/store")
                .cacheMode(CacheMode.NO_CACHE)
                .upObject(req)
                .execute(new SimpleCallBack<Object>() {
                    @Override
                    public void onError(ApiException e) {
                        hideLoading();
                        ToastUtil.showShort(FeedbackActivity.this, "提交失败,请稍后重试");
                    }

                    @Override
                    public void onSuccess(Object b) {
                        ToastUtil.showShort(FeedbackActivity.this, "提交成功");
                        finish();
                    }

                    @Override
                    public void onCompleteOk() {
                        ToastUtil.showShort(FeedbackActivity.this, "提交成功");
                        finish();
                    }
                });
    }
}
