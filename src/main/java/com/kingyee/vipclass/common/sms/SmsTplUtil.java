package com.kingyee.vipclass.common.sms;

import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 新短信通道,福建电信版 + 点击拨号(add by songqian 2018/12/06)
 *
 * @author 张宏亮
 * @version 2015年6月30日下午1:25:43
 */
public class SmsTplUtil {
    /**
     * 项目域名,短信标识
     */
    private static final String DOMAIN = "kangdini.kydev.net" ;
    /**
     *  短信发送 url
     */
    private static final String SMS_SEND_URL = "http://service.callcenter.medlive.cn/apifj/smsbytemplet";

    /**
     * 点击拨号 url
     */
    private static final String CTD_URL = "http://service.callcenter.medlive.cn/apifj/click2call";

    /**
     * 终止拨号 url
     */
    private static final String STOP_CTD_URL = "http://service.callcenter.medlive.cn/apifj/callstop";
    /**
     * 自定义签名 如果需要默认签名，就设置为""
     */
    private static final String Sign = "康蒂尼-自由呼吸频道";


    /**
     * httpclient超时时间10秒
     */
    private static int HTTPCLIENT_TIMEOUT = 10000;


    /**
     * 点击拨号
     * @param callerNbr 主叫号码
     * @param calleeNbr 被叫号码
     * @return
     */
    public static JsonNode ctd(String callerNbr, String calleeNbr) {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        HttpPost post = null;
        try {
            client = getHttpClient();
            post = new HttpPost(CTD_URL);
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("domain", DOMAIN));
            parameters.add(new BasicNameValuePair("callerNbr", callerNbr));
            parameters.add(new BasicNameValuePair("calleeNbr", calleeNbr));
            HttpEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
            post.setEntity(entity);
            response = client.execute(post);
            if (response.getEntity() != null) {
                return JacksonMapper.parse(EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (post != null) {
                    post.abort();
                }
                response.close();
                client.close();
            } catch (Exception e) {
                // nothing to do
            }
        }
        return JacksonMapper.newErrorInstance(null);
    }

    /**
     * 终止呼叫
     * @param sessionid 点击拨号回传回的sessionid
     * @return
     */
    public static JsonNode stopCtd(String sessionid) {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        HttpPost post = null;
        try {
            client = getHttpClient();
            post = new HttpPost(STOP_CTD_URL);
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("domain", DOMAIN));
            parameters.add(new BasicNameValuePair("sessionid", sessionid));
            HttpEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
            post.setEntity(entity);
            response = client.execute(post);
            if (response.getEntity() != null) {
                return JacksonMapper.parse(EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (post != null) {
                    post.abort();
                }
                response.close();
                client.close();
            } catch (Exception e) {
                // nothing to do
            }
        }
        return JacksonMapper.newErrorInstance(null);
    }


    /**
     * 发送短信
     *
     * @param mobile    手机号,只能是一个
     * @param values    短信内容
     * @param templetid 104291       【Value1】验证码【Value2】，您正在进行认证，验证码【Value3】分钟内有效。千万不要说出去哦！【Value4】
     * @param templetid 100453       尊敬的用户您好，您在【Value1】获取的验证码为：【Value2】。如非本人操作请忽略本条短信【Value4】

     * @return
     */
    public static JsonNode sendSms(String mobile, String templetid, List<String> values) {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        HttpPost post = null;
        try {
            client = getHttpClient();
            post = new HttpPost(SMS_SEND_URL);
            post.setEntity(createSendEntity(mobile, templetid, SmsTplUtil.Sign, values));
            response = client.execute(post);
            if (response.getEntity() != null) {
                return JacksonMapper.parse(EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (post != null) {
                    post.abort();
                }
                response.close();
                client.close();
            } catch (Exception e) {
                // nothing to do
            }
        }
        return JacksonMapper.newErrorInstance(null);
    }

    /**
     * 发送
     *
     * @param mobile
     * @param templetid
     * @param values
     * @return
     * @throws UnsupportedEncodingException
     */
    private static HttpEntity createSendEntity(String mobile, String templetid, String sign, List<String> values) throws UnsupportedEncodingException {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("domain", DOMAIN));
        parameters.add(new BasicNameValuePair("calleeNbr", mobile));
        parameters.add(new BasicNameValuePair("templetid", templetid));
        if(StringUtils.isNotEmpty(sign)){
            parameters.add(new BasicNameValuePair("Sign", sign));
        }
        for (int i = 0; i < values.size(); i++) {
            parameters.add(new BasicNameValuePair("value" + (i + 1), values.get(i)));
        }
        HttpEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
        return entity;
    }

    private static CloseableHttpClient getHttpClient() throws Exception {
        // 为避免时间过长，不retry
        DefaultHttpRequestRetryHandler retryhandler = new DefaultHttpRequestRetryHandler(0, false);
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(HTTPCLIENT_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(HTTPCLIENT_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(HTTPCLIENT_TIMEOUT);

        configBuilder.setCookieSpec(CookieSpecs.IGNORE_COOKIES);

        RequestConfig requestConfig = configBuilder.build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setRetryHandler(retryhandler)
                .setDefaultRequestConfig(requestConfig)
                .build();
        return httpClient;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        //10001 您的验证码是【Value1】，在【Value2】分钟内输入有效。【Value3】
        String id1 = "10000";
        List<String> values1 = new ArrayList<String>();
        values1.add("8888");
        values1.add("30");
//        values1.add("长城会");
        System.out.println(sendSms("15350571327", id1, values1));

    }
}
