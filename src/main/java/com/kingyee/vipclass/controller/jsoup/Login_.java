package com.kingyee.vipclass.controller.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Login_ {

	private final static String domain = "http://www.58dslt.com/";



	public static void main(String[] args)throws Exception {
		Login_ jli=new Login_();
		jli.login("一个胡萝北", "123456");//输入Iteye的用户名，和密码

	}
	/**
	 * 模拟登陆Iteye
	 *
	 * @param userName 用户名
	 * @param pwd 密码
	 *
	 * **/
	public void login(String userName,String pwd)throws Exception{

		String urlLogin = domain+"member.php?mod=logging&action=login";
		//第一次请求
		Connection con=Jsoup.connect(urlLogin);//获取连接
		con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");//配置模拟浏览器
		Connection.Response rs= con.execute();//获取响应
		Document d1=Jsoup.parse(rs.body());//转换为Dom树
		List<Element> et= d1.select("form");//获取form表单，可以通过查看页面源码代码得知
		Elements a = et.get(1).getAllElements();
		Elements form = d1.select("form[name=login]");
		String actionUrl = form.attr("action");
		//获取，cooking和表单属性，下面map存放post时的数据
		Map<String, String> datas = new HashMap<>();
		datas.put("username", userName);
		datas.put("password", pwd);

		for(Element e:et.get(1).getAllElements()){
			if(e.attr("name").equals("formhash")){
				datas.put(e.attr("name"), e.attr("value"));
			}
			if(e.attr("name").equals("referer")){
				datas.put(e.attr("name"), e.attr("value"));
			}
		}
		System.out.println();

		/**
		 * 第二次请求，post表单数据，以及cookie信息
		 *
		 * **/
		/*String urlLoginSubmit = domain+actionUrl+"&username="+userName+"&password="+pwd;
		Connection con2=Jsoup.connect(urlLoginSubmit);
		con2.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		//设置cookie和post上面的map数据
		Connection.Response login=con2.ignoreContentType(true).method(Connection.Method.POST).cookies(rs.cookies()).execute();
		//打印，登陆成功后的信息
		System.out.println(login.body());

		//登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可
		Map<String, String> map=login.cookies();
		for(String s:map.keySet()){
			System.out.println(s+"      "+map.get(s));
		}*/

	}



}
