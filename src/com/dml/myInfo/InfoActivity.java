package com.dml.myInfo;

import com.dml.myInfo.Login.MyASyncTask;
import com.dml.myInfo.Personal.MyASyncTask2;
import com.dml.souqiu.R;  //不要掉了，否则报错
import com.dml.souqiu.wxapi.WXEntryActivity;
import com.dml.utils.MyDataBaseHelper;

import android.app.Activity;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class InfoActivity extends ActivityGroup {
	private TabHost tabHost;		//声明TabHost组件的对象
    private TextView login;
    int status=0;
	private ListView listview;
	private ListView listview2;
	private boolean flag=false;//用于判断登录与否，从而进入不同的界面
	private LinearLayout layout;
	private ListView mylistview;
	private LayoutInflater m_inflater;
	private SimpleAdapter m_adapter;
	private View foot;
	private List<Map<String,Object>> m_data;
	boolean finish_load=true;
	private int maxPage;
	private int totalnum;
	private String[]m_videotitle;
	private String[]m_time;
	private String[]m_content;
	private String myname="";
	private int Login_state=0;
	private MyComment mc;
	private TextView textview;
	private ImageView settingImg;
	private ImageView userimage;
	private MyDataBaseHelper dbhelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_activity);
		 dbhelper=new MyDataBaseHelper(this, "play.db",  1);
	
		layout=(LinearLayout)findViewById(R.id.layout);
		settingImg = (ImageView) findViewById(R.id.setting);
		userimage=(ImageView) findViewById(R.id.userimg);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);  
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();  
        if(networkInfo == null || !networkInfo.isAvailable())  
        {     
        	//当前无可用网络  
        	layout.setVisibility(View.INVISIBLE); 
        	Toast.makeText(InfoActivity.this, "请检测您的网络设置", Toast.LENGTH_SHORT).show();
        	return;
        }   
        else   
        {    //当前有可用网络  
        	layout.setVisibility(View.VISIBLE);
        	 
        	m_data= new ArrayList<Map<String,Object>>();
        	tabHost=(TabHost)findViewById(android.R.id.tabhost);	//获取TabHost对象
    		tabHost.setup();	//初始化TabHost组件 
    		tabHost.setup(this.getLocalActivityManager()); 
    		login=(TextView)findViewById(R.id.textview);
    		SharedPreferences sp = getSharedPreferences("USERPASS",InfoActivity.MODE_PRIVATE);   
    		  Login_state=sp.getInt("Login_state", 0);
		      myname=sp.getString("Login_name", "");
		    LayoutInflater inflater = LayoutInflater.from(this); 	// 声明并实例化一个LayoutInflater对象  
            inflater.inflate(R.layout.tab1_, tabHost.getTabContentView());  
            inflater.inflate(R.layout.tab2, tabHost.getTabContentView());
            inflater.inflate(R.layout.tab3, tabHost.getTabContentView());
        	Intent it=new Intent();
             it.setClass(InfoActivity.this, Tab01.class);
             Intent it2=new Intent();
             it2.setClass(InfoActivity.this, Tab02.class);
            tabHost.addTab(tabHost.newTabSpec("tab1") 
            		.setIndicator("播放记录")  
             //注意这里可以用setContent可以跳转到另外一个activity中       
                     .setContent(it));
            tabHost.addTab(tabHost.newTabSpec("tab2")
            		.setIndicator("我的收藏")
            		.setContent(it2));  	//添加第二个标签页
            tabHost.addTab(tabHost.newTabSpec("tab3")
            		.setIndicator("我的评论")
            		.setContent(R.id.FrameLayout3));  	//添加第三个标签页
            textview=(TextView)findViewById(R.id.TextView2);
            
            TabWidget tabWidget = tabHost.getTabWidget();
            int count = tabWidget.getChildCount();
            for(int i = 0;i<count;i++){
             TextView tv = (TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
                //tv.setTextSize(15);
             tv.setGravity(Gravity.CENTER_VERTICAL);//居中
             }
            m_inflater=getLayoutInflater();
     		foot=(View) m_inflater.inflate(R.layout.comment_foot, null);///////这里由于是一个布局，所以写成view的形式
            mylistview=(ListView)findViewById(R.id.mycomment);
            mylistview.setOnScrollListener(new ScrollListener());
            if(Login_state==1){//如果上次没有退出
 	    	  flag=true;
 	    	  login.setText(myname);
 	    	   MyASyncTask yncTask = new MyASyncTask();
 	    	   yncTask.execute(0);
 	    	   textview.setText("您共有"+totalnum+"条评论");
 	    	 String fileName = "/sdcard/myImage/"+myname+".jpg"; //照片保存的地址  		     
		        if(new File(fileName).exists()==false){		
		        	MyASyncTask2 my=new MyASyncTask2();
		        	my.execute("http://202.114.18.96:8888/images/PersonalImages/"+myname+".jpg");
	
		        }   
		        else
				    userimage.setImageBitmap(BitmapFactory.decodeFile(fileName));		
 	    	 
 	       }else{
 	    	   flag=false;
 	    	   textview.setText("请登录后查看评论与回复");
 	    	   login.setText("点击登录");
 	    	    userimage.setImageResource(R.drawable.user);
 	       }       
        }      
		
		 settingImg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(InfoActivity.this, SettingActivity.class));
			}
        	
        }); 
		
        layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
		public void onClick(View v) {
	
			if(flag==false){
				Intent it=new Intent();
				it.setClass(InfoActivity.this, Login.class);
				startActivityForResult(it, 0);
				//把上面的帐号登录改为微信登录
				/*Intent it=new Intent();
				it.setClass(InfoActivity.this, WXEntryActivity.class);
				startActivity(it); */
				
				}
			 else{
				    Intent it=new Intent();	
					it.setClass(InfoActivity.this, Personal.class);//进入个性化界面
					SharedPreferences sp = getSharedPreferences("USERPASS",InfoActivity.MODE_PRIVATE);   					   		    		
				     myname=sp.getString("Login_name", "");	
					it.putExtra("name", myname);
					startActivityForResult(it, 1); 
			 }
			 
			}
		});
      
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent it) {
		if(requestCode==0&&resultCode==0){
		if(it!=null){//注意一定要加判断，不然会出现异常
			Bundle data=it.getExtras();
			status=data.getInt("status");
			if(status==1){
				SharedPreferences sp = getSharedPreferences("USERPASS",InfoActivity.MODE_PRIVATE);   				       
			    myname=sp.getString("Login_name", "");	//把名字读出来赋值给全局变量	  
				login.setText(myname);
				flag=true;
		        m_data.clear();
		        MyASyncTask yncTask = new MyASyncTask();
	 	    	yncTask.execute(0);  
	 	        String fileName = "/sdcard/myImage/"+myname+".jpg"; //照片保存的地址  		     
			    if(new File(fileName).exists()==false){		
			       MyASyncTask2 my=new MyASyncTask2();
			       my.execute("http://202.114.18.96:8888/images/PersonalImages/"+myname+".jpg");
			        }   
			       else
					 userimage.setImageBitmap(BitmapFactory.decodeFile(fileName));
			}
		}
		}
		else if(requestCode==1&&resultCode==1){
			if(it!=null){//注意一定要加判断，不然会出现异常	
				Bundle data=it.getExtras();
				 int result=data.getInt("result");                   //这里修改了
				if(result==1){
					userimage.setImageResource(R.drawable.user);
					login.setText("点击登录");
					flag=false;
					m_data.clear();
					m_adapter.notifyDataSetChanged();
		 	    	textview.setText("请登录后查看评论与回复"); 
				
			   }
			  else if(result==0){
				  String fileName = "/sdcard/myImage/"+myname+".jpg"; //照片保存的地址  		     
			        if(new File(fileName).exists()==false){		
			        	MyASyncTask2 my=new MyASyncTask2();
			        	my.execute("http://202.114.18.96:8888/images/PersonalImages/"+myname+".jpg");
		
			        }   
			        else
					    userimage.setImageBitmap(BitmapFactory.decodeFile(fileName));
				  
				  
			  }
	
			}
		}
	
	}
	public   List<Map<String,Object>> getCommentData(int startposition, int pagesize) {			   
	    System.out.println("开始："+startposition);			 
	    mc=new MyComment(myname,startposition);//用于从数据库中读入视频评论消息
		mc.getMyCommentMessage();
		m_videotitle=mc.m_videotitle;
		m_content=mc.m_content;
		m_time=mc.m_time;    
		totalnum=mc.m_total;//评论总数  
		maxPage= totalnum/pagesize+1;// 最多页数
ArrayList<Map<String,Object>> c_list= new ArrayList<Map<String,Object>>();	
	    for(int i =0;i <pagesize&&i<m_content.length; i++) { 
	    //注意这里要有后面的条件，不然如果没有满页，会出现越界错误 
	        Map<String,Object> item = new HashMap<String,Object>();  	      
	        item.put("videoname", "视频标题："+m_videotitle[i]);  
	        item.put("text", "评论内容："+m_content[i]);  
	        item.put("time","评论时间："+m_time[i] );
	        c_list.add(item);   
	    }
	        return c_list;
	    }
	private Bitmap getImage(String path) {
		// TODO Auto-generated method stub
	       Bitmap bm =null;
		try {
			URL aURL = new URL(path);
			URLConnection con = aURL.openConnection();
			con.connect();
			InputStream is = con.getInputStream();
			/* 建立缓冲区是一个良好的编程习惯. */
			BufferedInputStream bis = new BufferedInputStream(is);
			/* 解析网络上的图片 */
	        bm = BitmapFactory.decodeStream(bis);		
			is.close();
			/* 这时图片已经被加载到ImageView中. */
			} catch (IOException e) {
			e.printStackTrace();
			} 
			return bm;
	          }
	public class MyASyncTask extends AsyncTask<Integer, Integer,  ArrayList<Map<String,Object>>> {
	      protected  ArrayList<Map<String,Object>> doInBackground(Integer... param) {
	    	  mc=new MyComment(myname,param[0]);//用于从数据库中读入视频评论消息
	  		mc.getMyCommentMessage();
	  		m_videotitle=mc.m_videotitle;
	  		m_content=mc.m_content;
	  		m_time=mc.m_time;
	  		totalnum=mc.m_total;//评论总数
	  		maxPage= totalnum/4+1;// 最多页数
	  ArrayList<Map<String,Object>> c_list= new ArrayList<Map<String,Object>>();	
	  if(m_content==null)
		  return c_list;
	  	    for(int i =0;i <4&&i<m_content.length; i++) { 
	  	    //注意这里要有后面的条件，不然如果没有满页，会出现越界错误 
	  	        Map<String,Object> item = new HashMap<String,Object>();  	      
	  	        item.put("videoname", "视频标题："+m_videotitle[i]);  
	  	        item.put("text", "评论内容："+m_content[i]);  
	  	        item.put("time","评论时间："+m_time[i] );
	  	        c_list.add(item);   
	  	    }
	  	        return c_list;
	         }
	      
	      protected void onPostExecute(ArrayList<Map<String,Object>> Result){
	    	  m_data.addAll(Result);
	    	  m_adapter  = new SimpleAdapter(InfoActivity.this,m_data,R.layout.mycomment_list,  
	    		       new String[]{"videoname","text","time"},new int[]{R.id.c_name,R.id.c_content,R.id.c_time});   
	            mylistview.addFooterView(foot);
	    	    mylistview.setAdapter(m_adapter);
	    	    mylistview.removeFooterView(foot);   
	    	    textview.setText("您共有"+totalnum+"条评论");
				
	         }	
	     } 
	public class MyASyncTask2 extends AsyncTask<String, Integer,  Bitmap> {
	      protected  Bitmap doInBackground(String... param) {
	    	  Bitmap bitmap=getImage(param[0]);
			return   bitmap;
	  	       
	         }      
	   protected void onPostExecute(Bitmap result){
	      if(result!=null){//如果后台数据库存在该图片地址
	    	  userimage.setImageBitmap(result);	
	        }
	       else{//如果后台数据库不存的话
	    	  userimage.setImageResource(R.drawable.ic_launcher);
	      }
			
	   }				  
	}	
