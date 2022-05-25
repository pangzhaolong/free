package com.donews.yfsdk.utils

import com.donews.yfsdk.manager.AdConfigManager

object IdUtil {
    fun getBannerDnId(): String {
        if (AdConfigManager.mNormalAdBean.banner.DnIdNew.isBlank()
                && AdConfigManager.mNormalAdBean.banner.DnIdOld.isBlank()
                && AdConfigManager.mNormalAdBean.banner.DnIdInvalid.isBlank()) {
            return ""
        }

        if (AdConfigManager.mRewardVideoId.layer_id == AdConfigManager.mNormalAdBean.invalidLayer) {
            if (AdConfigManager.mNormalAdBean.banner.DnIdInvalid.isBlank()) {
                if (AdConfigManager.mNormalAdBean.banner.DnIdNew.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.banner.DnIdNew
                } else if (AdConfigManager.mNormalAdBean.banner.DnIdOld.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.banner.DnIdOld
                }
            }
            return AdConfigManager.mNormalAdBean.banner.DnIdInvalid
        }

        return if (System.currentTimeMillis() - AppUtil.getAppInstallTime() > AdConfigManager.mNormalAdBean.banner.switchAfter * 60 * 1000) {
            if (AdConfigManager.mNormalAdBean.banner.DnIdOld.isBlank()) {
                AdConfigManager.mNormalAdBean.banner.DnIdNew
            } else {
                AdConfigManager.mNormalAdBean.banner.DnIdOld
            }
        } else {
            if (AdConfigManager.mNormalAdBean.banner.DnIdNew.isBlank()) {
                AdConfigManager.mNormalAdBean.banner.DnIdOld
            } else {
                AdConfigManager.mNormalAdBean.banner.DnIdNew
            }
        }
    }

    fun getSplashDnId(): String {
        if (AdConfigManager.mNormalAdBean.splash.DnIdNew.isBlank()
                && AdConfigManager.mNormalAdBean.splash.DnIdOld.isBlank()
                && AdConfigManager.mNormalAdBean.splash.DnIdInvalid.isBlank()) {
            return ""
        }

        if (AdConfigManager.mRewardVideoId.layer_id == AdConfigManager.mNormalAdBean.invalidLayer) {
            if (AdConfigManager.mNormalAdBean.splash.DnIdInvalid.isBlank()) {
                if (AdConfigManager.mNormalAdBean.splash.DnIdNew.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.splash.DnIdNew
                } else if (AdConfigManager.mNormalAdBean.splash.DnIdOld.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.splash.DnIdOld
                }
            }
            return AdConfigManager.mNormalAdBean.splash.DnIdInvalid
        }

        return if (System.currentTimeMillis() - AppUtil.getAppInstallTime() > AdConfigManager.mNormalAdBean.splash.switchAfter * 60 * 1000) {
            if (AdConfigManager.mNormalAdBean.splash.DnIdOld.isBlank()) {
                AdConfigManager.mNormalAdBean.splash.DnIdNew
            } else {
                AdConfigManager.mNormalAdBean.splash.DnIdOld
            }
        } else {
            if (AdConfigManager.mNormalAdBean.splash.DnIdNew.isBlank()) {
                AdConfigManager.mNormalAdBean.splash.DnIdOld
            } else {
                AdConfigManager.mNormalAdBean.splash.DnIdNew
            }
        }
    }

    fun getInstlFullDnId(): String {
        if (AdConfigManager.mNormalAdBean.interstitialFull.DnIdNew.isBlank()
                && AdConfigManager.mNormalAdBean.interstitialFull.DnIdOld.isBlank()
                && AdConfigManager.mNormalAdBean.interstitialFull.DnIdInvalid.isBlank()) {
            return ""
        }

        if (AdConfigManager.mRewardVideoId.layer_id == AdConfigManager.mNormalAdBean.invalidLayer) {
            if (AdConfigManager.mNormalAdBean.interstitialFull.DnIdInvalid.isBlank()) {
                if (AdConfigManager.mNormalAdBean.interstitialFull.DnIdNew.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.interstitialFull.DnIdNew
                } else if (AdConfigManager.mNormalAdBean.interstitialFull.DnIdOld.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.interstitialFull.DnIdOld
                }
            }
            return AdConfigManager.mNormalAdBean.interstitialFull.DnIdInvalid
        }

        return if (System.currentTimeMillis() - AppUtil.getAppInstallTime() > AdConfigManager.mNormalAdBean.interstitialFull.switchAfter * 60 * 1000) {
            if (AdConfigManager.mNormalAdBean.interstitialFull.DnIdOld.isBlank()) {
                AdConfigManager.mNormalAdBean.interstitialFull.DnIdNew
            } else {
                AdConfigManager.mNormalAdBean.interstitialFull.DnIdOld
            }
        } else {
            if (AdConfigManager.mNormalAdBean.interstitialFull.DnIdNew.isBlank()) {
                AdConfigManager.mNormalAdBean.interstitialFull.DnIdOld
            } else {
                AdConfigManager.mNormalAdBean.interstitialFull.DnIdNew
            }
        }
    }

