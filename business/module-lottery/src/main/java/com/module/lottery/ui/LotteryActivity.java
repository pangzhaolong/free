package com.module.lottery.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.router.RouterFragmentPath;
import com.module.lottery.adapter.GuessAdapter;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.dialog.ExhibitCodeStartsDialog;
import com.module.lottery.dialog.GenerateCodeDialog;
import com.module.lottery.dialog.LessMaxDialog;
import com.module.lottery.dialog.LotteryCodeStartsDialog;
import com.module.lottery.dialog.NoDrawDialog;
import com.module.lottery.dialog.ReceiveLotteryDialog;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.utils.GridSpaceItemDecoration;
import com.module.lottery.view.TextureVideoViewOutlineProvider;
import com.module.lottery.viewModel.LotteryViewModel;
import com.module_lottery.R;
import com.module_lottery.databinding.GuesslikeHeadLayoutBinding;
import com.module_lottery.databinding.LotteryMainLayoutBinding;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.Map;


@Route(path = RouterFragmentPath.Lottery.PAGER_LOTTERY)
public class LotteryActivity extends BaseActivity<LotteryMainLayoutBinding, LotteryViewModel> {

    //    @Autowired(name = "goodsListDTO")
//    SpikeBean.GoodsListDTO mGoodsListDTO;
    @Autowired(name = "goods_id")
    String mGoodsId;
    //    String id = "tb:655412572200";
    GuessAdapter guessAdapter;

    @Autowired(name = "action")
    String mAction;


    int mPageNumber = 1;
    boolean refresh = true;
    //抽奖码的对象用来拦截返回
    LotteryCodeBean mLotteryCodeBean;



