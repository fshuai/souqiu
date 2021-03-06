package com.dml.cluster;

import java.util.Date;
import java.util.List;

import com.dml.adapter.MoreListAdapter;
import com.dml.bean.VideoBean;
import com.dml.home.MoreActivity;
import com.dml.internet.GetConnectionMore;
import com.dml.internet.GetConnectionUpdate;
import com.dml.souqiu.R;
import com.dml.tvPlay.video_show;
import com.dml.widget.XListView;
import com.dml.widget.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PianKu extends Activity implements IXListViewListener {

	private int videotag=1;
	private View pb;
	private XListView listView;
	private List<VideoBean> data;
	private List<VideoBean> data2;
	private MoreListAdapter adapter;
	private int start  = 1;
	private int flag;
	private ImageView backImg;
	private TextView title;
	
	private final String TAG = "XListViewActivity";
	private final int DATA_EMPTY = 0; 
	private final int DATA_SUCCESS = 1;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more_activity);
		backImg = (ImageView) findViewById(R.id.more_back);
		title = (TextView) findViewById(R.id.morelist_title);
		title.setText("足球");	
		pb = (View) findViewById(R.id.pb);
		pb.setVisibility(View.VISIBLE);
		listView = (XListView) findViewById(R.id.more_list);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		
		getData();
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub		
					//GetConnectionUpdate.updateClickNum(videotag, data.get(position - 1).getId());
					Intent intent = new Intent();
					intent.putExtra("videotag", String.valueOf(videotag));
					intent.putExtra("videoID", String.valueOf(data.get(position - 1).getId()));
					intent.putExtra("videoTitle", String.valueOf(data.get(position - 1).getTitle()));
					intent.putExtra("videoUrl", String.valueOf(data.get(position - 1).getVideoUrl()));
					intent.setClass(getApplicationContext(), video_show.class);
					startActivity(intent);
			}			
		 });
		
	   backImg.setOnClickListener(new OnClickListener(){

		   @Override
		   public void onClick(View v) {
			   // TODO Auto-generated method stub
			   finish();
		  }
			
	      });
       }
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case DATA_SUCCESS:
				if(start == 1){    //����ʱ��һ�μ������
					adapter = new MoreListAdapter(getApplicationContext(), data, listView);
					System.out.println("adpter:" + adapter.getCount());
			    	listView.setAdapter(adapter);
			    	pb.setVisibility(View.INVISIBLE);
			    	onLoad();
				}else{
					//����ݼ��ھ���ݺ���
					data2.addAll(data);      
					data = data2;
					System.out.println("data after:" + adapter.getCount());
					adapter.notifyDataSetChanged();  //ֱ�Ӹ�������������������set
					System.out.println("adpter:" + adapter.getCount());
			    	onLoad();
				}
				break;
			case DATA_EMPTY:
				data = data2;
				if(flag != 0){
				   adapter.notifyDataSetChanged();
				   onLoad();
				   Toast.makeText(getApplicationContext(), "�����ȫ���������", Toast.LENGTH_SHORT).show();
				}else{
				   Toast.makeText(getApplicationContext(), "���Ϊ��", Toast.LENGTH_SHORT).show();
				   pb.setVisibility(View.INVISIBLE);
				}
				break;
			}
		}
	};
	
	
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		
		String time = Long.toString(new Date().getTime());
        Long timeLong = Long.valueOf(time);
        listView.setRefreshTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(new java.util.Date (timeLong)));

    }    
	
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		Log.i(TAG, "ˢ������");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start  = 1;
				if(data != null)
				     data.clear();
				getData();
			}
		}, 2000);
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		Log.i(TAG, "���ظ��");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start += 8;
				getData();
			
			}
		}, 2000);
	}
	
	public void getData(){
		new Thread(){
	    	@Override    
	    	public void run(){
	    		data2 = data;
	    		data = GetConnectionMore.getCon(videotag, start);
	    		if(data != null && !data.isEmpty()){
	    			 flag = 1;
	    			 System.out.println("data before:" + data.size());
		    		 handler.sendEmptyMessage(DATA_SUCCESS);
	    		}else{
		    		 handler.sendEmptyMessage(DATA_EMPTY);
	    		}
	    	}
	    }.start();
	}

}