    fun getFeedTemplateDnId(): String {
        if (AdConfigManager.mNormalAdBean.information.DnIdNew.isBlank()
                && AdConfigManager.mNormalAdBean.information.DnIdOld.isBlank()
                && AdConfigManager.mNormalAdBean.information.DnIdInvalid.isBlank()) {
            return ""
        }

        if (AdConfigManager.mRewardVideoId.layer_id == AdConfigManager.mNormalAdBean.invalidLayer) {
            if (AdConfigManager.mNormalAdBean.information.DnIdInvalid.isBlank()) {
                if (AdConfigManager.mNormalAdBean.information.DnIdNew.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.information.DnIdNew
                } else if (AdConfigManager.mNormalAdBean.information.DnIdOld.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.information.DnIdOld
                }
            }
            return AdConfigManager.mNormalAdBean.information.DnIdInvalid
        }

        return if (System.currentTimeMillis() - AppUtil.getAppInstallTime() > AdConfigManager.mNormalAdBean.information.switchAfter * 60 * 1000) {
            if (AdConfigManager.mNormalAdBean.information.DnIdOld.isBlank()) {
                AdConfigManager.mNormalAdBean.information.DnIdNew
            } else {
                AdConfigManager.mNormalAdBean.information.DnIdOld
            }
        } else {
            if (AdConfigManager.mNormalAdBean.information.DnIdNew.isBlank()) {
                AdConfigManager.mNormalAdBean.information.DnIdOld
            } else {
                AdConfigManager.mNormalAdBean.information.DnIdNew
            }
        }
    }


    /** draw 模板*/
    fun getDrawTemplateId(): String {
        if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew.isBlank()
                && AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld.isBlank()
                && AdConfigManager.mNormalAdBean.drawInformation.CsjIdInvalid.isBlank()) {
            return ""
        }

        if (AdConfigManager.mRewardVideoId.layer_id == AdConfigManager.mNormalAdBean.invalidLayer) {
            if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdInvalid.isBlank()) {
                if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew
                } else if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld
                }
            }
            return AdConfigManager.mNormalAdBean.drawInformation.CsjIdInvalid
        }

        return if (System.currentTimeMillis() - AppUtil.getAppInstallTime() > AdConfigManager.mNormalAdBean.drawInformation.switchAfter * 60 * 1000) {
            if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld.isBlank()) {
                AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew
            } else {
                AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld
            }
        } else {
            if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew.isBlank()) {
                AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld
            } else {
                AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew
            }
        }
    }

    /** draw 自渲染*/
    fun getDrawNativeDmId(): String {
        if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew.isBlank()
                && AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld.isBlank()
                && AdConfigManager.mNormalAdBean.drawInformation.CsjIdInvalid.isBlank()) {
            return ""
        }

        if (AdConfigManager.mRewardVideoId.layer_id == AdConfigManager.mNormalAdBean.invalidLayer) {
            if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdInvalid.isBlank()) {
                if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew
                } else if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld.isNotBlank()) {
                    return AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld
                }
            }
            return AdConfigManager.mNormalAdBean.drawInformation.CsjIdInvalid
        }

        return if (System.currentTimeMillis() - AppUtil.getAppInstallTime() > AdConfigManager.mNormalAdBean.drawInformation.switchAfter * 60 * 1000) {
            if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld.isBlank()) {
                AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew
            } else {
                AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld
            }
        } else {
            if (AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew.isBlank()) {
                AdConfigManager.mNormalAdBean.drawInformation.CsjIdOld
            } else {
                AdConfigManager.mNormalAdBean.drawInformation.CsjIdNew
            }
        }
    }
}