    @Override
    protected int getLayoutId() {
        return R.layout.lottery_main_layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ARouter.getInstance().inject(this);
        mDataBinding.panicBuyingButton.post(new Runnable() {
            @Override
            public void run() {
                mDataBinding.panicBuyingButton.setHeight(mDataBinding.panicBuyingButton.getWidth());
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        mDataBinding.panicBuyingButton.setOutlineProvider(new TextureVideoViewOutlineProvider());
        mDataBinding.panicBuyingButton.setClipToOutline(true);
        mDataBinding.panicBuyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //开始抽奖
                //弹框抽奖码生成dialog
                LotteryCodeStartsDialog lotteryCodeStartsDialog = new LotteryCodeStartsDialog(LotteryActivity.this);
                lotteryCodeStartsDialog.setStateListener(new LotteryCodeStartsDialog.OnStateListener() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onJumpAd() {
                        lotteryCodeStartsDialog.dismiss();
                        //弹起生成抽奖码的dialog
                        showGenerateCodeDialog();
                    }
                });
                lotteryCodeStartsDialog.create();
                lotteryCodeStartsDialog.show();
            }
        });
        guessAdapter = new GuessAdapter(LotteryActivity.this);
        guessAdapter.getLayout(R.layout.guesslike_item_layout);
        mDataBinding.lotteryGridview.setLayoutManager(gridLayoutManager);
        mDataBinding.lotteryGridview.setAdapter(guessAdapter);
        mDataBinding.lotteryGridview.addItemDecoration(new GridSpaceItemDecoration(2));
        setHeaderView(mDataBinding.lotteryGridview);
        setObserveList();
        //加载抽奖商品详情信息
        lotteryInfo();
        //设置下拉，和上拉的监听
        setSmartRefresh();
    }

    //生成抽奖码的Dialog
    private void showGenerateCodeDialog(){
        GenerateCodeDialog generateCodeDialog=new GenerateCodeDialog(LotteryActivity.this);
        generateCodeDialog.setStateListener(new GenerateCodeDialog.OnStateListener() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onJumpAd() {
                //不跳转广告 展示生成的随机抽奖码
                generateCodeDialog.dismiss();
                showExhibitCodeDialog();
            }
        });
        generateCodeDialog.create();
        generateCodeDialog.show();

    }



    //生成抽奖码的Dialog
    private void showReceiveLotteryDialog(){
        ReceiveLotteryDialog receiveLottery=new ReceiveLotteryDialog(LotteryActivity.this,mLotteryCodeBean);
        receiveLottery.setStateListener(new ReceiveLotteryDialog.OnStateListener() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onJumpAd() {


            }
        });
        receiveLottery.create();
        receiveLottery.show();

    }





    //展示生成的抽奖码
    private void showExhibitCodeDialog(){
        ExhibitCodeStartsDialog exhibitCodeStartsDialog=new ExhibitCodeStartsDialog(LotteryActivity.this);
        exhibitCodeStartsDialog.setStateListener(new ExhibitCodeStartsDialog.OnStateListener() {
            @Override
            public void onFinish() {
                exhibitCodeStartsDialog.dismiss();
                //显示立刻领取的dialog
                showReceiveLotteryDialog();
            }

            @Override
            public void onJumpAd() {




            }
        });
        exhibitCodeStartsDialog.create();
        exhibitCodeStartsDialog.show();

    }





    private void setSmartRefresh() {
        //下拉刷新
        mDataBinding.mailSmRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageNumber = 1;
                refresh = true;
                lotteryInfo();
            }
        });
        //上拉加载
        mDataBinding.mailSmRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageNumber++;
                refresh = false;
                youMayAlsoLike(mPageNumber, false);
                Toast.makeText(getApplicationContext(), "加载更多", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        ARouter.getInstance().inject(this);
        if(mAction!=null&&mAction.equals("newAction")){
            clearState();
            //加载抽奖商品详情信息
            lotteryInfo();
        }

    }

    public void setObserveList() {
        //观察商品数据顶部
        mViewModel.getMutableLiveData().observe(this, CommodityBean -> {
            mDataBinding.mailSmRefresh.finishRefresh();
            mDataBinding.mailSmRefresh.setEnableLoadMore(true);
            // 获取数据
            if (CommodityBean == null) {
                return;
            }
            guessAdapter.setCommodityBean(CommodityBean);
            guessAdapter.notifyDataSetChanged();
            //查询 猜你喜欢的数据
            //猜你喜欢
            youMayAlsoLike(mPageNumber, true);
            requestParticipateNumber();
            //抽奖码
            requestLotteryCode(CommodityBean.getPeriod());


        });
        //观察可能喜欢的数据
        mViewModel.getmGuessLike().observe(this, LotteryBean -> {
            mDataBinding.mailSmRefresh.finishLoadMore(true);
            // 获取数据
            if (LotteryBean == null) {
                return;
            }
            //获取数据刷新列表
            if (LotteryBean.getList() != null) {
                if (refresh) {
                    guessAdapter.setGuessLikeData(LotteryBean.getList());
                    guessAdapter.notifyDataSetChanged();
                } else {
                    guessAdapter.addGuessLikeData(LotteryBean.getList());
                    guessAdapter.notifyDataSetChanged();
                }
            }
        });


        mViewModel.getmParticipateBean().observe(this, MaylikeBean -> {
            // 获取数据
            if (MaylikeBean == null) {
                return;
            }
            //获取数据刷新列表
            if (MaylikeBean.getList() != null) {
                CommodityBean commodityBean = guessAdapter.getCommodityBean();
                commodityBean.setParticipateBean(MaylikeBean);
                guessAdapter.notifyDataSetChanged();
            }
        });
        //抽奖码
        mViewModel.getmLotteryCodeBean().observe(this, MaylikeBean -> {
            // 获取数据
            if (MaylikeBean == null) {
                return;
            }
            mLotteryCodeBean = MaylikeBean;
            //获取数据刷新列表
            if (MaylikeBean.getCodes() != null) {
                CommodityBean commodityBean = guessAdapter.getCommodityBean();
                commodityBean.setLotteryCodeBean(MaylikeBean);
                guessAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void initView() {

    }


    //加载抽奖商品详情信息
    public void lotteryInfo() {
        Map<String, String> params = BaseParams.getMap();
        params.put("goods_id", mGoodsId);
        mViewModel.getNetLotteryData(LotteryModel.LOTTERY_GUESS_INFO, params);
    }


    //加载猜你喜欢的数据
    public void youMayAlsoLike(int pageNumber, boolean refresh) {
        Map<String, String> params = BaseParams.getBaseParams();
        params.put("page_id", pageNumber + "");
        params.put("goods_id", mGoodsId);
        mViewModel.getGuessLikeData(LotteryModel.LOTTERY_GUESS_LIKE, params);
    }


    //请求抽奖累计人数
    public void requestParticipateNumber() {
        Map<String, String> params = BaseParams.getMap();
        params.put("goods_id", mGoodsId);
        mViewModel.getParticipateNumberData(LotteryModel.LOTTERY_PARTICIPATE_NUM, params);
    }

    //请求抽奖码
    public void requestLotteryCode(int numberPeriods) {
        Map<String, String> params = BaseParams.getMap();
        params.put("goods_id", mGoodsId);
        params.put("period", numberPeriods + "");
        mViewModel.getLotteryCodeData(LotteryModel.LOTTERY_LOTTERY_CODE, params);
    }

    //设置列表的顶部
    private void setHeaderView(RecyclerView view) {
        GuesslikeHeadLayoutBinding guesslikeHeadLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.guesslike_head_layout, view, false);
        guessAdapter.setHeaderView(guesslikeHeadLayoutBinding);
    }


    private  void clearState(){
        mPageNumber=1;
        refresh=true;
        mLotteryCodeBean=null;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //判断抽奖码的数量显示对应的dialog
            if (mLotteryCodeBean != null&&mLotteryCodeBean.getCodes().size()==0) {
                NoDrawDialog   mNoDrawDialog = new NoDrawDialog(LotteryActivity.this);
                mNoDrawDialog.setFinishListener(new NoDrawDialog.OnFinishListener() {
                    @Override
                    public void onFinish() {
                        finish();
                    }
                });
                mNoDrawDialog.create();
                mNoDrawDialog.show();
                return false;
            }

            //当抽奖码小于6个
            if (mLotteryCodeBean != null&&mLotteryCodeBean.getCodes().size()<6) {
                LessMaxDialog     mLessMaxDialog = new LessMaxDialog(LotteryActivity.this);
                mLessMaxDialog.setFinishListener(new NoDrawDialog.OnFinishListener() {
                    @Override
                    public void onFinish() {
                        finish();
                    }
                });
                mLessMaxDialog.create();
                mLessMaxDialog.show();
                return false;
            }


        }
        return true;
    }

}