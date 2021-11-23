package com.donews.main;

import static com.donews.utilslibrary.utils.KeySharePreferences.MAIN_MASK_FLG;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.main.entitys.resps.HistoryPeopleLottery;
import com.donews.main.entitys.resps.RewardHistoryBean;
import com.donews.main.model.MainModel;
import com.donews.utilslibrary.utils.SPUtils;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/11/22
 * Description:
 */
public class MainViewModel extends BaseLiveDataViewModel<MainModel> {

    /**
     * 个人的参与记录。从中可以获取到最新自己参与的期数(参与记录)
     */
    private MutableLiveData<List<HistoryPeopleLottery.Period>> peopleLotteryLivData = new MutableLiveData<>();
    /**
     * 最新一期已开奖的期数(往期开奖)
     */
    private MutableLiveData<List<RewardHistoryBean.RewardBean>> peopFastOpenLivData = new MutableLiveData<>();

    /**
     * 是否需要显示浮标的通知数据标记
     */
    public MutableLiveData<Boolean> openWindFastNewPeriod = new MutableLiveData<>(null);

    /**
     * 获取当前最新开奖期数，如果为null表示未获取到最新开奖期数
     *
     * @return
     */
    public RewardHistoryBean.RewardBean getFastOpenWindPeriod() {
        if (peopFastOpenLivData.getValue() == null || peopFastOpenLivData.getValue().size() <= 0) {
            return null;
        }
        return peopFastOpenLivData.getValue().get(0);
    }

    /**
     * 更新本地的开奖期数
     */
    public void updateLocalOpenWindPeriod() {
        if (peopFastOpenLivData.getValue() != null && peopFastOpenLivData.getValue().size() > 0) {
            SPUtils.setInformain(MAIN_MASK_FLG, peopFastOpenLivData.getValue().get(0).getPeriod());
        }
        openWindFastNewPeriod.postValue(false);
    }

    private boolean isLoadDataLoading = false;

    /**
     * 检查浮标数据
     *
     * @param activity
     */
    public void checkMaskData(FragmentActivity activity) {
        if (isLoadDataLoading) {
            return;
        }
        isLoadDataLoading = true;
        //获取最新的开奖期数(往期开奖)
        peopFastOpenLivData.observe(activity, period -> {
            if (period != null && period.size() > 0) {
                mModel.requestPeopleLottery(peopleLotteryLivData);
            } else {
                isLoadDataLoading = false;
            }
        });
        //获取参与记录的数据监听(参与记录)
        peopleLotteryLivData.observe(activity, rewardBeans -> {
            isLoadDataLoading = false;
            if (rewardBeans != null && rewardBeans.size() > 0) {
                //本地记录的。已经提示过的最新一期
                int recordPeriod = SPUtils.getInformain(MAIN_MASK_FLG, 0);
                for (int i = 0; i < rewardBeans.size(); i++) {
                    if (peopFastOpenLivData.getValue().get(0).getPeriod() == rewardBeans.get(i).period &&
                            peopFastOpenLivData.getValue().get(0).getPeriod() > recordPeriod) {
                        //保存当前已经提示的期数。下次提示只会大于当前期数才会提示
                        //如果放开只提示一次。目前是当天内只提示一次
//                        SPUtils.setInformain(MAIN_MASK_FLG, peopFastOpenLivData.getValue().get(0).getPeriod());
                        openWindFastNewPeriod.postValue(true);
                        return;
                    }
                }
                openWindFastNewPeriod.postValue(false);
            } else {
                openWindFastNewPeriod.postValue(false);
            }
        });
        requestHomeMask();
    }

    /**
     * 开始请求mask数据情况
     */
    public void requestHomeMask() {
        mModel.requestRewarHistory(peopFastOpenLivData);
    }

    @Override
    public MainModel createModel() {
        return new MainModel();
    }
}
