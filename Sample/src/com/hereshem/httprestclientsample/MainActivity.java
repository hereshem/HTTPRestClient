package com.hereshem.httprestclientsample;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.hereshem.httprestclient.HTTPRestCient;
import com.hereshem.httprestclient.OnNetworkListener;
import com.hereshem.httprestclient.RequestData;
import com.hereshem.httprestclient.RequestData.*;
import com.hereshem.httprestclient.ResponseErrorData;
import com.hereshem.httprestclient.ResponseSuccessData;

public class MainActivity extends Activity implements OnNetworkListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//  Method 1 : only request without callback
		HTTPRestCient client = new HTTPRestCient(this);
		client.processRequest(new RequestGetData("http://fb.com/"));

		// Method 2
		HTTPRestCient cl2 = new HTTPRestCient(this, this);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("name", "Hem"));
		parameters.add(new BasicNameValuePair("email", "hereshem"));
		cl2.processRequest(new RequestPostData("http://some.thing/link/", parameters));
		
		//Method 3 
		HTTPRestCient  cl3 = new HTTPRestCient(getApplicationContext(), new OnNetworkListener() {
			@Override
			public void onSuccessConnection(RequestData request,
					ResponseSuccessData response) {
				toast("Response is = " + response.getResult());
			}
			
			public void onErrorConnection(RequestData request,
					ResponseErrorData response, int status) {
				toast("Error :: Status = " + status + ",  response = " + response.getResult() );
			}
		});
		
		cl3.processRequest(new RequestDeleteData("http://delete.link"));
		
	}
	
	void toast(String string){
		Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSuccessConnection(RequestData request,
			ResponseSuccessData response) {
		
	}

	@Override
	public void onErrorConnection(RequestData request,
			ResponseErrorData response, int errorCode) {
		
	}

}
