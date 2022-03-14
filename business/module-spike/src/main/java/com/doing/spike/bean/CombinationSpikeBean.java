package com.doing.spike.bean;

import com.donews.common.contract.BaseCustomViewModel;

public class CombinationSpikeBean  extends BaseCustomViewModel {

    public SpikeBean getSpikeBean() {
        return mSpikeBean;
    }

    public void setSpikeBean(SpikeBean mSpikeBean) {
        this.mSpikeBean = mSpikeBean;
    }

    public SpikeBean.RoundsListDTO getRoundsListDTO() {
        return mRoundsListDTO;
    }

    public void setRoundsListDTO(SpikeBean.RoundsListDTO mRoundsListDTO) {
        this.mRoundsListDTO = mRoundsListDTO;
    }

    SpikeBean mSpikeBean;
    SpikeBean.RoundsListDTO mRoundsListDTO;

}
