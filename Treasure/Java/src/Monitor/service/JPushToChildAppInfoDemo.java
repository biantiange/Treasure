package Monitor.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
public class JPushToChildAppInfoDemo {  
     protected static final Logger LOG = LoggerFactory.getLogger(JPushToChildAppInfoDemo.class);  
  
     // demo App defined in resources/jpush-api.conf   
  
    public static final String TITLE = "申通快递";  
   // public static final String ALERT = "true";  
    public static final String MSG_CONTENT = "申通快递祝新老客户新春快乐";  
    public static final String REGISTRATION_ID = "0900e8d85ef";  
    public static final String TAG = "tag_api";  
      
    public  static JPushClient jpushClient=null;  
      
    public static void testSendPush(String appKey ,String masterSecret,String alias1,String content,String key,String value) {  
          
		 jpushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
		 PushPayload payload=PushPayload.newBuilder()
				 .setPlatform(Platform.all())//设置接受的平台  
				 .setAudience(Audience.newBuilder()
						 .addAudienceTarget(AudienceTarget.alias(alias1))  
						 .build()) 
				 .setNotification(Notification.newBuilder()
		                 .setAlert(content)
		                 .addPlatformNotification(AndroidNotification.newBuilder().addExtra(key,value).build())
		                 .build())
				 .build();  
        try {  
            System.out.println(payload.toString());  
            PushResult result = jpushClient.sendPush(payload);  
            System.out.println(result+"................................");  
              
            LOG.info("Got result - " + result); 
          
              
        } catch (APIConnectionException e) {  
            LOG.error("Connection error. Should retry later. ", e);  
              
        } catch (APIRequestException e) {  
            LOG.error("Error response from JPush server. Should review and fix it. ", e);  
            LOG.info("HTTP Status: " + e.getStatus());  
            LOG.info("Error Code: " + e.getErrorCode());  
            LOG.info("Error Message: " + e.getErrorMessage());  
            LOG.info("Msg ID: " + e.getMsgId());  
        }  
    }  
}  



