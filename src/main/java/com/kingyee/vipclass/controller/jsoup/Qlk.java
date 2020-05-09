package com.kingyee.vipclass.controller.jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import com.kingyee.vipclass.common.Poxy;
import com.kingyee.vipclass.common.UtilJsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class Qlk {
	/**
	 * @param args
	 * @throws Exception
	 */
	private final static String domain = "http://www.58dslt.com/";

	private static String ksName;
	private static String jbName;
	private static String ypName;
	private static String sfcf;
	private static String ypUrl;
	private static String sfcfStr;
	private static int page;
	/**
	 * 第一次访问获取的cookie(查看发现就返回一个cookie:ASP.NET_SessionId)
	 */
	private static  Map<String, String> cookies = null;
	/**
	 * __viewstate    教务系统用于验证的信息
	 */
	private static String viewState = null;

	public Qlk() {
		cookies = new HashMap<String,String>();;
		viewState = "";
	}
	
	public static void main(String[] args) throws Exception {
		String urlLogin = domain+"member.php?mod=logging&action=login&loginsubmit=yes";
		/*Document doc = UtilJsoup.getHtmlByUrl(url);
		GetQlk(doc);*/
		Connection connect = Jsoup.connect(urlLogin);
		// 伪造请求头
		connect.header("Accept", "application/json, text/javascript, */*; q=0.01").header("Accept-Encoding",
				"gzip, deflate");
		connect.header("Accept-Language", "zh-CN,zh;q=0.9").header("Connection", "keep-alive");
		connect.header("Content-Length", "213").header("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		connect.header("Host", "www.58dslt.com").header("Referer", urlLogin);
		connect.header("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
				.header("X-Requested-With", "XMLHttpRequest");
		Map<String, String> loginMap = new HashMap<String, String>();
//		loginMap.put("username", "小悟空");
//		loginMap.put("password", "XINGZHE123");
		loginMap.put("username", "一个胡萝北");
		loginMap.put("password", "123456");

		// 请求url获取响应信息
		// 执行请求
		Connection.Response res = connect.ignoreContentType(true).data(loginMap).method(Connection.Method.POST).execute();
		// 获取返回的cookie
		cookies = res.cookies();
		for (Map.Entry<String, String> entry : cookies.entrySet()) {
			System.out.println(entry.getKey() + "-" + entry.getValue());
		}


		//https://ask.csdn.net/questions/165013
		Document doc = res.parse();
		//这儿的SESSIONID需要根据要登录的目标网站设置的session Cookie名字而定
		String sessionId = res.cookie("_d_id");
		//在上面的代码成功登录后，就可以利用登录的cookie来保持会话，抓取网页内容了
		String userCenter = domain+"home.php?mod=space&uid=51073";

		String huifu = domain+"forum.php?mod=post&infloat=yes&action=reply&fid=86&extra=&tid=10493&replysubmit=yes";
		Map<String, String> huifukejian = new HashMap<String, String>();

		huifukejian.put("formhash", "e9d11853");
		huifukejian.put("handlekey", "reply");
		huifukejian.put("noticeauthor", "");
		huifukejian.put("noticetrimstr", "");
		huifukejian.put("noticeauthormsg", "");
		huifukejian.put("usesig", "");

		huifukejian.put("message", "学习了！！！");

		Document objectDoc = Jsoup.connect(huifu)
				.cookie("_d_id", sessionId)
				.get();

		// 获取响应体
		String body = res.body();
		System.out.println();

		// 调用下面方法获取__viewstate
		getViewState(body);// 获取viewState
	}
	/**
	 * 获取viewstate
	 *
	 * @return
	 */
	public static String getViewState(String htmlContent) {
		Document document = Jsoup.parse(htmlContent);
		Element ele = document.select("input[name='__VIEWSTATE']").first();
		String value = ele.attr("value");
		// 获取到viewState
		viewState = value;
		return value;
	}
	
	/**
	  * 通过url获取网站html
	  * @param url 网站url
	  */
	 public static void parseHtml (String url) {
		 HttpClient client = null;
		 HttpPost post = null;
		try {
			client = new DefaultHttpClient();
			post = new HttpPost(url);
			HttpResponse response = client.execute(post);

			String responseStr = EntityUtils.toString(response.getEntity());
			Document doc = null;
			if (responseStr != null && !"".equals(responseStr)) {
				doc = Jsoup.parse(responseStr);
				GetQlk(doc);
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
	 }
	
	private static void GetQlk(Document doc) throws Exception {
		Element ksyy = doc.select("div.typeone-info-boxMl").first();// 获取科室用药

		// 科室数量  此处做for循环
		int ksCount = ksyy.select("div.typeone-info-thr").size();
		System.out.println("科室数量:" + ksCount);
		//TODO
		//此处循环条件设置，表示只获取某一科的药品i=0
		for(int i = 5;i<6;i++){
			ksName = ksyy.select("div.typeone-info-thr-title").get(i).text();//科室名称
			System.out.println("第"+i+"个科室的名称:" + ksName);
			// 此处做for循环
			//第i个科室
			Element ks_ = ksyy.select("div.typeone-info-thr-con").get(i);
			int jbCount = ks_.select("a").size();
			System.out.println("第"+i+"个科室所有疾病数量:" + jbCount);
			//通过科室获取疾病
			getJbByKs(i,ks_,jbCount);
		}
	}

	private static void getJbByKs(int tag,Element ksyy, int jbCount) throws Exception {
		if(jbCount >1){
			//TODO
			//第i+1个疾病i=0
			for(int i = 0;i<jbCount;i++){
				//第i个疾病
				Element jb_ = ksyy.select("a").get(i);
				jbName = jb_.text();
				System.out.println("第"+tag+"个科室第"+i+"个疾病名称:" + jbName);
				
				String jbUrl = jb_.attr("href");
				System.out.println("第"+tag+"个科室第"+i+"个疾病url:" + jbUrl);
				
				
				//疾病对应的药品
				Element jbyp = null;
				Thread.sleep(4000);
				//非处方药========================
				page = getAllPage(jbUrl,0);//获取总页数
				if(page >0){
					for(int j = 1;j<=page;j++){
						//拼url
						String url = jbUrl.split("/")[0]+"/"+jbUrl.split("/")[1]+"/"+jbUrl.split("/")[2]+"/"+jbUrl.split("/")[3]+"_"+j;
						url = url+"/?rx=0";
						jbyp = getDivByUrl(url);
						if(jbyp != null){
							//获取药品的h2的数量
							int ypFcfCount = jbyp.select("li.result-container-item").size();
							System.out.println("非处方药总数：" + ypFcfCount);
							//通过疾病获取药品并写入txt
							getYpByJb(i,jbyp,ypFcfCount,0);
						}
					}
				}else{
					String url = jbUrl+"?rx=0";
					jbyp = getDivByUrl(url);
					if(jbyp != null){
						//获取药品的h2的数量
						int ypFcfCount = jbyp.select("li.result-container-item").size();
						System.out.println("非处方药总数：" + ypFcfCount);
						//通过疾病获取药品并写入txt
						getYpByJb(i,jbyp,ypFcfCount,0);
					}
				}
				
				//处方药==========================
				page = getAllPage(jbUrl,1);//获取总页数
				if(page >0){
					for(int j = 1;j<=page;j++){
						//拼url
						String url = jbUrl.split("/")[0]+"/"+jbUrl.split("/")[1]+"/"+jbUrl.split("/")[2]+"/"+jbUrl.split("/")[3]+"_"+j;
						url = url+"/?rx=1";
						jbyp = getDivByUrl(url);
						if(jbyp != null){
							//获取药品的h2的数量
							int ypCfCount = jbyp.select("li.result-container-item").size();
							System.out.println("处方药总数：" + ypCfCount);
							//通过疾病获取药品并写入txt
							getYpByJb(i,jbyp,ypCfCount,1);
						}
					}
				}else{
					String url = jbUrl+"?rx=1";
					jbyp = getDivByUrl(url);
					if(jbyp != null){
						//获取药品的h2的数量
						int ypCfCount = jbyp.select("li.result-container-item").size();
						System.out.println("处方药总数：" + ypCfCount);
						//通过疾病获取药品并写入txt
						getYpByJb(i,jbyp,ypCfCount,1);
					}
				}
				
				
			}
		}
	}

	private static int getAllPage(String jbUrl, int i) throws Exception {
		Thread.sleep(4000);
		try{
			jbUrl = jbUrl.substring(0, jbUrl.length() - 1) + "?rx="+i;
			Document doc = UtilJsoup.getHtmlByUrl(jbUrl);
			//获取一共多少页
			String page_ =doc.select("span.zsPage").text();
			if(page_ == null || "".equals(page_)){
				page = 0;
			}else{
				page =  Integer.parseInt(page_.split("/")[1]);
			}
			System.out.println("一共"+page+"页");
		}catch(Exception e){
			e.printStackTrace();
			//拨号一下  
//            ConnectAdslNet.reconnectAdsl("宽带",Main.PORT_PROP_NAME,Main.PORT_PROP_NAME);   
            //使用代理
			System.out.println("开始使用代理");
			String result = Poxy.getHtml(jbUrl);
			if(result != null){
				 Document doc = Jsoup.parse(result);
				//获取一共多少页
				String page_ =doc.select("span.zsPage").text();
				if(page_ == null || "".equals(page_)){
					page = 0;
				}else{
					page =  Integer.parseInt(page_.split("/")[1]);
				}
				System.out.println("一共"+page+"页");
				System.out.println("使用代理结束");
				return page;
			}else{
				System.out.println("使用代理失败");
				System.out.println("使用代理结束");
				throw new Exception();
			}
		}
		return page;
	}

	private static void getYpByJb(int i, Element jbyp, int ypCount,int sfcf) throws Exception {
		boolean flag = false;
		if(ypCount >=1){
			System.out.println("一共"+ypCount+"条");
			System.out.println("开始写入txt");
			for (int k = 0; k < ypCount; k++) {
				// 获取第i个非处方药品的url
				ypUrl = jbyp.select("li.result-container-item").get(k).select("h2").select("a").attr("href");
				// 获取第i个非处方药品的名称
				ypName = jbyp.select("li.result-container-item").get(k).select("h2").select("a").attr("title");
				if(sfcf == 0){
					sfcfStr = "否";
				}else if(sfcf == 1){
					sfcfStr = "是";
				}
				String content = ksName + "  |  " + jbName + "  |  " + ypName + "  |  " + sfcfStr + "  |  " + ypUrl;
				System.out.println(content);
				System.out.println("这是第"+k+"条");
				flag = writeTxtFile(content + System.getProperty("line.separator"),new File("D://7lk//消化系统科.txt"));//TODO
				if(!flag){
					System.out.println(flag+"出错！！！！"+"这是第"+k+"条，本条数据为"+content);
					throw new Exception();
				}
			}
			System.out.println(flag+"本批药品写入完毕");
		}
		
	}

	/**
	 * 通过url获取指定HTML
	 * @throws Exception 
	 */
	private static Element getDivByUrl(String jbUrl) throws Exception {
		Element jbyp = null;
		try{
			Thread.sleep(4000);
			System.out.println("开始获取疾病对应的药品，疾病url："+jbUrl);
			Document doc = UtilJsoup.getHtmlByUrl(jbUrl);
			jbyp = doc.select("ul.result-cantainer").first();// 获取疾病对应的药
		}catch(Exception e){
			e.printStackTrace();
			//拨号一下  
	//        ConnectAdslNet.reconnectAdsl("宽带",Main.PORT_PROP_NAME,Main.PORT_PROP_NAME);   
	        //使用代理
			System.out.println("开始使用代理");
			String result = Poxy.getHtml(jbUrl);
			if(result != null){
				 Document doc = Jsoup.parse(result);
				 jbyp = doc.select("ul.result-cantainer").first();// 获取疾病对应的药
				 System.out.println("使用代理结束");
			}else{
				 System.out.println("使用代理失败");
				 System.out.println("使用代理结束");
				 throw new Exception();
			}
			return jbyp;
		}
		return jbyp;
	}

	// 写入txt
	public static boolean writeTxtFile(String content, File fileName)
			throws Exception {
		RandomAccessFile mm = null;
		boolean flag = false;
		FileOutputStream o = null;
		try {
			// if file doesnt exists, then create it
			if (!fileName.exists()) {
				fileName.createNewFile();
			}
			o = new FileOutputStream(fileName, true);
			o.write(content.getBytes("GBK"));
			o.close();
			// mm=new RandomAccessFile(fileName,"rw");
			// mm.writeBytes(content);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mm != null) {
				mm.close();
			}
		}
		return flag;
	}
	//写入excel
	/*public static boolean writeExcel(String content, File fileName) throws IOException, BiffException{
		boolean flag = false;
		try {
			File file = new File("D:\\yp.xlsx");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
	        //通过Workbook的静态方法getWorkbook选取Excel文件
	        Workbook workbook = Workbook.getWorkbook(file);


	        //通过Workbook的getSheet方法选择第一个工作簿（从0开始）
	        Sheet sheet = workbook.getSheet(0);

	        int rows = sheet.getRows();
	        int clos = sheet.getColumns();
	        Cell cells[][] = new Cell[clos][rows];

	        for(int c=1;c<=clos;++c){
	            for(int r=0;r<rows;++r){
	                cells[c][r] = sheet.getCell(c,r);
	                String value = "";
	                if(cells != null){
	                    if(cells[c][r].getType()==CellType.DATE_FORMULA) {
	                        value += "\t";
	                    }else if(cells[c][r].getType()==CellType.NUMBER_FORMULA){
	                         value += cells[c][r].getContents()
	                                     + "\t";
	                    }else if(cells[c][r].getType()==CellType.STRING_FORMULA){
	                        value += cells[c][r].getContents() + "\t";
	                    }else if(cells[c][r].getType()==CellType.BOOLEAN_FORMULA){
	                        value += cells[c][r].getContents() + "\t";
	                    }
	                    else{
	                        value += "\t";
	                    }
	                 }
	                System.out.println(cells[c][r].getContents());
	            }
	        }
	        //通过Sheet方法的getCell方法选择位置为C2的单元格（两个参数都从0开始）
	        Cell c2 = sheet.getCell(2,0); //(列，行)
	        //通过Cell的getContents方法把单元格中的信息以字符的形式读取出来
	        String stringc2 = c2.getContents();
	        System.out.println(stringc2);
	        workbook.close();
	        flag = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
        return flag;
	}*/

		
		
		

//	// 写入txt-notUse
//	public static boolean writeTxtFile(String content) throws Exception {
//		boolean flag = false;
//		try {
//			content = "\r\n" + content;
//
//			File file = new File("pc.txt");
//
//			// if file doesnt exists, then create it
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//
//			// true = append file
//			FileWriter fileWritter = new FileWriter(file.getName(), true);
//			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
//			bufferWritter.write(content);
//			bufferWritter.close();
//
//			System.out.println("Done");
//			flag = true;
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return flag;
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public String getKsName() {
		return ksName;
	}

	public void setKsName(String ksName) {
		this.ksName = ksName;
	}

	public String getJbName() {
		return jbName;
	}

	public void setJbName(String jbName) {
		this.jbName = jbName;
	}

	public String getYpName() {
		return ypName;
	}

	public void setYpName(String ypName) {
		this.ypName = ypName;
	}

	public String getSfcf() {
		return sfcf;
	}

	public static String getSfcfStr() {
		return sfcfStr;
	}

	public static void setSfcfStr(String sfcfStr) {
		Qlk.sfcfStr = sfcfStr;
	}

	public static String getYpUrl() {
		return ypUrl;
	}

	public static void setYpUrl(String ypUrl) {
		Qlk.ypUrl = ypUrl;
	}

	public static void setSfcf(String sfcf) {
		Qlk.sfcf = sfcf;
	}

	public static int getPage() {
		return page;
	}

	public static void setPage(int page) {
		Qlk.page = page;
	}

}
