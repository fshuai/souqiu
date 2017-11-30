package com.dml.cluster;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.dml.adapter.MoreListAdapter;
import com.dml.bean.VideoBean;
import com.dml.internet.GetConnectionLeague;
import com.dml.internet.GetConnectionSearch;
import com.dml.internet.GetConnectionUpdate;
import com.dml.souqiu.R;
import com.dml.tvPlay.video_show;
import com.dml.widget.XListView;
import com.dml.widget.XListView.IXListViewListener;

public class SimilarActivity extends Activity  implements IXListViewListener{
	private int Query=1;//足球的查询
	private View pb;
	private XListView listView;
	private List<VideoBean> data;
	private List<VideoBean> data2;
	private MoreListAdapter adapter;
	private int start  = 1;
	private int flag;
	private final String TAG = "XListViewActivity";
	private final int DATA_EMPTY = 0; 
	private final int DATA_SUCCESS = 1;
	private String id;
	private String title;
	private String origintitle;
	private int pageid=0;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.f2);
		Intent it=getIntent();
		id=it.getStringExtra("id");
		origintitle=it.getStringExtra("title");
		pb = (View) findViewById(R.id.pb);
		pb.setVisibility(View.VISIBLE);
		listView = (XListView) findViewById(R.id.similar_list);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		getInfo();
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub		
				//	GetConnectionUpdate.updateClickNum(1, data.get(position - 1).getId());
					Intent intent = new Intent();
					intent.putExtra("videotag", "1");
					intent.putExtra("videoID", String.valueOf(data.get(position - 1).getId()));
					intent.putExtra("videoTitle", String.valueOf(data.get(position - 1).getTitle()));
					intent.putExtra("videoUrl", String.valueOf(data.get(position - 1).getVideoUrl()));
					intent.setClass(getApplicationContext(), video_show.class);
					startActivity(intent);
			}			
		 });	
       }
	
	 
	
	private void getInfo() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				 title="";
				try{
					URL url=new URL("http://202.114.18.96:8080/SouQiuWang/playInfo");
					HttpURLConnection conn=(HttpURLConnection)url.openConnection();
					 conn.setDoOutput(true);
					 conn.setDoInput(true);
					 conn.setRequestMethod("POST");  
					 OutputStream out=conn.getOutputStream();
					 DataOutputStream obj=new DataOutputStream(out);	
					 //mod=1相当获取关键字
		  			 String params="playid="+URLEncoder.encode(id,"utf-8")+"&mod="+URLEncoder.encode("0","utf-8");;
				     obj.writeBytes(params); 
		  			 obj.flush();
		  			 obj.close();					
					 InputStreamReader in=new InputStreamReader(conn.getInputStream());  
					 BufferedReader buff=new BufferedReader(in);
					 String line="";
					 while((line=buff.readLine())!=null){
						 title+=line+"\n";
					 }
			   System.out.println("分词结果:"+title);
					 if(title==null||title.equals(""))//如果分词没有结果
						 title=origintitle;
				        in.close(); 
					 conn.disconnect(); 
					 getData();//放到这里来
			 
			}catch(Exception e){
				e.printStackTrace(); 
			} 
			
		} 
	  }.start(); 
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
				pageid++;
				getData();
			
			}
		}, 2000);
	}
	
	public void getData(){
		new Thread(){
	    	@Override    
	    	public void run(){
	    		data2 = data;
	    		String m_keyword=URLEncoder.encode(title);//如果有空格，就会出现错误
					data = GetConnectionSearch.getCon(1,pageid,m_keyword , 1,0,0 );
				
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





