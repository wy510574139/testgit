package cn.wy.qqlogin;

import org.json.JSONObject;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button btLogin;
	private Button btGetinfo;
	private Button btExit;

	private TextView tvInfo;

	String APP_ID = "1105014617";
	private Tencent tencent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();

		setListeners();
	}

	private void setListeners() {
		btLogin.setOnClickListener(this);
		btGetinfo.setOnClickListener(this);
		btExit.setOnClickListener(this);
	}

	private void initViews() {
		btLogin = (Button) findViewById(R.id.bt_login);
		btGetinfo = (Button) findViewById(R.id.bt_getinfo);
		btExit = (Button) findViewById(R.id.bt_exit);
		tvInfo = (TextView) findViewById(R.id.tv_info);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login:
			login();
			break;
		case R.id.bt_getinfo:
			getinfo();
			break;
		case R.id.bt_exit:
			exit();
			break;
		}

	}

	private void login() {
		tencent = Tencent.createInstance(APP_ID, getApplicationContext());
		if (!tencent.isSessionValid()) {
			tencent.login(this, "all", new IUiListener() {
				
				@Override
				public void onError(UiError arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(Object arg0) {
					try {
					
					JSONObject jo=(JSONObject) arg0;
					int ret=jo.getInt("ret");
					if (ret==0) {
						Toast.makeText(MainActivity.this, "µÇÂ¼³É¹¦", Toast.LENGTH_SHORT).show();
						
						String openId=jo.getString("openid");
						String accessToken=jo.getString("access_token");
						String expires=jo.getString("expires_in");
						
						tencent.setOpenId(openId);
						tencent.setAccessToken(accessToken, expires);
					}
					
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}
			});
		}

	}

	private void getinfo() {
		UserInfo userInfo=new UserInfo(this, tencent.getQQToken());

		userInfo.getUserInfo(new IUiListener() {
			
			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Object arg0) {
				try {
					tvInfo.setVisibility(View.VISIBLE);
					tvInfo.setText(String.valueOf(arg0));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void exit() {
		tencent.logout(this);

	}

}
