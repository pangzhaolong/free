package com.donews.mine.viewModel;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.mine.bean.resps.HistoryPeopleLottery;
import com.donews.mine.bean.resps.RecommendGoodsResp;
import com.donews.mine.model.MineModel;

import java.util.List;

public class MineParticipateRecordViewModel extends BaseLiveDataViewModel<MineModel> {
    private ViewDataBinding viewDataBinding;

    //个人参与记录的数据
    public MutableLiveData<List<HistoryPeopleLottery.Period>> peopleHistoryLiveData = new MutableLiveData();
    //精品推荐
    public MutableLiveData<List<RecommendGoodsResp.ListDTO>> recommendGoodsLiveData= new MutableLiveData<>();

    public void setDataBinDing(ViewDataBinding dataBinding) {
        this.viewDataBinding = dataBinding;
    }

    @Override
    public MineModel createModel() {
        return new MineModel();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    /**
     * 获取个人参与记录的数据
     */
    public void loadHistoryPeopleLottery(){
        mModel.requestPeopleLottery(peopleHistoryLiveData);
    }

    /**
     * 加载精选推荐
     * @param limit 需要加载多少条数据
     */
    public void loadRecommendGoods(int limit){
        mModel.requestRecommendGoodsList(recommendGoodsLiveData,limit);
    }

}
