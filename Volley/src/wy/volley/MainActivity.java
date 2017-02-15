package wy.volley;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		RequestQueue mQueue=Volley.newRequestQueue(this);
		
		StringRequest stringRequest=new StringRequest("https://www.baidu.com", 
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("wy", response);
						
					}
				}, new Response.ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("wy", "111111111111"+error.toString());
						
					}
					
				});
		mQueue.add(stringRequest);
	}
}