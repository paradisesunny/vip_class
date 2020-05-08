
package com.kingyee.vipclass.controller.jsoup;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class jsoupTest {
	public static void main(String[] args) {
		/*
         * Document doc = Jsoup.connect("http://www.oschina.net/")
         * .data("query", "Java") // 请求参数 .userAgent("I ’ m jsoup") // 设置
         * User-Agent .cookie("auth", "token") // 设置 cookie .timeout(3000) //
         * 设置连接超时时间 .post();
         */// 使用 POST 方法访问 URL

        /*
         * // 从文件中加载 HTML 文档 File input = new File("D:/test.html"); Document doc
         * = Jsoup.parse(input,"UTF-8","http://www.oschina.net/");
         */
	    String html = "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>";  
	    Document doc = Jsoup.parse(html);//解析HTML字符串返回一个Document实现  
	    System.out.println("doc:"+doc);
	    Element link = doc.select("a").first();//查找第一个a元素  
	    System.out.println("查找第一个a元素:"+link);//<a href="http://example.com/"><b>example</b></a>
	      
	    String text = doc.body().text(); // "An example link"//取得字符串中的文本  
	    System.out.println("取得字符串中的文本:"+text);
	    
	    String linkHref = link.attr("href"); // "http://example.com/"//取得链接地址  
	    System.out.println("取得链接地址:"+linkHref);
	    
	    String linkText = link.text(); // "example""//取得链接地址中的文本  
	    System.out.println("取得链接地址中的文本:"+linkText);
	      
	    String linkOuterH = link.outerHtml();   
	    System.out.println("11111:"+linkOuterH);
	        // "<a href="http://example.com"><b>example</b></a>"  
	    
	    String linkInnerH = link.html(); // "<b>example</b>"//取得链接内的html内容 
	    System.out.println("取得链接内的html内容 :"+linkInnerH);
	}
	 /**
     * 获取指定HTML 文档指定的body
     * @throws IOException
     */
    private static void BolgBody() throws IOException {
        // 直接从字符串中输入 HTML 文档
        String html = "<html><head><title> 开源中国社区 </title></head>"
                + "<body><p> 这里是 jsoup 项目的相关文章 </p></body></html>";
        Document doc = Jsoup.parse(html);
        System.out.println(doc.body());
        
        
        Connection con= Jsoup.connect("http://www.7lk.com/") 
		.data("query", "Java")   // 请求参数
		.userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)") // 设置 User-Agent 
		.timeout(30000); // 设置连接超时时间
		/*.ignoreHttpErrors(true)
		.followRedirects(true)*/

		Connection.Response resp = con.execute();
		Document doc1 = null; 
		if (resp.statusCode() == 200){ 
		doc1 = con.get(); 
		}
		System.out.println(doc1);
        
        // 从 URL 直接加载 HTML 文档
//        Document doc2 = Jsoup.connect("http://www.oschina.net/").get();
//        String title = doc2.body().toString();
//        System.out.println(title);
    }

    /**
     * 获取博客上的文章标题和链接
     */
    public static void article() {
        Document doc;
        try {
            doc = Jsoup.connect("http://www.cnblogs.com/zyw-205520/").get();
            Elements ListDiv = doc.getElementsByAttributeValue("class","postTitle");
            for (Element element :ListDiv) {
                Elements links = element.getElementsByTag("a");
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    String linkText = link.text().trim();
                    System.out.println(linkHref);
                    System.out.println(linkText);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    /**
     * 获取指定博客文章的内容
     */
    public static void Blog() {
        Document doc;
        try {
            doc = Jsoup.connect("http://www.cnblogs.com/zyw-205520/archive/2012/12/20/2826402.html").get();
            Elements ListDiv = doc.getElementsByAttributeValue("class","postBody");
            for (Element element :ListDiv) {
                System.out.println(element.html());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
