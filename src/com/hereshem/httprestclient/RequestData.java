package com.hereshem.httprestclient;

import java.util.List;

import org.apache.http.NameValuePair;

public class RequestData {
	
	public static class RequestGetData extends RequestData{
		public RequestGetData(String url) {
			super(Utils.GET, url, null, null);
		}
	}
	public static class RequestPostData extends RequestData{
		public RequestPostData(String url, List<NameValuePair> parameters) {
			super(Utils.POST, url, parameters, null);
		}
	}
	public static class RequestPutData extends RequestData{
		public RequestPutData(String url, String file) {
			super(Utils.PUT, url, null, new String[]{file});
		}
	}
	public static class RequestDeleteData extends RequestData{
		public RequestDeleteData(String url) {
			super(Utils.DELETE, url, null, null);
		}
	}
	public static class RequestUploadData extends RequestData{
		public RequestUploadData(String url, List<NameValuePair> parameters) {
			super(Utils.UPLOAD, url, parameters, null);
		}
	}
	public static class RequestDownloadData extends RequestData{
		public RequestDownloadData(String url) {
			super(Utils.DOWNLAOD, url, null, null);
		}
	}
	
	private int method;
	private String url;
	private List<NameValuePair> parameters;
	private String extras[];
	
	public RequestData(int method, String url,
			List<NameValuePair> parameters, String[] extras) {
		super();
		this.method = method;
		this.url = url;
		this.parameters = parameters;
		this.extras = extras;
	}
	public int getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public List<NameValuePair> getParameters() {
		return parameters;
	}
	public String[] getExtras() {
		return extras;
	}
	public void setMethod(int method) {
		this.method = method;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setParameters(List<NameValuePair> parameters) {
		this.parameters = parameters;
	}
	public void setExtras(String[] extras) {
		this.extras = extras;
	}
}
