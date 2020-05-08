package com.kingyee.vipclass.controller.jsoup;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class test {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		Element jbyp =getDivByUrl("http:///www.7lk.com/j-42_1/?rx=0",0);
		System.out.println(jbyp);
	}
	private static Element getDivByUrl(String jbUrl,int sfcf) throws IOException, InterruptedException {
		jbUrl = "http://www.7lk.com/j-42_1/?rx=0";
		Connection con = Jsoup.connect(jbUrl)
				.data("query", "Java")
				// 请求参数
				.userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)") // 设置
				.timeout(30000); // 设置连接超时时间
		/*
		 * .ignoreHttpErrors(true) .followRedirects(true)
		 */

		Connection.Response resp = con.execute();
		Document doc = null;
		if (resp.statusCode() == 200) {
			doc = con.get();
		}
		Element jbyp = doc.select("ul.result-cantainer").first();// 获取疾病对应的药
		return jbyp;
	}
}
