package com.dml.home;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dml.adapter.MoreListAdapter;
import com.dml.adapter.SearchResultAdapter;
import com.dml.bean.VideoBean;
import com.dml.internet.GetConnectionMore;
import com.dml.internet.GetConnectionSearch;
import com.dml.internet.GetConnectionUpdate;
import com.dml.search.SearchResultActivity;
import com.dml.souqiu.R;
import com.dml.tvPlay.video_show;
import com.dml.widget.XListView;
import com.dml.widget.XListView.IXListViewListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MoreActivity extends Activity implements IXListViewListener {

	private int videotag;
	private View pb;
	private XListView listView;
	private List<VideoBean> data;
	private List<VideoBean> data2;
	private MoreListAdapter adapter;
	private int start  = 0;
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
		
		Intent intent = this.getIntent();
		videotag = intent.getIntExtra("videotag", 0);
		System.out.println("moreactivity videotag:"+videotag);
		backImg = (ImageView) findViewById(R.id.more_back);
		title = (TextView) findViewById(R.id.morelist_title);
		setText();
		
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
					intent.setClass(MoreActivity.this, video_show.class);
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
	
	  protected void setText(){
		  String str = "";
		  switch(videotag){
		  case 1:
			  str = "足球";
			  break;
		  case 2:
		      str = "篮球";
			  break;
		  case 3:
		      str = "网球";
			  break;
		  case 4:
			  str = "乒乓球";
			  break;
		  case 5:
		   	  str = "羽毛球";
			  break;
		  case 6:
			  str = "排球";
			  break;
		  case 7:
			  str = "台球";
			  break;
		  default:
			  break;
		}
		title.setText(str);
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case DATA_SUCCESS:
				if(start == 0){    //进入时第一次加载数据
					adapter = new MoreListAdapter(MoreActivity.this, data, listView);
					System.out.println("adpter:" + adapter.getCount());
			    	listView.setAdapter(adapter);
			    	pb.setVisibility(View.INVISIBLE);
			    	onLoad();
				}else{
					//新数据加在旧数据后面
					data2.addAll(data);      
					data = data2;
					System.out.println("data after:" + adapter.getCount());
					adapter.notifyDataSetChanged();  //直接更新适配器，不用重新set
					System.out.println("adpter:" + adapter.getCount());
			    	onLoad();
				}
				break;
			case DATA_EMPTY:
				data = data2;
				if(flag != 0){
				   adapter.notifyDataSetChanged();
				   onLoad();
				   Toast.makeText(MoreActivity.this, "数据已全部加载完毕", Toast.LENGTH_SHORT).show();
				}else{
				   Toast.makeText(MoreActivity.this, "结果为空", Toast.LENGTH_SHORT).show();
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
		Log.i(TAG, "刷新最新");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start  = 0;
				if(data != null)
				     data.clear();
				getData();
			}
		}, 2000);
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		Log.i(TAG, "加载更多");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start += 9;
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
