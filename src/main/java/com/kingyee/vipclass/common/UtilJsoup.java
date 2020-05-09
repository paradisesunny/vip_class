package com.kingyee.vipclass.common;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class UtilJsoup {
	public static void main(String[] args) throws Exception {
		getHtmlByUrl("http:///www.7lk.com/j-181");
	}
	public static Document getHtmlByUrl(String url) throws Exception{
		try{
			Connection con = Jsoup
					.connect(url)
					.data("query", "Java")
					// 请求参数
					.userAgent(
							"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)") // 设置
																														// User-Agent
					.timeout(30000); // 设置连接超时时间
			/*
			 * .ignoreHttpErrors(true) .followRedirects(true)
			 */
			Connection.Response resp = con.execute();
			Document doc = null;
			if (resp.statusCode() == 200) {
				doc = con.get();
			}
			 return doc;
		}catch(Exception e){
			e.printStackTrace();
			//拨号一下  
	//        ConnectAdslNet.reconnectAdsl("宽带",Main.PORT_PROP_NAME,Main.PORT_PROP_NAME);   
	        //使用代理
			System.out.println("开始使用代理");
			String result = Poxy.getHtml(url);
			if(result != null){
				 Document doc = Jsoup.parse(result);
				 System.out.println("使用代理结束");
				 return doc;
			}else{
				System.out.println("使用代理失败");
				System.out.println("使用代理结束");
				throw new Exception();
			}
		}
	}
}
