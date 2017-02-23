package cn.wy.baiduwhere;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

	/**
	 * 地图主控件
	 */
	private MapView mMapView;
	/**
	 * 完成地图控制
	 */
	private MapController mMapController;
	/**
	 * 处理回调事件
	 */
	private MKMapViewListener mMKMapViewListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DemoApplication app = (DemoApplication) getApplicationContext();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(app.strKey,
					new DemoApplication.MyGeneralListener());
		}
		setContentView(R.layout.activity_main);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		/**
		 * 设置地图是否响应点击事件 .
		 */
		mMapController.enableClick(true);
		mMapController.setZoom(12);
		/**
		 * 将地图移动至指定点
		 * 使用百度经纬度坐标，可以通过http://api.map.baidu.com/lbsapi/getpoint/index
		 * .html查询地理坐标 如果需要在百度地图上显示使用其他坐标系统的位置，请发邮件至mapapi@baidu.com申请坐标转换接口
		 */
		double cLat = 30.67;
		double cLon = 104.10;
		GeoPoint p = new GeoPoint((int) (cLat * 1E6), (int) (cLon * 1E6));
		mMapController.setCenter(p);
		mMKMapViewListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {
				/**
				 * 在此处理地图移动完成回调 缩放，平移等操作完成后，此回调被触发
				 */

			}

			@Override
			public void onMapLoadFinish() {
				Toast.makeText(MainActivity.this, "地图加载完毕!", Toast.LENGTH_SHORT)
						.show();

			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 * 地图完成带动画的操作（如: animationTo()）后，此回调被触发
				 */

			}

			@Override
			public void onGetCurrentMap(Bitmap arg0) {
				/**
				 * 当调用过 mMapView.getCurrentMap()后，此回调会被触发 可在此保存截图至存储设备
				 */

			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件 显示底图poi名称并移动至该点 设置过：
				 * mMapController.enableClick(true)，此回调才能被触发
				 */
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT)
							.show();
					mMapController.animateTo(mapPoiInfo.geoPt);
				}

			}
		};
		mMapView.regMapViewListener(DemoApplication.getInstance().mBMapManager,
				mMKMapViewListener);
	}

	@Override
	protected void onPause() {
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		/**
		 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		 */
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}
}
