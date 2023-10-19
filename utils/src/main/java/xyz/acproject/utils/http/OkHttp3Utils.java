package xyz.acproject.utils.http;

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName OkHttp3Utils
 * @Description TODO
 * @author BanqiJane
 * @date 2020年8月12日 下午12:46:20
 *
 * @Copyright:2020 blogs.acproject.xyz Inc. All rights reserved.
 */
public class OkHttp3Utils {

	private static final Logger LOGGER = LogManager.getLogger(OkHttp3Utils.class);
	private volatile static OkHttp3Utils okHttp3Utils;

	/**
	 * HTTP实例
	 * <p>
	 * 确保本条指令不会因编译器的优化而省略，且要求每次直接读值
	 */
	private OkHttpClient okHttpClient;

	// MEDIA_TYPE <==> Content-Type
	private static final int READ_TIMEOUT = 15;
	private static final int CONNECT_TIMEOUT = 15;
	private static final int WRITE_TIMEOUT = 15;
	private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
	// MEDIA_TYPE_TEXT
	// post请求不是application/x-www-form-urlencoded的，全部直接返回，不作处理，即不会解析表单数据来放到request
	// parameter map中。所以通过request.getParameter(name)是获取不到的。只能使用最原始的方式，读取输入流来获取。
	private static final MediaType MEDIA_TYPE_TEXT = MediaType
			.parse("application/x-www-form-urlencoded; charset=utf-8");

	private static final MediaType MEDIA_TYPE_BODY  = MediaType
			.parse("text/plain; charset=utf-8");

	private static final MediaType MEDIA_TYPE_FILE = MediaType
			.parse("application/octet-stream");

