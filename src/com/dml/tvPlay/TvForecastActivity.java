package com.dml.tvPlay;

import org.json.JSONObject;

import com.dml.adapter.TvForecastAdapter;
import com.dml.internet.GetConnectionTvForecast;
import com.dml.souqiu.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TvForecastActivity extends Activity{
	
	private String[] group = new String[]{"CCTV-5", "风云足球", "欧洲足球"};
	private JSONObject data = null;
	//private ExpandableListView expandableListView;
	private LayoutInflater inflater = null;
	private FrameLayout flayout;
	private View pb;
	private ImageView backImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title);
		
		inflater = LayoutInflater.from(this);
	    flayout = (FrameLayout) findViewById(R.id.flayout);
		
		pb = (View) findViewById(R.id.pb);
		
		backImg = (ImageView) findViewById(R.id.tvforecast_back);
		backImg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	    new TvThread().start();
		
	//	expandableListView = (ExpandableListView) findViewById(R.id.expand);
	//	expandableListView.setGroupIndicator(null);
	}
	
	

	class TvThread extends Thread{
		@Override
		public void run(){
			pb.setVisibility(View.VISIBLE);
			data = GetConnectionTvForecast.getCon();
			if(data != null){
				handler.sendEmptyMessage(0x123);
			}else{
				handler.sendEmptyMessage(0x234);
			}
		}
	}
		
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == 0x123){
				pb.setVisibility(View.INVISIBLE);
				
				//ֱ�Ӽ��أ�����
				ExpandableListView expandableListView = new ExpandableListView(TvForecastActivity.this);
				expandableListView.setGroupIndicator(null);
				expandableListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
				expandableListView.setVerticalScrollBarEnabled(false);
				flayout.addView(expandableListView);
				
				//�ò��ּ��أ����
				//LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.expandlistview, null).findViewById(R.id.layout1);
				//ExpandableListView expandableListView = (ExpandableListView) layout.getChildAt(0);
			    expandableListView.setAdapter(new TvForecastAdapter(TvForecastActivity.this, data, expandableListView));
			    //flayout.removeAllViews();
			    //flayout.addView(layout);
			}else if(msg.what == 0x234){
				pb.setVisibility(View.INVISIBLE);
				RelativeLayout flayout2 = (RelativeLayout) inflater.inflate(R.layout.fail, null).findViewById(R.id.flayout2);
				flayout.addView(flayout2);
				flayout.setOnClickListener(new OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					//	  new TvThread().start();
						//flayout.removeAllViews();
						//pb.setVisibility(View.INVISIBLE);
						//handler.sendEmptyMessage(0x124);
					//	Intent intent = new Intent();
						//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//ˢ��
						//startActivity(intent);
					//	onCreate(null);
						startActivity(new Intent(TvForecastActivity.this, TvForecastActivity.class));
						finish();
			
					}
		
				});
			}else if(msg.what == 0x124){
				//pb.setVisibility(View.VISIBLE);
				new TvThread().start();
			}
		}
	};

}
