package com.dml.recommend;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dml.adapter.MoreListAdapter;
import com.dml.adapter.recommendAdapter;
import com.dml.bean.VideoBean;
import com.dml.cluster.League;
import com.dml.home.MoreActivity;
import com.dml.internet.GetConnectionMore;
import com.dml.internet.GetConnectionUpdate;
import com.dml.souqiu.R;
import com.dml.tvPlay.video_show;
import com.dml.utils.TimeFormat;
import com.dml.widget.XListView;
import com.dml.widget.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class recommendActivity extends Activity  {
	int videotag=1;
	private final int DATA_EMPTY = 0; 
	private final int DATA_SUCCESS = 1;
	private List<VideoBean> data;
	private recommendAdapter adapter;
	private ListView listview;
	private View pb;
	private ImageView backImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recommend);
		listview = (ListView) findViewById(R.id.recommend);
		backImg = (ImageView) findViewById(R.id.more_back);
		pb = (View) findViewById(R.id.pb);
		pb.setVisibility(View.VISIBLE);
	        getData();
		
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub		
					//GetConnectionUpdate.updateClickNum(videotag, data.get(position ).getId());
					Intent intent = new Intent();
					intent.putExtra("videotag", String.valueOf(videotag));
					intent.putExtra("videoID", String.valueOf(data.get(position ).getId()));
					intent.putExtra("videoTitle", String.valueOf(data.get(position ).getTitle()));
					intent.putExtra("videoUrl", String.valueOf(data.get(position ).getVideoUrl()));
					intent.setClass(recommendActivity.this, video_show.class);
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
					adapter = new recommendAdapter(recommendActivity.this, data, listview);
			    	listview.setAdapter(adapter);
			    	pb.setVisibility(View.INVISIBLE);
				break;
			case DATA_EMPTY:
				   Toast.makeText(recommendActivity.this, "", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	private void getData() {
		// TODO Auto-generated method stub
		new Thread(){
	    	@Override    
	    	public void run(){
	    		data = getCon("A0000044257213", "kwb");
	    		if(data != null && !data.isEmpty()){
		    		 handler.sendEmptyMessage(DATA_SUCCESS);
	    		}else{
		    		 handler.sendEmptyMessage(DATA_EMPTY);
		    		 
	    		}
	    	}
	    }.start();
	}
	   public static List<VideoBean> getCon(String deviceid,String name){
	    	String path = "http://202.114.18.96:8080/Community/recommend";	
	    	JSONArray ja = null;
	    	List<VideoBean> data = new ArrayList<VideoBean>();	    	 	    
	    
	        try {
			    StringBuilder result=new StringBuilder();
				URL url=new URL(path);
				HttpURLConnection conn=(HttpURLConnection)url.openConnection();
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(5000);
				OutputStream out=conn.getOutputStream();
				DataOutputStream obj=new DataOutputStream(out);
				String params =  "deviceid="+ URLEncoder.encode(deviceid, "utf-8")
						+ "&username="+ URLEncoder.encode(name, "utf-8");
				
				obj.writeBytes(params);
				obj.flush();
				obj.close(); 
				InputStreamReader in=new InputStreamReader(conn.getInputStream(),"UTF-8");
				BufferedReader buff=new BufferedReader(in);
			    String line="";
			    while((line=buff.readLine())!=null){
					 result.append(line+"\n");
				 }
			     in.close();
				 conn.disconnect();
				 
				 ja =new JSONArray(result.toString());
				 System.out.println(deviceid+":"+name);
				 System.out.println("推荐消息"+ja);
			}catch(Exception e){
				e.printStackTrace();
			}	
	        
			for(int i = 0;i < ja.length(); i++){
				VideoBean video = new VideoBean();
				try{
					video.setId(ja.getJSONObject(i).getString("videoID"));
					video.setImageUrl(ja.getJSONObject(i).getString("imageUrl"));;
					video.setClickNum(ja.getJSONObject(i).getInt("clicknum"));
					video.setLength(TimeFormat.format(ja.getJSONObject(i).getInt("totalduration")));
					video.setOrigin(ja.getJSONObject(i).getString("videoorigin"));
					video.setVideoUrl(ja.getJSONObject(i).getString("videoUrl"));
					video.setTime(ja.getJSONObject(i).getString("videodateCome"));
					video.setTitle(ja.getJSONObject(i).getString("videoTitle"));    
				}catch(Exception e){
					e.printStackTrace();
				}
				data.add(video);
			}
			return data;
	    }
}