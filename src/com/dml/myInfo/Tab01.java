package com.dml.myInfo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.dml.adapter.VideoRecordAdapter;
import com.dml.bean.VideoBean;
import com.dml.souqiu.R;
import com.dml.tvPlay.video_show;
import com.dml.tvPlay.video_show2;
import com.dml.utils.MyDataBaseHelper;
import com.dml.utils.TimeFormat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Tab01 extends Activity{
	 private List<VideoBean> data = new ArrayList<VideoBean>();  
	    private ListView listItem;  
	   /* String videotag[]=null;
	    String imgurl[]=null;
	    String videotitle[]=null;
	    String videourl[]=null;
	    String videoid[]=null;
	    String playtime[]=null;*/
	    
	    private MyDataBaseHelper dbhelper;
	    @Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.tab1_);  
	        dbhelper=new MyDataBaseHelper(this, "play.db",  1);
	        listItem = (ListView) this.findViewById(R.id.list);  
	     
	        
	    } 
	    
	    private void getData() {  
	  
	    	Cursor cursor=dbhelper.getReadableDatabase().
	    		       rawQuery("select * from videoplay",null);  
			try {
      /*  int n=cursor.getCount();	
		  videotag=new String[n];
		  videoid=new String[n];
		  videourl=new String[n];
		  imgurl=new String[n];
		  videotitle=new String[n];
		  playtime=new String[n]; */	  
		  data.clear();
		  while(cursor.moveToNext()){
			  VideoBean video = new VideoBean();
			  String videotag_id=cursor.getString(1);
			  video.setVideotag(videotag_id.substring(0, 1));
			  video.setId(videotag_id.substring(1,videotag_id.length()));
			  video.setVideoUrl(cursor.getString(2));
			  video.setImageUrl(cursor.getString(3));
			  video.setTitle(cursor.getString(4));
			  video.setTime(cursor.getString(5));
			  data.add(video);
		}					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}       

	    }  
	    
	    @Override
	    public void onResume(){
	    	super.onResume();
	    	getData();  
	        listItem.setAdapter(new VideoRecordAdapter(Tab01.this, data));
	        listItem.setOnItemClickListener(new OnItemClickListener() {  
	      	  
	            @Override    
	            public void onItemClick(AdapterView<?> adapter, View view, int position,  
	                    long id) { 
	            	if(data.get(position).getVideotag().equals("2")){//��������򣬾����뵽������video_show2������	
	  	              Intent it=new Intent(getApplicationContext(),video_show2.class);
	  	              it.putExtra("videoID", ""+data.get(position).getId());//���ڵ���û��Э�̺ã�����ǿ����ת��Ϊ�ַ�
		          	  it.putExtra("videoTitle", data.get(position).getTitle());//������ƵԤ���У���Ϊ��ƵԤ����Ҳ�б��⡣
		              it.putExtra("videoUrl", data.get(position).getVideoUrl());
		              it.putExtra("videotag", data.get(position).getVideotag());
	            	  startActivity(it);      
	            	}
	            else{
	            	  Intent it=new Intent(getApplicationContext(),video_show.class);	
	            	  it.putExtra("videoID", ""+data.get(position).getId());//���ڵ���û��Э�̺ã�����ǿ����ת��Ϊ�ַ�
		          	  it.putExtra("videoTitle", data.get(position).getTitle());//������ƵԤ���У���Ϊ��ƵԤ����Ҳ�б��⡣
		              it.putExtra("videoUrl", data.get(position).getVideoUrl());
		              it.putExtra("videotag", data.get(position).getVideotag());
	            	  startActivity(it);      
	            	}
	            }				
	        });   
	    }
	}  
