package com.kingyee.vipclass.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import javax.swing.text.Document;

public class UtilHttpclient {
	/**
	 * 通过url获取网站html
	 * 
	 * @param url
	 *            网站url
	 */
	public static Document getHtmlByUrl(String url){
	    HttpClient client = null;
	    HttpPost post = null;
	    Document doc = null;
		client = new DefaultHttpClient();
		post = new HttpPost(url);
		// HttpPost连接对象
        HttpPost httpRequest=new HttpPost(url);
        //使用NameValuePair来保存要传递的Post参数
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        //添加要传递的参数
        params.add(new BasicNameValuePair("par","HTTP_Client_android_Post"));
	  try {
          //设置字符集
          HttpEntity httpentity=new UrlEncodedFormEntity(params,"gb2312");
          //请求httpRequest
          httpRequest.setEntity(httpentity);
          //取得HttpClient对象
          HttpClient httpclient=new DefaultHttpClient();
          //请求HttpCLient，取得HttpResponse
          HttpResponse httpResponse=httpclient.execute(httpRequest);
          //请求成功
          if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
        	  //取得返回的字符串
    		  String strResult=EntityUtils.toString(httpResponse.getEntity());
    		  if (strResult != null && !"".equals(strResult)) {
    			  doc = (Document) Jsoup.parse(strResult);
	  		  }else{
	  			  throw new Exception();
	  		  }
          }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (post != null) {
					post.abort();
				}
				client.getConnectionManager().shutdown();
			} catch (Exception e2) {
				// noting to do
			}
		}
		return doc;
	 }

	public static void main(String[] args) {
		getHtmlByUrl("http://www.7lk.com/");
	}
	/**
	  * 通过url获取网站html
	  * @param url 网站url
	  */
	 public static Document getHtmlByUrl1(String url){
		 HttpClient client = null;
		 HttpPost post = null;
		 Document doc = null;
		try {
			client = new DefaultHttpClient();
			post = new HttpPost(url);
			HttpResponse response = client.execute(post);
			String responseStr = EntityUtils.toString(response.getEntity());
			if (responseStr != null && !"".equals(responseStr)) {
				doc = (Document) Jsoup.parse(responseStr);
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (post != null) {
					post.abort();
				}
				client.getConnectionManager().shutdown();
			} catch (Exception e2) {
				// noting to do
			}
		}
		return doc;
	 }
}
