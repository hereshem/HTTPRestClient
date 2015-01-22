package com.hereshem.httprestclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;
class ServerConnection{
	
	private static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
	private static final int HTTP_UPLOAD_TIMEOUT = 120 * 1000; // milliseconds
	
	private DefaultHttpClient getDefaultHttpClient(int timeout){
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);
		ConnManagerParams.setTimeout(params, timeout);
		DefaultHttpClient client = new DefaultHttpClient(params);
		return client;
	}
	
	public HttpResponse RequestGetHttp(String url) {
		try{
			HttpGet request = new HttpGet(new URI(url));
			return getDefaultHttpClient(HTTP_TIMEOUT).execute(request);
		}
		catch(Exception e){e.printStackTrace();}
		return null;
	}
	
	public HttpResponse RequestGetHttp(String url, List<NameValuePair> parameters) {
		if(parameters == null){	parameters = new ArrayList<NameValuePair>();}
		parameters.add(new BasicNameValuePair("timestamp", String.valueOf(System.currentTimeMillis())));
		return RequestGetHttp(url + "?" + URLEncodedUtils.format(parameters, "utf-8"));
	}

	public HttpResponse RequestPostHttp(String url, List<NameValuePair> parameters) {
		try{
			HttpPost request = new HttpPost(new URI(url));
			if(parameters != null){
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "utf-8");
				request.setEntity(formEntity);
			}
			return getDefaultHttpClient(HTTP_TIMEOUT).execute(request);
		}
		catch(Exception e){e.printStackTrace();}
		return null;
	}
	
	public HttpResponse RequestPostHttpMultipart(String url, List<NameValuePair> parameters) {
		try{
			HttpPost request = new HttpPost(new URI(url));
			MultipartEntity entity = new MultipartEntity();
			Charset charset = Charset.forName("utf-8");
			if(parameters != null)
			for (int i = 0; i < parameters.size(); i++) {
				String key = parameters.get(i).getName();
				String value = parameters.get(i).getValue();
				if(value.startsWith("/")){
					entity.addPart(key, new FileBody(new File(value), "application/octet-stream"));
				}
				else{
					entity.addPart(key, new StringBody(value, charset));
				}
			}
			// UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
			request.setEntity(entity);
			return getDefaultHttpClient(HTTP_UPLOAD_TIMEOUT).execute(request);
		}
		catch(FileNotFoundException e){e.printStackTrace();}
		catch(Exception e){e.printStackTrace();}
		return null;
	}
	
	public HttpResponse RequestPutHttp(String url, String extras[]) {
		try{
			HttpPut request = new HttpPut(new URI(url));
			request.setEntity(new FileEntity(new File(extras[0]), "application/octet-stream"));
			return getDefaultHttpClient(HTTP_UPLOAD_TIMEOUT).execute(request);
		}
		catch(Exception e){e.printStackTrace();}
		return null;
	}

	
	public HttpResponse RequestDeleteHttp(String url) {
		try{
			HttpDelete request = new HttpDelete(new URI(url));
			return getDefaultHttpClient(HTTP_TIMEOUT).execute(request);
		}
		catch(Exception e){e.printStackTrace();}
		return null;
	}
	
	
	public HttpResponse RequestDownloadFile(String url, String[] extras) {
		return null;
	}

	public HttpResponse PvRequestHttp(RequestData request) {
		switch(request.getMethod()){
			case Utils.GET:
				return RequestGetHttp(request.getUrl());
			case Utils.POST:
				return RequestPostHttp(request.getUrl(), request.getParameters());
			case Utils.PUT:
				Log.i("PVServerConnect", "Uploading file with filename " + request.getExtras()[0]);
				return RequestPutHttp(request.getUrl(), request.getExtras());
			case Utils.DELETE:
				return RequestDeleteHttp(request.getUrl());
			case Utils.UPLOAD:
				return RequestPostHttpMultipart(request.getUrl(), request.getParameters());
			case Utils.DOWNLAOD:
				return RequestDownloadFile(request.getUrl(), request.getExtras());
			default:
				return null;
		}
	}
	
	/*public static boolean isNetworkConnected(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		}
		else return true;
	}*/
}
