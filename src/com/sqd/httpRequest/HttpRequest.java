package com.sqd.httpRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HttpRequest {
	static boolean proxySet = false;
	static String proxyHost = "127.0.0.1";
	static int proxyPort = 1080;
	
	/**
     * 向指定 URL 发送GET方法的请求
     * @param url  发送请求的 URL
     * @param params  请求参数，请求参数应该是 name1=value1&name2=value2的形式。
     * @return  所代表远程资源的响应结果
     */
	public static String sendGet(String url,String params){
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			if(params!=null)urlNameString+="?"+params;
			System.out.println(urlNameString);
			URL realUrl = new URL(urlNameString);
			//打开和URL之间的链接
			URLConnection connection = realUrl.openConnection();
			//设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			//建立实际的链接
			connection.connect();
			//获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			//遍历所有的响应头字段
			for (String key : map.keySet()){
				System.out.println(key + "--->" + map.get(key));
			}
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = in.readLine())!=null){
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		} finally{
			try {
				if(in != null){
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	/**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url  发送请求的 URL
     * @param param  请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     *               x-www-form-urlencoded
     * @param isproxy  是否使用代理模式
     * @return 所代表远程资源的响应结果
     */
	public static String sendPost(String url,String param,boolean isproxy){
		String result = "";
		OutputStreamWriter out = null;
		BufferedReader in = null;
		try{
			URL realUrl = new URL(url);
			HttpURLConnection conn = null;
			if(isproxy){
				@SuppressWarnings("static-access")
				Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost,proxyPort));
				conn = (HttpURLConnection) realUrl.openConnection();
			}else{
				conn = (HttpURLConnection) realUrl.openConnection();
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Conntent-Type", "application/x-www-form-urlencoded");
			
			conn.connect();
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			out.write(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line ;
			while((line = in.readLine()) != null){
				result += line;
			}
		}catch(Exception e){
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}finally{
			try {
				if(out != null){
					out.close();
				}
				if(in != null){
					in.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	/**
		 * 向指定 URL  发送POST form-data 方法的请求
		 * @param url  发送请求的 URL
		 * @param map  请求参数
		 * @param is  请求文件
		 * @return  所代表远程资源的响应结果
		 */
		public static String sendOfFormDataPost(String url,Map<String, Object> map,Map<String, File> is){
			String result = "";
		DataOutputStream outStream = null;
		BufferedReader br = null;
		InputStream in = null;
		try {
			URL url0 = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) url0.openConnection();//创建一个HTTP链接
			conn.setConnectTimeout(30000);//连接超时时间
			conn.setReadTimeout(120000);//读取超时时间
			conn.setDoInput(true);//向连接中写入数据
			conn.setDoOutput(true);//从连接中读取数据
			conn.setUseCaches(false);//禁止缓存
			conn.setRequestMethod("POST");//请求方式
			//请求头
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			
			String BOUNDARY = java.util.UUID.randomUUID().toString();
			String PREFIX = "--";
			String LINEND = "\r\n";
			String MULTIPART_FROM_DATA = "multipart/form-data";
			String CHARSET = "UTF-8";
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
			
			StringBuilder sb = new StringBuilder();//首先组拼文本类型的参数
			//发送请求参数 请求格式请百度form-data报文
			for(Entry <String,Object> entry : map.entrySet()){
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\"" 
						+ entry.getKey()+"\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET 
						+ LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}
			
			//创建输入数据流
			outStream = new DataOutputStream(conn.getOutputStream());
			//写入数据
			outStream.write(sb.toString().getBytes());
			//发送文件数据
			StringBuilder sb0 = new StringBuilder();
			for(Entry<String,File> entry : is.entrySet()){
				sb0.append(PREFIX);
				sb0.append(BOUNDARY);
				sb0.append(LINEND);
				sb0.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"; filename=\""
						+ entry.getValue().getName() + "\"" + LINEND);
				sb0.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb0.append(LINEND);
				outStream.write(sb0.toString().getBytes());
				in = new FileInputStream(entry.getValue());
				byte[] buffer = new byte[1024 * 5];
				int len = 0;
				//项数据流写入文件
				while((len = in.read(buffer)) != -1){
					outStream.write(buffer, 0 , len);
				}
				in.close();
				outStream.write(LINEND.getBytes());
			}
			
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			//得到响应码
			int reCode = conn.getResponseCode();
			if(reCode == 200){
				//将数据转换成字符流
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line ;
				while((line = br.readLine()) != null){
					result += line;
				}
			}else{
				result+="faild";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			result+="Error";
		} catch (IOException e) {
			e.printStackTrace();
			result+="Error";
		}finally{
			try {
				if(in!=null)in.close();
		
				if(br!=null)br.close();
				
				if(outStream!=null)outStream.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		/*String re = HttpRequest.sendGet("https://free-api.heweather.com/s6/weather", "key=e06d63122e2b4afbb7a819a7eeb6494a&location=108.38443,22.81371");
		JSONObject json = JSONObject.parseObject(re);
		JSONArray arr = json.getJSONArray("HeWeather6");
		
		System.out.println(JSON.toJSONString(arr));*/
		/*String re = HttpRequest.sendPost("http://192.168.2.163:28080/web-portal/o2oPk/getPKDetails.action", "jsonStr={'id':19}",false);
		JSONObject json = JSONObject.parseObject(re);
		
		System.out.println(JSON.toJSONString(json));*/
		
		Map<String, Object> strMap = new HashMap<String, Object>();
		strMap.put("id", 15);
		strMap.put("name", "sqd");
		Map<String, File> fileMap = new HashMap<String, File>();
		fileMap.put("file", new File("F:/235034-140Q60K05695.jpg"));
		String re = HttpRequest.sendOfFormDataPost("http://192.168.2.163:28080/web-portal/team/fileUpload.action", strMap, fileMap);
		System.out.println(re);
	}
}
