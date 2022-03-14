package com.donews.common.contract;

/**
 * @Author: honeylife
 * @CreateDate: 2020/9/25 10:40
 * @Description:
 */
public class SignInHelp {

    private int signVideoTotal;
    private int signVideoNumb;

    public static SignInHelp getInstance() {
        return SignInHelp.Holder.instance;
    }

    public int getSignVideoTotal() {
        return signVideoTotal;
    }

    public void setSignVideoTotal(int signVideoTotal) {
        this.signVideoTotal = signVideoTotal;
    }

    public int getSignVideoNumb() {
        return signVideoNumb;
    }

    public void setSignVideoNumb(int signVideoNumb) {
        this.signVideoNumb = Math.max(signVideoNumb, 0);
    }

    private static final class Holder {
        private static SignInHelp instance = new SignInHelp();
    }
}
