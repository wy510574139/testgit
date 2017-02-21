package cn.wy.baiduwhere;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Toast mToast;
	private BMapManager mBMapManager;
	private MapView mMapView;
	private MapController mMapController;

	private LocationClient mLocationClient;
	private LocationData mLocationData;

	private LocationOverlay myLocationOverlay = null;

	private boolean isRequest = false;
	private boolean isFirstloc = true;

	private PopupOverlay mPopupOverlay = null;// 弹出泡泡图层，浏览节点时使用
	private View viewCache;
	private BDLocation location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mBMapManager = new BMapManager(this);

		mBMapManager.init("3fWXsGOfVbQOWiczlBtVgqI36zCRseX5",
				new MKGeneralListener() {

					@Override
					public void onGetPermissionState(int iError) {
						if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
							showToast("API HEY错误！请检查！");
						}
					}

					@Override
					public void onGetNetworkState(int iError) {
						if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
							showToast("您的网络出错啦！");
						}
					}
				});
		setContentView(R.layout.activity_main);

		findViewById(R.id.request).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestLocation();
			}
		});

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		mMapController.enableClick(true);
		mMapController.setZoom(14);
		mMapView.setBuiltInZoomControls(true);

		viewCache = LayoutInflater.from(this)
				.inflate(R.layout.pop_layout, null);
		mPopupOverlay = new PopupOverlay(mMapView, new PopupClickListener() {

			@Override
			public void onClickedPopup(int arg0) {
				mPopupOverlay.hidePop();
			}
		});

		mLocationData = new LocationData();

		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceivePoi(BDLocation arg0) {

			}

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null) {
					return;
				}
				MainActivity.this.location = location;
				mLocationData.latitude = location.getLatitude();
				mLocationData.longitude = location.getLongitude();

				mLocationData.accuracy = location.getRadius();
				mLocationData.direction = location.getDerect();

				myLocationOverlay.setData(mLocationData);
				mMapView.refresh();

				if (isFirstloc || isRequest) {
					mMapController.animateTo(new GeoPoint((int) (location
							.getLatitude() * 1e6), (int) (location
							.getLongitude() * 1e6)));

					showPopupOverlay(location);

					isRequest = false;
				}

				isFirstloc = false;
			}
		});

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPRS
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
		option.disableCache(false);// 禁止启用缓存定位

		mLocationClient.setLocOption(option);
		mLocationClient.start(); // 调用此方法开始定位

		// 定位图层初始化
		myLocationOverlay = new LocationOverlay(mMapView);
		// 设置定位数据
		myLocationOverlay.setData(mLocationData);

		myLocationOverlay.setMarker(getResources().getDrawable(
				R.drawable.location_arrows));

		// 添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();

		// 修改定位数据后刷新图层生效
		mMapView.refresh();
	}

	private void showPopupOverlay(BDLocation location) {
		TextView popText = ((TextView) viewCache
				.findViewById(R.id.location_tips));
		popText.setText("[我的位置]\n" + location.getAddrStr());
		mPopupOverlay.showPopup(getBitmapFromView(popText),
				new GeoPoint((int) (location.getLatitude() * 1e6),
						(int) (location.getLongitude() * 1e6)), 10);
	}

	public static Bitmap getBitmapFromView(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	protected void requestLocation() {
		isRequest = true;
		if (mLocationClient != null && mLocationClient.isStarted()) {
			showToast("正在定位......");
			mLocationClient.requestLocation();
		} else {
			Log.d("wy", "locClient is null or not started");
		}

	}

	private class LocationOverlay extends MyLocationOverlay {

		public LocationOverlay(MapView arg0) {
			super(arg0);
		}

		@Override
		protected boolean dispatchTap() {
			return super.dispatchTap();
		}

		@Override
		public void setMarker(Drawable arg0) {
			super.setMarker(arg0);
		}
	}

	private void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
}
