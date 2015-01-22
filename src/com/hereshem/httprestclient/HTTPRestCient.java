package com.hereshem.httprestclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HTTPRestCient{
	
	Context context;
	OnNetworkListener action; // for storing the caller activity to return data
	ServerConnection connection;
	
	// // constructor
	public HTTPRestCient(Context context, OnNetworkListener listener) {
		this.context = context;
		this.action = listener;
		connection = new ServerConnection();
	}
	
	public HTTPRestCient(Context context) {
		this.context = context;
		connection = new ServerConnection();
	}

	public void setOnNetworkListener(OnNetworkListener listner) {
		this.action = listner;
		connection = new ServerConnection();
	}
	
	
	public void processRequest(RequestData request) {
		if(isNetworkConnected(context)){
			new AsyncServerRequest(request).execute();
		}
		else{
			toast("No internet connection");
			showActionResult(request, 0, genErrorMsg(0, "No internet connection"));
		}
	}
	
	
	private void toast(String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}
	private static void trace(String string) {
		if (BuildConfig.DEBUG) 
			Log.i("PvConnect", string);
	}

	
	// ////// Async task activity for selecting right url and parameters
	protected class AsyncServerRequest extends AsyncTask<Void, Void, String>{
		RequestData request;
		int status = 0;
		
		public AsyncServerRequest(RequestData request) {
			this.request = request;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(context instanceof Activity){
				// do some loading stuffs
			}
		}
		
		@Override
		protected String doInBackground(Void... params) {
			HttpResponse response = connection.PvRequestHttp(request);
			try{
				status  = response.getStatusLine().getStatusCode();
				trace("Status code is " + status);
				HttpEntity httpEntity = response.getEntity();
				return EntityUtils.toString(httpEntity);
			} catch(Exception e){
				e.printStackTrace();
				return genErrorMsg(1, e.getMessage());
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(context instanceof Activity){
				// do hiding loading stuffs
			}
			showActionResult(request, status, result);
		}
	}

	private String genErrorMsg(int status, String string) {
		JSONObject jsObj = new JSONObject();
		JSONObject jObj = new JSONObject();
		try{
			jObj.put("status", status);
			jObj.put("message", string);
			jsObj.put("error", jObj);
		}catch(Exception e){}
		return jsObj.toString();
	}

	private void showActionResult(RequestData request, int status, String result) {
		if(status == 200){		// response ok from server
			ResponseSuccessData okdata = new ResponseSuccessData(result, status);
			if(action != null)
				action.onSuccessConnection(request, okdata);
		}
		else{
			ResponseErrorData errdata = new ResponseErrorData(result, status);
			if(status == 401){ 
				trace("401 unauthorized :: request url = " + request.getUrl());
				// do something 
			}
			if(action != null)
				action.onErrorConnection(request, errdata, status);
		}
	}
	
	public static boolean isNetworkConnected(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm == null){
			trace("Permission not added in Manifest");
			return false;
		}
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		}
		return true;
	}
}
