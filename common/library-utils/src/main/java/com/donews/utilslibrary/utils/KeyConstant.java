package com.donews.utilslibrary.utils;

import com.donews.utilslibrary.BuildConfig;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/22 18:51<br>
 * 版本：V1.0<br>
 */
public final class KeyConstant {
    //大数据统计
    private static String ANALYSIS_DATA = BuildConfig.ANALYSIS_DATA;
    //umeng数据统计
    private static String ANALYSIS_U_MENG =BuildConfig.ANALYSIS_U_MENG;

    // bugly
    private static String BUGLY_ID =BuildConfig.BUGLY_ID;

    /**
     * 数美
     */
    private static final String ORIGANIZATION = "Xcbqk8Qp83yoOIr3XPbu";
    private static final String SHU_MEI_PUBLIC_KEY = "MIIDLzCCAhegAwIBAgIBMDANBgkqhkiG9w0BAQUFADAyMQswCQYDVQQGEwJDTjELMAkGA1UECwwCU00xFjAUBgNVBAMMDWUuaXNodW1laS5jb20wHhcNMjAwOTI3MDM1MTE5WhcNNDAwOTIyMDM1MTE5WjAyMQswCQYDVQQGEwJDTjELMAkGA1UECwwCU00xFjAUBgNVBAMMDWUuaXNodW1laS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCDz2rRUNMek9mMvjiJvDV0b7OCN5B26IDN/sYrpSb5ryCqCm5U8gam1n+Gq2iw0rLNuRYLl755TfIm0s5RXywbhOMUaAxlUwmGyVsSWIzz56IIUJpbyEEd6D3U1s5td0kOUpjPfBgsO/tnrCTKUGH7bOnPIZoFey3XYZxZTllHfENZA+7UoleXX+9AjY7EGtgiwq3rpBiZHhayA7ZvhDoVb9iuIB8kHIYUm8YU++oteHXAPcTasZ+74K8I2DHpq/7slTHENHpwnK35fPLX6HoDVSXoYhYd9Km1mccnnzgcCP9UskIdxLc/UUznz1zJVHdHZml48vicZbfjEr3+Qt2PAgMBAAGjUDBOMB0GA1UdDgQWBBQfO1yiDFQJ4rWfurO+Dc9Gano92jAfBgNVHSMEGDAWgBQfO1yiDFQJ4rWfurO+Dc9Gano92jAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUAA4IBAQBYy0yRjH3ewZ/ASi/raP/VAplJvBlo+cIrkQmL3iX29WrdJ9rbgPGCOG6OnbmbrY+xtVnAaughLGlQ6knwKzHJ2DXF1B1RrUG7u+peE77wcZIdh8caWbIlhB5R46S7msMdDSvKfDSlOGwjev25ibQiuskV3BoAUNsoRk5TiMSHs96+xcBDsmbKG5lVuvT+dRSa9gLcsoHGPrUqEMZ8dKNm4S/nn5NT1UzJFoakKLNuNFUbEeOrnWNq2gznWvDVUnBz/pHRRazH/5HodHp/dWfYB1KI3+PSprFOxg4u/txZcsx1exD33zyjW79sACCuFgfQOw0i10mnPypvTivtJ3D0";
    private static final String SHU_MEI_APP_ID = "jiankangzhuan";
    private static final String SHU_MEI_KEY_INFO = "smsdkandroidXcbqk8Qp83yoOIr3XPbuflag";


    public static String getANALYSIS_DATA() {
        return ANALYSIS_DATA;
    }

    public static String getANALYSIS_U_MENG() {
        return ANALYSIS_U_MENG;
    }

    public static String getBuglyId() {
        return BUGLY_ID;
    }


    public static String getOrganization() {
        return ORIGANIZATION;
    }

    public static String getShuMeiKey() {
        return SHU_MEI_PUBLIC_KEY;
    }

    public static String getShuMeiKeyInfo() {
        return SHU_MEI_KEY_INFO;
    }

    public static String getShuMeiAppId() {
        return SHU_MEI_APP_ID;
    }
}
