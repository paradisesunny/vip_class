package com.kingyee.vipclass.common;

import org.apache.xmlbeans.impl.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author 张烨
 * @version：2018-11-16 16:41
 */
public class MedliveUtil {
    private static final String AES_KEY = "kEV7TXRS6k8z1uEr";
    //    public static final String CALL_BACK_URL = "http://ahaesc.sciemr.com//login";//"http://localhost/login";/
    private static final String EMT_USER_URL = "http://www.medlive.cn/pgtoken/userinfo.php?rtn_url=";
    private static final String EMT_LOGIN_URL = "http://www.medlive.cn/auth/login?service=" + EMT_USER_URL;
    private static final String EMT_LOGIN_URLI_INIT = "http://www.medlive.cn/auth/login?service=";
    public static final String GET_USER_INFO = "http://api.medlive.cn/user/get_user_info.php?hashid=%s&checkid=%s";

    public static final String decryptAES(String input) {
        String key = AES_KEY;
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decode(input.getBytes()));
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }

        return new String(output);
    }

    public static String getMedLoginUrlInit(String callBackUrl) {
        return EMT_LOGIN_URLI_INIT + callBackUrl;
    }

    public static String getMedLoginUrlSuccess(String callBackUrl) {
        return EMT_USER_URL + callBackUrl;
    }

    /**
     * 登录url
     *
     * @param callBackUrl
     * @return
     */
    @Deprecated
    public static String getMedLoginUrl(String callBackUrl) {
        return EMT_LOGIN_URL + callBackUrl;
    }

}
