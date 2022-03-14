package com.donews.common.contract;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/9 15:19<br>
 * 版本：V1.0<br>
 */
public class PublicConfigBean extends BaseCustomViewModel {

    /**
     * gameGold : {"drawed":{"display":true,"type":0},"drawing":{"display":true,"type":0},"gold":true,"page":true,"video":true}
     * home : {"drawed":{"display":true,"type":0},"drawing":{"display":true,"type":0},"gold":true,"page":true,"video":true}
     * homeOne : {"drawed":{"display":true,"type":0},"drawing":{"display":true,"type":0},"gold":true,"page":true,"video":true}
     * homeTwo : {"drawed":{"display":true,"type":0},"drawing":{"display":true,"type":0},"gold":true,"page":true,"video":true}
     * luckGold : {"drawed":{"display":true,"type":0},"drawing":{"display":true,"type":0},"gold":true,"page":true,"video":true}
     * taskDraw : {"drawed":{"display":true,"type":"0'"},"drawing":{"display":true,"type":0},"gold":false,"page":false,"video":true}
     * welfare : {"drawed":{"display":true,"type":0},"drawing":{"display":true,"type":0},"gold":true,"page":false,"video":true}
     */
    /**
     * luckGold: 幸运金币
     * home: 首页充电领取奖励
     * homeOne: 首页模拟充电第一阶段
     * homeTwo: 首页模拟充电第二阶段
     * gameGold: 充电游戏
     * taskDraw: 任务领取
     * battery: 收取电池
     */

    private AdPopupBean gameGold;
    private AdPopupBean home;
    private AdPopupBean homeOne;
    private AdPopupBean homeTwo;
    private AdPopupBean luckGold;
    private AdPopupBean taskDraw;
    private AdPopupBean welfare;
    private AdPopupBean guessWord;
    private AdPopupBean guessIdiom;


    public AdPopupBean getGameGold() {
        return gameGold;
    }

    public void setGameGold(AdPopupBean gameGold) {
        this.gameGold = gameGold;
    }

    public AdPopupBean getHome() {
        return home;
    }

    public void setHome(AdPopupBean home) {
        this.home = home;
    }

    public AdPopupBean getHomeOne() {
        return homeOne;
    }

    public void setHomeOne(AdPopupBean homeOne) {
        this.homeOne = homeOne;
    }

    public AdPopupBean getHomeTwo() {
        return homeTwo;
    }

    public void setHomeTwo(AdPopupBean homeTwo) {
        this.homeTwo = homeTwo;
    }

    public AdPopupBean getLuckGold() {
        return luckGold;
    }

    public void setLuckGold(AdPopupBean luckGold) {
        this.luckGold = luckGold;
    }

    public AdPopupBean getTaskDraw() {
        return taskDraw;
    }

    public void setTaskDraw(AdPopupBean taskDraw) {
        this.taskDraw = taskDraw;
    }

    public AdPopupBean getWelfare() {
        return welfare;
    }

    public void setWelfare(AdPopupBean welfare) {
        this.welfare = welfare;
    }

    public AdPopupBean getGuessWord() {
        return guessWord;
    }

    public void setGuessWord(AdPopupBean guessWord) {
        this.guessWord = guessWord;
    }

    public AdPopupBean getGuessIdiom() {
        return guessIdiom;
    }

    public void setGuessIdiom(AdPopupBean guessIdiom) {
        this.guessIdiom = guessIdiom;
    }
}