	private OkHttp3Utils() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
		builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
		builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
		builder.connectionPool(pool());
		okHttpClient = builder.build();
	}
	/**
	* 连接池 复用http
	*/
	private static ConnectionPool pool() {
		return new ConnectionPool(100, 5, TimeUnit.MINUTES);
	}

	public static OkHttp3Utils getHttp3Utils() {

		if(okHttp3Utils==null) {
			synchronized (OkHttp3Utils.class) {
				if(okHttp3Utils==null) {
					okHttp3Utils = new OkHttp3Utils();
				}
			}
		}
		return okHttp3Utils;
	}
	public Response httpGet(String url, Map<String, String> headers, Map<String, Object> datas) throws Exception{
		Headers hearderHeaders = null;
		StringBuilder stringBuilder= null;
		Request request=null;
		if (datas != null && datas.size() > 0) {
			stringBuilder = new StringBuilder(100);
			if(url.indexOf("?")<0&&url.indexOf("=")<0) {
				stringBuilder.append("?");
			}else{
				if(!url.endsWith("&")){
					stringBuilder.append("&");
				}
			}
			for (Entry<String, Object> entry : datas.entrySet()) {
				stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
		}
		if(stringBuilder!=null) {
			url = stringBuilder.insert(0, url).toString();
		}
		if (headers != null && headers.size() > 0) {
			hearderHeaders = Headers.of(headers);
			request = new Request.Builder().url(url).headers(hearderHeaders).get().build();
		}else {
			request = new Request.Builder().url(url).get().build();
		}
		LOGGER.debug("okhttp -> url:{},method:{},headers:{},datas:{}",url,"GET",headers!=null?headers.toString():null,datas!=null?datas.toString():null);
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			LOGGER.debug("okhttp return -> response:{}",response);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		}finally {
//			if(headers!=null)headers.clear();
//			if(datas!=null)datas.clear();
//		}
		return response;
	}
	public Response  httpPostJson(String url,Map<String, String> headers,String json) throws Exception{
		RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
		Request request=null;
		Headers hearderHeaders = null;
		Response response = null;
		if (headers != null && headers.size() > 0) {
			hearderHeaders = Headers.of(headers);
			request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
		}else {
			request = new Request.Builder().url(url).post(body).build();
		}
		LOGGER.debug("okhttp -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,json);
		try {
			response = okHttpClient.newCall(request).execute();
			LOGGER.debug("okhttp return -> response:{}",response);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally {
//			if(headers!=null)headers.clear();
//		}
		return response;
	}
	public Response httpPostForm(String url, Map<String, String> headers, Map<String, Object> params) throws Exception {
		Request request=null;
		StringBuilder content = new StringBuilder();
		if(params!=null) {
			Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				content.append(entry.getKey()).append("=").append(entry.getValue());
				if (iterator.hasNext()) {
					content.append("&");
				}
			}
		}
		RequestBody body = RequestBody.create(MEDIA_TYPE_TEXT, content.toString());
		Headers hearderHeaders = null;
		if (headers != null && headers.size() > 0) {
			hearderHeaders = Headers.of(headers);
			request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
		}else {
			request = new Request.Builder().url(url).post(body).build();
		}
		LOGGER.debug("okhttp -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,params!=null?params.toString():null);
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			LOGGER.debug("okhttp return -> response:{}",response);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally {
//			if(headers!=null)headers.clear();
//			if(params!=null)params.clear();
//		}
		return response;
	}

	public Response httpPostFileForm(String url, Map<String, String> headers, byte[] fileBytes) throws Exception {
		Request request=null;
		RequestBody body  = MultipartBody.create(MEDIA_TYPE_FILE, fileBytes);
		Headers hearderHeaders = null;
		if (headers != null && headers.size() > 0) {
			hearderHeaders = Headers.of(headers);
			request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
		}else {
			request = new Request.Builder().url(url).post(body).build();
		}
		LOGGER.debug("okhttp -> url:{},method:{},headers:{},size:{}",url,"POST-FILE",headers!=null?headers.toString():null,fileBytes.length);
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			LOGGER.debug("okhttp return -> response:{}",response);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally {
//			if(headers!=null)headers.clear();
//		}
		return response;
	}

	public Response httpPostBody(String url,Map<String, String> headers,String data){
		Request request=null;
		RequestBody body = RequestBody.create(MEDIA_TYPE_BODY, data);
		Headers hearderHeaders = null;
		if (headers != null && headers.size() > 0) {
			hearderHeaders = Headers.of(headers);
			request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
		}else {
			request = new Request.Builder().url(url).post(body).build();
		}
		LOGGER.debug("okhttp -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,data!=null?data:null);
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			LOGGER.debug("okhttp return -> response:{}",response);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally {
//			if(headers!=null)headers.clear();
//			if(params!=null)params.clear();
//		}
		return response;
	}




	//自定义 client

	public Response httpGet(String url, Map<String, String> headers, Map<String, Object> datas,OkHttpClient okHttpClient) throws Exception{
		Headers hearderHeaders = null;
		StringBuilder stringBuilder= null;
		Request request=null;
		if (datas != null && datas.size() > 0) {
			stringBuilder = new StringBuilder(100);
			if(url.indexOf("?")<0&&url.indexOf("=")<0) {
				stringBuilder.append("?");
			}else{
				if(!url.endsWith("&")){
					stringBuilder.append("&");
				}
			}
			for (Entry<String, Object> entry : datas.entrySet()) {
				stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
		}
		if(stringBuilder!=null) {
			url = stringBuilder.insert(0, url).toString();
		}
		if (headers != null && headers.size() > 0) {
			hearderHeaders = Headers.of(headers);
			request = new Request.Builder().url(url).headers(hearderHeaders).get().build();
		}else {
			request = new Request.Builder().url(url).get().build();
		}
		LOGGER.debug("okhttp -> url:{},method:{},headers:{},datas:{}",url,"GET",headers!=null?headers.toString():null,datas!=null?datas.toString():null);
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			LOGGER.debug("okhttp return -> response:{}",response);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		}finally {
//			if(headers!=null)headers.clear();
//			if(datas!=null)datas.clear();
//		}
		return response;
	}
	public Response  httpPostJson(String url,Map<String, String> headers,String json,OkHttpClient okHttpClient) throws Exception{
		RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
		Request request=null;
		Headers hearderHeaders = null;
		Response response = null;
		if (headers != null && headers.size() > 0) {
			hearderHeaders = Headers.of(headers);
			request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
		}else {
			request = new Request.Builder().url(url).post(body).build();
		}
		LOGGER.debug("okhttp -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,json);
		try {
			response = okHttpClient.newCall(request).execute();
			LOGGER.debug("okhttp return -> response:{}",response);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally {
//			if(headers!=null)headers.clear();
//		}
		return response;
	}
	public Response httpPostForm(String url, Map<String, String> headers, Map<String, Object> params,OkHttpClient okHttpClient) throws Exception {
		Request request=null;
		StringBuilder content = new StringBuilder();
		Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			content.append(entry.getKey()).append("=").append(entry.getValue());
			if (iterator.hasNext()) {
				content.append("&");
			}
		}
		RequestBody body = RequestBody.create(MEDIA_TYPE_TEXT, content.toString());
		Headers hearderHeaders = null;
		if (headers != null && headers.size() > 0) {
			hearderHeaders = Headers.of(headers);
			request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
		}else {
			request = new Request.Builder().url(url).post(body).build();
		}
		LOGGER.debug("okhttp -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,params!=null?params.toString():null);
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			LOGGER.debug("okhttp return -> response:{}",response);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally {
//			if(headers!=null)headers.clear();
//			if(params!=null)params.clear();
//		}
		return response;
	}

	public Response httpPostFileForm(String url, Map<String, String> headers, byte[] fileBytes,OkHttpClient okHttpClient) throws Exception {
		Request request=null;
		RequestBody body  = MultipartBody.create(MEDIA_TYPE_FILE, fileBytes);
		Headers hearderHeaders = null;
		if (headers != null && headers.size() > 0) {
			hearderHeaders = Headers.of(headers);
			request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
		}else {
			request = new Request.Builder().url(url).post(body).build();
		}
		LOGGER.debug("okhttp -> url:{},method:{},headers:{},size:{}",url,"POST-FILE",headers!=null?headers.toString():null,fileBytes.length);
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			LOGGER.debug("okhttp return -> response:{}",response);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally {
//			if(headers!=null)headers.clear();
//		}
		return response;
	}

	public Response httpPostBody(String url,Map<String, String> headers,String data,OkHttpClient okHttpClient){
		Request request=null;
		RequestBody body = RequestBody.create(MEDIA_TYPE_BODY, data);
		Headers hearderHeaders = null;
		if (headers != null && headers.size() > 0) {
			hearderHeaders = Headers.of(headers);
			request = new Request.Builder().url(url).headers(hearderHeaders).post(body).build();
		}else {
			request = new Request.Builder().url(url).post(body).build();
		}
		LOGGER.debug("okhttp -> url:{},method:{},headers:{},datas:{}",url,"POST",headers!=null?headers.toString():null,data!=null?data:null);
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			LOGGER.debug("okhttp return -> response:{}",response);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally {
//			if(headers!=null)headers.clear();
//			if(params!=null)params.clear();
//		}
		return response;
	}
}
