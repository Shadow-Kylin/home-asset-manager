package cn.shadowkylin.ham.common;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @创建人 li cong
 * @创建时间 2023/3/28
 * @描述 容联云短信工具类
 */
public class RLYSmsUtil {
    private static final String ACCOUNT_SID = "2c9488768658b82f01868d06480b0dd6";
    private static final String AUTH_TOKEN = "44a05cc131e74d3fad251058159bacda";
    private static final String BASE_URL = "https://app.cloopen.com:8883";
    private static final String TEMPLATE_ID = "1";
    private static final String APP_ID = "2c9488768658b82f01868d0649460ddd";

    /**
     * 发送短信验证码
     *
     * @param phone
     * @param code
     */
    public static String sendSMS(String phone, String code) {
        String timestamp = getTimestamp();
        String signature = getSignature(timestamp);
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL).append("/2013-12-26/Accounts/").append(ACCOUNT_SID);
        sb.append("/SMS/TemplateSMS?sig=").append(signature);
        try {
            URL url = new URL(sb.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", getAuthorization(timestamp));
            connection.setRequestProperty("Content-Length", "256");
            String body = "{" +
                    "\"to\":\"" + phone + "\"," +
                    "\"appId\":\"" + APP_ID + "\"," +
                    "\"templateId\":\"" + TEMPLATE_ID + "\"," +
                    "\"datas\":[\"" + code + "\",\"1\"]}";
            connection.getOutputStream().write(body.getBytes("utf-8"));
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getAuthorization(String timestamp) {
        String src = ACCOUNT_SID + ":" + timestamp;
        String auth = new String(Base64.encodeBase64(src.getBytes()));
        return auth;
    }

    private static String getTimestamp() {
        return new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
    }

    private static String getSignature(String timestamp) {
        String sig = ACCOUNT_SID + AUTH_TOKEN + timestamp;
        String signature = null;
        try {
            signature = md5(sig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //sig参数需要大写，否则会返回401错误
        return signature.toUpperCase();
    }

    private static String md5(String string) throws Exception {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(string.getBytes("utf-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
}
