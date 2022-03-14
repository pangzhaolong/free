package com.donews.middle.utils;

import com.airbnb.lottie.LottieAnimationView;

public class LottieUtil {
    public static void initLottieView(LottieAnimationView view) {
        if ((view != null && !view.isAnimating())) {
            view.setImageAssetsFolder("");
            view.clearAnimation();
            view.setAnimation("littleHand.json");
            view.loop(true);
            view.playAnimation();
        }
    }

    public static void cancelLottieView(LottieAnimationView view) {
        if (view != null) {
            view.removeAllAnimatorListeners();
            view.cancelAnimation();
            view.clearAnimation();
        }
    }
}
