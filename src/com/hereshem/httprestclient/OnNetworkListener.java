package com.hereshem.httprestclient;
public interface OnNetworkListener {
		public void onSuccessConnection(RequestData request, ResponseSuccessData response);	
		public void onErrorConnection(RequestData request, ResponseErrorData response, int status);	
}
