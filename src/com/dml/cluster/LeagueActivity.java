package com.dml.cluster;



import com.dml.souqiu.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class LeagueActivity extends TabActivity implements OnTouchListener, OnGestureListener {
	private static final int FLING_MIN_DISTANCE = 20;
	private static final int FLING_MIN_VELOCITY = 0;
	TabHost tabHost;
	GestureDetector mGestureDetector;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.league);
		
		mGestureDetector = new GestureDetector(this);
		LinearLayout ll=(LinearLayout)findViewById(R.id.linew);
		ll.setOnTouchListener(this);
		ll.setLongClickable(true);
		tabHost = getTabHost();
		tabHost.setup();
		Intent intent0 = new Intent(this, ZhongChao.class);
		createTab("中超", intent0);
		Intent intent1 = new Intent(this, OuGuan.class);
		createTab("欧冠", intent1);

		Intent intent2 = new Intent(this, XiJia.class);
		createTab("西甲", intent2);

		Intent intent3 = new Intent(this, YingChao.class);
		createTab("英超", intent3);

		Intent intent4 = new Intent(this, YiJia.class);
		createTab("意甲", intent4);

		Intent intent5 = new Intent(this, DeJia.class);
		createTab("德甲", intent5);
		Intent intent6 = new Intent(this, FaJia.class);
		createTab("法甲", intent6);
		Intent intent7 = new Intent(this, GuoJiaDui.class);
		createTab("国家队", intent7);
	    Intent intent8 = new Intent(this, PianKu.class);
     	createTab("片库", intent8);
		TabWidget tabWidget = tabHost.getTabWidget();
		int count = tabWidget.getChildCount();

//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
////
//		// 窗口的宽度
//		int screenWidth = dm.widthPixels;
//		Log.i("test", "screenWidth=" + screenWidth);
//		if (count > 4) {
//			for (int i = 0; i < count; i++) {
//				tabWidget.getChildTabViewAt(i).setMinimumWidth(screenWidth / 3);
//			}
//		} 
//		for (int i =0; i < tabWidget.getChildCount(); i++) {  
//		            tabWidget.getChildAt(i).getLayoutParams().height = 100;  
//		            tabWidget.getChildAt(i).getLayoutParams().width = 100;
//		}
		tabHost.setCurrentTab(0);
		
	
		
	}

	private void createTab(String text, Intent intent) {
		tabHost.addTab(tabHost.newTabSpec(text).setIndicator(
				createTabView(text)).setContent(intent));
	}

	private View createTabView(String text) {
		View view = LayoutInflater.from(this).inflate(R.layout.tab_indicator,
				null);
		TextView tv = (TextView) view.findViewById(R.id.tv_tab);
		tv.setText(text);
		return view;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Log.i("touch","touch");
		 return mGestureDetector.onTouchEvent(event); 
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int total=tabHost.getTabWidget().getChildCount();
		int current=tabHost.getCurrentTab();
		// TODO Auto-generated method stub
		 if (e1.getX()-e2.getX() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
	            // Fling left 
	            Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show(); 
	            
	            tabHost.setCurrentTab(current-1<0?0:current-1);
	        } else if (e2.getX()-e1.getX() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
	            // Fling right 
	        	 tabHost.setCurrentTab(current+1>total?total-1:current+1);
	            Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show(); 
	        } 
	        return false; 
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		 Toast.makeText(this, "滑动", Toast.LENGTH_SHORT).show(); 
   
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}