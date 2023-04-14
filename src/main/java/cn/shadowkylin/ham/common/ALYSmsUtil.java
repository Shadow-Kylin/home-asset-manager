package cn.shadowkylin.ham.common;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @创建人 li cong
 * @创建时间 2023/3/28
 * @描述 阿里云短信工具类
 */
@Component
public class ALYSmsUtil {
    //accessKeyId和accessKeySecret
    //获取环境变量
    private static final String ACCESS_KEY_ID = System.getenv("ACCESS_KEY_ID");
    private static final String ACCESS_KEY_SECRET = System.getenv("ACCESS_KEY_SECRET");

    private static final String SIGN_NAME = "家庭资产管家";
    private static final String TEMPLATE_CODE = "SMS_275015560";

    public static String sendSms(String phoneNumber, String code) {
        try {
            //设置超时时间-可自行调整
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //初始化ascClient需要的几个参数
            final String product = "Dysmsapi";
            final String domain = "dysmsapi.aliyuncs.com";
            //初始化ascClient,暂时不支持多region（请勿修改）
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //获取发送短信的request
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(phoneNumber);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(SIGN_NAME);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(TEMPLATE_CODE);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时。
            request.setTemplateParam("{\"code\":\"" + code + "\"}");
            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            System.out.println(sendSmsResponse.getCode());
            if (sendSmsResponse.getCode() == null && !sendSmsResponse.getCode().equals("OK")) {
                //请求失败
                return sendSmsResponse.getMessage();
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return "false";
        } catch (ClientException e) {
            e.printStackTrace();
            return "false";
        }
        return "OK";
    }
}