public class ScrollListener implements OnScrollListener {
	 int pagesize = 4;// 每页显示条目 		
     int currentpage;// 当前页
     int nextpage;		  
     /**
      * 监听滚动状态改变：1-手指正在滑动 2-手指停止滑动 3-组件停止滚动
      */
     public void onScrollStateChanged(AbsListView view, int scrollState) {
    
     }
     /**
      * firstVisibleItem：第一个可见item 
      * visibleItemCount：可见item数量
      * totalItemCount：目前加载总条目数量
      */
     public void onScroll(AbsListView view, int firstVisibleItem,
             int visibleItemCount, int totalItemCount) {
         final int total = totalItemCount;
         /* 如果滚动到最后一条 */
      //如果到最后了一页  
         System.out.println("total："+total);
         if (mylistview.getLastVisiblePosition() + 1 == totalItemCount) {
             if (totalItemCount > 0) {
                 /* 获取当前页 */
                 currentpage = totalItemCount % pagesize == 0 ? totalItemCount
                         / pagesize
                         : totalItemCount / pagesize+1;
                 nextpage = currentpage+1;
                 /*
                  * 如果当前页小于规定的最大页数，并且加载完成(不断滚动就会不断执行onScroll方法，
                  * 所以用finish_load锁定翻页)
                  */
                 if (nextpage <= maxPage && finish_load) {
                     finish_load = false;
                     /* 每次翻页前添加页脚 */
                     mylistview.addFooterView(foot);
                     /* 创建子线程，执行翻页 */
                     new Thread(new Runnable() {
                         public void run() {
                             try {
                                 Thread.sleep(1000);
                             } catch (InterruptedException e) {
                                 e.printStackTrace();
                             }		                           
                             List<Map<String,Object>> l = getCommentData(total,
                                     pagesize);
                             handle.sendMessage(handle.obtainMessage(100, l));
                         }
                     }).start();
                 }

             }
         }
     }

     /* 通过handle和主线程通讯，主线程接收消息更新UI */
     Handler handle = new Handler() {
         public void handleMessage(Message msg) {
             m_data.addAll((List<Map<String,Object>>) msg.obj);
             m_adapter.notifyDataSetChanged();
             /* 页脚显示完就删掉 */
             if (mylistview.getFooterViewsCount() > 0)//返回0表示有footview
                 mylistview.removeFooterView(foot);
             finish_load = true;
         };
     };
 } 
}
