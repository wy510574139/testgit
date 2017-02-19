package wy.volley;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

		RequestQueue mQueue = Volley.newRequestQueue(this);

		StringRequest stringRequest = new StringRequest(
				"https://www.baidu.com", new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("wy", response);

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("wy", "111111111111" + error.toString());

					}

				});
		mQueue.add(stringRequest);
		mQueue.start();

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				"https://www.baidu.com", null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						Log.i("wy", "22222" + jsonObject.toString());

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("wy", "3333" + error.toString());

					}
				});
		mQueue.add(jsonObjectRequest);
		mQueue.start();
	}
}
