package com.dml.tvPlay;

import com.dml.adapter.GalleryAdapter;
import com.dml.adapter.HomeAdapter;
import com.dml.adapter.JingCaiAdapter;
import com.dml.home.HomeActivity;
import com.dml.myInfo.InfoActivity;
import com.dml.souqiu.R;
import com.dml.tvPlay.video_show.ScrollListener;
import com.dml.utils.MyDataBaseHelper;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;










import android.app.TabActivity;
import android.content.Context;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

public class video_show2 extends TabActivity{  
	List<? extends Map<String, ?>> glist;
	 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
	private SurfaceView surfaceView;  
	private ImageView  btnPlayUrl,btnLarge,btnpre,btnnext,btnsound;  
	private TextView  playtime,totalTime,ltitle;
	private ImageView btn_back;
	
	
	/**
	 * ����Ƿ��һ�ν��벥����
	 */
	private boolean first = true;
	
	private  Button   submit;//�ύ����
	private ImageView  store;//�ղ�
	private EditText e_content; //���ۿ���е�����
	String content="";
	int maxpage=0;//���ڶ��������е������������ж���ҳ��
	
	SharedPreferences  preference;
	SharedPreferences.Editor editor;
	
	ListView c_lv;
	boolean finish_load = true;// �����Ƿ���ɣ�Ĭ�����
	SimpleAdapter c_adapter;
	private View c_foot; 
	private LayoutInflater c_inflater;
	
	private int  c_total;//��������
	private String[] c_mListTitle ;
	private  String[]c_timelist;
	private  String[] c_mListStr;
    List<Map<String,Object>> mData;
	private VideoComment vc;
	
	/**
	 * ����״̬���,0���ţ�1��ͣ
	 */
	private int press = 1;
	private int  playheight;
	
	private SeekBar skbProgress;
	private VerticalSeekBar soundseekBar;  
	private ProgressBar cachebar;
	private Player1 player;
	private RelativeLayout rlayout;
	private FrameLayout flayout; 
	private TextView textView3;
	private RelativeLayout reLayout;
	private RelativeLayout reLayout2;
	private static String u;//���ڴ���������Ƶ��ַ
	private ProgressBar circleP;	//Բ�ν�����	
	private int mProgressStatus = 0;		//��ɽ�����ʼֵ
	private Handler mHandler;		//����һ�����ڴ�����Ϣ��Handler��Ķ���
    private Handler progressHandler ;
    private String  ID="";//���ڱ���ID
    
    BasketURL_Transfer bur;
    private static String Realadd;//������ʵ��Ƶ��ַ
	private static String temstr;//��ʱ�ַ���
	 String []arr_tape;//���ڱ��澫�ʾ�ͷ��ͼƬ��ַ
	 String []arr_starttime;//���ڱ��澫�ʾ�ͷ����ʼʱ��
	 String []arr_totaltime;//���ڱ��澫�ʾ�ͷ����ʱ��
	 String[]arr_preview;//���ڱ�����ƵԤ����ͼƬ��ַ
	 String[]arr_Pstarttime;//���ڱ�����ƵԤ����ÿ����ͷ����ʼʱ��
	 String[]arr_Pduration;//���ڱ�����ƵԤ����ÿ����Ƶ�ĳ���ʱ��
	 String imagetitle;//����ƵԤ���У���Ϊ��ƵԤ����Ҳ�б���
	int sum=0;//��ʱ��
	boolean flag2=false;//�����ж�����ͣ���Ǽ�������
	boolean flag3=false;//�����ж���ȫ�����ǰ���
	boolean flag4=true;//�����Ƿ����ر���
	boolean flag5=false;//�����Ƿ���������seekbar
	boolean flag6=false;
	int i=0;//��������Ŀǰ���ŵ�ַ��������
	private AudioManager audiomanager;
	Fun_TransfetDateFormate fun=new Fun_TransfetDateFormate();
	MyGridview gridview=null;
	 Gallery gallery;
	List<Map<String, Object>> listItems ;
	TabHost tabHost;
	private String videotag;
	SimpleAdapter adapter2=null;
	private MyDataBaseHelper dbhelper;
	
	@Override
   protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		setContentView(R.layout.video_show);
		dbhelper=new MyDataBaseHelper(this, "play.db",  1);
		Intent it =getIntent();
		u=it.getStringExtra("videoUrl");
		imagetitle=it.getStringExtra("videoTitle");
		ID=it.getStringExtra("videoID"); 	
		videotag = it.getStringExtra("videotag");				
		new  Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				videoID videoId=new videoID(ID, videotag);   
				videoId.getImageList();
				arr_tape=videoId.getArr_tape(); 
				arr_starttime=videoId.getArr_starttime();
				arr_totaltime=videoId.getArr_totaltime();
				arr_preview=videoId.getArr_preview();
				arr_Pstarttime=videoId.getArr_Pstarttime();
				arr_Pduration=videoId.getArr_Pduration(); 
				handler1.sendEmptyMessage(1);
			}
			
			
			
		}.start();
		btn_back = (ImageView) findViewById(R.id.videoshow_back);
		btn_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
	surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1); 
		
		btnPlayUrl = (ImageView) this.findViewById(R.id.btnPlayUrl);
		btnLarge=(ImageView)this.findViewById(R.id.btnLarge);
		btnpre=(ImageView)this.findViewById(R.id.btnpre);
		btnnext=(ImageView)this.findViewById(R.id.btnnext);
		btnsound=(ImageView)this.findViewById(R.id.sound); 
		 store=(ImageView)findViewById(R.id.store);
	    mData= new ArrayList<Map<String,Object>>();	
		playtime=(TextView)findViewById(R.id.time1);
		totalTime=(TextView)findViewById(R.id.time2);
		  ltitle=(TextView)findViewById(R.id.ltitle);
		rlayout=(RelativeLayout)findViewById(R.id.relativelayout);
		flayout=(FrameLayout)findViewById(R.id.framelayout);
		reLayout = (RelativeLayout) findViewById(R.id.reLayout);
		 reLayout2 = (RelativeLayout) findViewById(R.id.reLayout2);
		circleP=(ProgressBar)findViewById(R.id.progressBar2);		//��ȡԲ�ν�����
		circleP.setVisibility(View.GONE); 
		ltitle.setText(imagetitle);
		ltitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		ltitle.getPaint().setFakeBoldText(true);//�Ӵ�
		btnpre.setEnabled(false);
		btnnext.setEnabled(false);
		audiomanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);//���ϵͳ��Ƶ�������	
		tabHost=(TabHost)findViewById(android.R.id.tabhost);	//��ȡTabHost����
		tabHost.setup();	//��ʼ��TabHost��� 
		tabHost.setup(this.getLocalActivityManager()); 
		LayoutInflater inflater = LayoutInflater.from(this); 	// ������ʵ����һ��LayoutInflater����  
        inflater.inflate(R.layout.videoshow_tab1, tabHost.getTabContentView());  
        inflater.inflate(R.layout.videoshow_tab2, tabHost.getTabContentView());  
     	   
         tabHost.addTab(tabHost.newTabSpec("tab1")
         		.setIndicator("������Ƶ��ͷ")
         		.setContent(R.id.r1));  	//��ӵ�1����ǩҳ
         tabHost.addTab(tabHost.newTabSpec("tab2")
          		.setIndicator("�������")
          		.setContent(R.id.l1));  	//��ӵ�1����ǩҳ
        c_inflater=getLayoutInflater();
 		c_foot=(View) c_inflater.inflate(R.layout.comment_foot, null);///////����������һ�����֣�����д��view����ʽ
        submit=(Button)findViewById(R.id.bsubmit);//ע����Щ��tab�е�UI���Ҫ��tabhost��ʼ����ɺ�
        cachebar=(ProgressBar)findViewById(R.id.cachebar);
        e_content=(EditText)findViewById(R.id.discuss);
		c_lv=(ListView)findViewById(R.id.c_list);
		c_lv.setOnScrollListener(new ScrollListener() );
		
		   MyASyncTask yncTask = new MyASyncTask();
           yncTask.execute("0");
		
        TextView textview=(TextView)findViewById(R.id.textview);
		TextView textview1=(TextView)findViewById(R.id.textview1);
		textview.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//�Ӵ�
		textview1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		textview.getPaint().setFakeBoldText(true);//�Ӵ�
		textview1.getPaint().setFakeBoldText(true);//�Ӵ�
		gridview = (MyGridview) findViewById(R.id.gridView1); // ��ȡGridView���
		gallery = (Gallery) findViewById(R.id.gallery1); // ��ȡGallery���
		soundseekBar=(VerticalSeekBar)findViewById(R.id.soundseekbar);//����������
		soundseekBar.setProgress(audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC));
		soundseekBar.setMax(audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));//������ϵͳ��������Ӧ����	
    gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
		    circleP.setVisibility(View.VISIBLE);
				Toast.makeText(video_show2.this,
						"��ѡ���˵�" + String.valueOf(position) + "��ͼƬ"+arr_Pstarttime[position],
						Toast.LENGTH_LONG).show();
				 new  Thread(){
						public void run() {
							// TODO Auto-generated method stub
							bur=new BasketURL_Transfer();
							temstr=bur.getVideoUrl(ID.trim());														
							Message m=new Message();
							m.what=2;					
							Bundle b=new Bundle();
							b.putString("result", temstr);	
							b.putInt("start", position);
							m.obj=b;
							handler1.sendMessage(m);
						}				
						
					}.start();
			
			}
		});

		
  gridview.setOnItemClickListener(new OnItemClickListener() {
	@Override
	    public void onItemClick(AdapterView<?> parent, View view, final int postion, long id) {
					// TODO Auto-generated method stub
		 circleP.setVisibility(View.VISIBLE);	
	     Toast.makeText(video_show2.this, "��ѡ���˵�"+postion+"��ͼƬ"+"videotape:"+arr_tape[postion]+"��ʼʱ��"+arr_starttime[postion], Toast.LENGTH_SHORT).show();
	     new  Thread(){
				public void run() {
					// TODO Auto-generated method stub
					bur=new BasketURL_Transfer();
					temstr=bur.getVideoUrl(ID.trim());
					Message m=new Message();
					m.what=4;					
					Bundle b=new Bundle();
					b.putString("result", temstr);
					b.putInt("start", postion);
					m.obj=b;
					handler1.sendMessage(m);
				}						
				
			}.start();
	     }		
				});
    //�ύ���ۺ�Ĵ��� 
  submit.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		 SharedPreferences sp = getSharedPreferences("USERPASS",InfoActivity.MODE_PRIVATE);    
		int Login_state=sp.getInt("Login_state", 0);
		final String s_name=sp.getString("Login_name", "");//���ڽ���¼��Ա���������  
		   if(Login_state==1){
			Toast.makeText(video_show2.this, "���������Ѿ��ύ�ɹ�:", Toast.LENGTH_SHORT).show();  
	         //����Ҫд�뵽���ݿ���,�ٸ���listview
			 content=e_content.getText().toString(); 		
			new Thread(){
				public void run() {
		    String store_path="http://202.114.18.96:8080/SouQiuWang/Store";
		    try {
			    URL url=new URL(store_path);
				HttpURLConnection conn=(HttpURLConnection)url.openConnection();
				conn.setRequestMethod("POST"); 
				conn.setDoInput(true); 
				conn.setDoOutput(true); 
				DataOutputStream out=new DataOutputStream(conn.getOutputStream());
				String params="name="+URLEncoder.encode(s_name,"utf-8")+
						"&videoid="+URLEncoder.encode(ID,"utf-8")+				
						"&videotag="+URLEncoder.encode(videotag,"utf-8")+				
						"&content="+URLEncoder.encode(content,"utf-8")+				
						"&videotitle="+URLEncoder.encode(imagetitle,"utf-8");
				out.writeBytes(params); 
				out.flush();    
				out.close();
				
		//����ֻ��Ҫ����Ӧ�𣬶�����Ҫ��ȡ�κ����ݣ���̨Ҳû������κ�����		
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
				}
				conn.disconnect();
				}catch(Exception e){  
					e.printStackTrace();
					}
				}
				 
			}.start();
			  e_content.setText("");//�������Ժ�
			  mData.clear();			
			 MyASyncTask yncTask = new MyASyncTask();
	           yncTask.execute("0");
			finish_load=true;
			c_adapter.notifyDataSetChanged(); 
	        }
	        else{
	        	Toast.makeText(video_show2.this, "����δ��¼��������¼�������ۣ�", Toast.LENGTH_SHORT).show();	
	        }
		}
	});

	Cursor cursor=dbhelper.getReadableDatabase().
		       rawQuery("select * from videostore where videotag_id = ?",new String[]{videotag+ID}); 
	if(cursor.getCount()>0){
		flag6 = true;
		store.setImageResource(R.drawable.likeit);
	}else{
		flag6 = false;
		store.setImageResource(R.drawable.like);
	}
    
    //�ղ���Ƶ��Ĵ���
   store.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
	    	String videotag_id=videotag+ID;//����Ƚ������videotag��id�����һ�𹹳�һ���ֶΣ�����sqlitedatabase��ֻ��idΪ��������ԲŻ���������
	    	if(flag6 == false){
	    		store.setImageResource(R.drawable.likeit);
	              try
	              {
	           dbhelper.getReadableDatabase().execSQL("insert into videostore values(null,?,?,?,?,?)",new String[]{videotag_id,u,arr_tape[0],imagetitle,sdf.format(new Date())});      
	           Toast.makeText(video_show2.this, "�ղسɹ�", Toast.LENGTH_SHORT).show();     
	              }catch(Exception e){//����ס�쳣�����ڲ���
	            	  e.printStackTrace();
	              }			    		   		
	    		flag6=true;
	    	
	    	}
	    	else{		
	    		store.setImageResource(R.drawable.like);
	    	    dbhelper.getWritableDatabase().delete("videostore", "videotag_id like ?", new String[]{videotag_id});
	    	    Toast.makeText(video_show2.this, "ȡ���ղ�", Toast.LENGTH_SHORT).show();     	
	    		flag6=false;
	    	}
	    }
    });	
        	            	
 
	
    surfaceView.setOnTouchListener(new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
		          switch(event.getAction()& MotionEvent.ACTION_MASK){
		             case   MotionEvent.ACTION_DOWN: 	 
		             if(player.mediaPlayer.isPlaying()){//�����Ƶ���ڲ��ŵ�ǰ����
		            	 if(flag4){
				    	 reLayout.setVisibility(View.INVISIBLE);
				    	 flag4 = false;
		            	 }
				       
				     else{ 
				        reLayout.setVisibility(View.VISIBLE);
				        flag4 = true;
				        handler1.sendEmptyMessageDelayed(0x123, 3000);
				       }
		            	
			   
		             }
		             else                               
		            	 if(player.mediaPlayer.isPlaying()==false){  //���û����
		            	   reLayout.setVisibility(View.VISIBLE);
							i = 0;
							player.mediaPlayer.pause();
							btnPlayUrl.setImageResource(R.drawable.play);
							player.begningtime = 0;
							press = 1;	
							
		             }
		             break;
			}
			return true;
		}
	  });
    btnPlayUrl.setOnClickListener(new View.OnClickListener() {	
		@Override
	      public void onClick(View v) {		 
			if(press == 0){
				player.mediaPlayer.pause(); 
	    		btnPlayUrl.setImageResource(R.drawable.play);
	    		press = 1;
	    	}else{
	    		btnPlayUrl.setImageResource(R.drawable.pause);
	    		press = 0;
	    		if(!first){
	    			player.mediaPlayer.start();
	    		}else{
	    			circleP.setVisibility(View.VISIBLE);	
	    			first = false;	    			
				new  Thread(){
					public void run() {
						// TODO Auto-generated method stub
						bur=new BasketURL_Transfer();
						temstr=bur.getVideoUrl(ID.trim());						
						Message m=new Message();
						m.what=3;					
						Bundle b=new Bundle();
						b.putString("result", temstr);							
						m.obj=b;
						handler1.sendMessage(m);
					}
				}.start();          
	    		}
			}
		}
		});  

  
   btnLarge.setOnClickListener(new View.OnClickListener() {	
	@Override
	       public void onClick(View v) {
         WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
         full(!flag3);
       if(flag3==false){
	     int videoWidth = wm.getDefaultDisplay().getWidth();
         int videoHeight = wm.getDefaultDisplay().getHeight();
         Log.e("width", videoWidth + " " + videoHeight);
     	btnLarge.setImageResource(R.drawable.halfscreen);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
        rlayout.setVisibility(RelativeLayout.GONE);
        reLayout2.setVisibility(View.GONE);
	    android.view.ViewGroup.LayoutParams l=flayout.getLayoutParams();
	    Log.e("width",  l.height + " " + l.width);
	    l.height=videoWidth;//������ĸ߶���ԭ���Ŀ��
	     flag3=true;	
           }
           
    else{
    	btnLarge.setImageResource(R.drawable.fullscreen);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    android.view.ViewGroup.LayoutParams l=flayout.getLayoutParams();
	     l.height=playheight;
         rlayout.setVisibility(RelativeLayout.VISIBLE);
         reLayout2.setVisibility(RelativeLayout.VISIBLE);

	    flag3=false;
         }
        } 	
		
	
    }); 
    btnpre.setOnClickListener(new View.OnClickListener() {
    	
	    @Override
	    public void onClick(View v) {
		    // TODO Auto-generated method stub
	        if(player.mediaPlayer.getCurrentPosition() > 15000){
		        player.mediaPlayer.seekTo(player.mediaPlayer.getCurrentPosition() - 15 * 1000);
	        }else{
	        	player.mediaPlayer.seekTo(0);
	        }
	    }
    });
    
    btnnext.setOnClickListener(new View.OnClickListener() {
	
	    @Override
	    public void onClick(View v) {
	     	// TODO Auto-generated method stub
	        if(player.mediaPlayer.getCurrentPosition() + 15000 > sum * 1000){
	        	 player.mediaPlayer.seekTo(sum*1000);
	        	 player.mediaPlayer.pause();
	        	 press = 1;
	        	 btnPlayUrl.setImageResource(R.drawable.play);
	        }else{
		         player.mediaPlayer.seekTo(player.mediaPlayer.getCurrentPosition() + 15 * 1000);
	        }
	    }
     });
    
    skbProgress = (SeekBar) this.findViewById(R.id.skbProgress); 
		skbProgress.setEnabled(false);
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		player = new Player1(surfaceView, skbProgress); 
	btnsound.setOnClickListener(new View.OnClickListener() {
		
		@Override
		     public void onClick(View v) {
			// TODO Auto-generated method stub
			    if(flag5==false){
				soundseekBar.setVisibility(View.VISIBLE);
				flag5=true;
		     }
			else{
				soundseekBar.setVisibility(View.GONE);
				flag5=false;
			}
			
		}
	});
    soundseekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub	
			}
			  
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int Cprogress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, Cprogress, 0);
				soundseekBar.setProgress(audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC));
				
			}
		});	
		}  

	@Override
	public void onDestroy(){
		super.onDestroy();
		player.stop();
	}
	
	private Handler handler1 = new Handler() {
		public void dispatchMessage(android.os.Message msg){
			int iii=0;
			int sum_=0;
			Bundle b;
			int t=0;
			int BegTime=0;
			int g_totaltime=0;
			int postion=0;
			String uu;//���϶�����ʱ����
			switch(msg.what){
			case 0x123:
				reLayout.setVisibility(View.INVISIBLE);
				flag4 = false;
				break;
			case 1:
				cachebar.setVisibility(View.GONE);
				gallery.setAdapter(new GalleryAdapter(video_show2.this, arr_preview, gallery));
				gallery.setSelection(1);
				gridview.setAdapter(new JingCaiAdapter(video_show2.this, arr_tape, arr_totaltime, gridview)); 
				break;
			case 2:
		
				 b=(Bundle)msg.obj;
				 temstr=b.getString("result");
				 postion =b.getInt("start");			
				 Realadd=temstr.substring(0, temstr.indexOf("#"));	
				 sum=Integer.parseInt(temstr.substring(temstr.indexOf("#")+1));	
			     totalTime.setText(fun.Fun(sum/1000));		
	             t=(int)(Float.parseFloat(arr_Pstarttime[postion]));
	             player.playUrl(Realadd,t);//���� 	 			     
	             btnpre.setEnabled(true);
		         btnnext.setEnabled(true);
		        skbProgress.setEnabled(true);			
				break;
			case 3:				
				/******���²��ź�Ҫ��ʼ��sum��pre��i��videoaddress*****/	
				   b=(Bundle)msg.obj;
				   temstr=b.getString("result");
				   Realadd=temstr.substring(0, temstr.indexOf("#"));	
				   sum=Integer.parseInt(temstr.substring(temstr.indexOf("#")+1));
				   player.playUrl(Realadd);//���ø÷���			
				   totalTime.setText(fun.Fun(sum/1000));
				   btnpre.setEnabled(true);
				   btnnext.setEnabled(true);
				   skbProgress.setEnabled(true);
		         //  Toast.makeText(video_show2.this, "��ʼ���ţ�"+"videoid:"+ID, Toast.LENGTH_LONG).show(); 	    
				    
				
				break;
			case 4:
				 b=(Bundle)msg.obj;
				 temstr=b.getString("result");
				 postion =b.getInt("start");			
				 Realadd=temstr.substring(0, temstr.indexOf("#"));	
				 sum=Integer.parseInt(temstr.substring(temstr.indexOf("#")+1));	
			     totalTime.setText(fun.Fun(sum/1000));		
	             t=(int)(Float.parseFloat(arr_Pstarttime[postion]));
	             player.playUrl(Realadd,t);//���� 	 			     
	             btnpre.setEnabled(true);
		         btnnext.setEnabled(true);
		        skbProgress.setEnabled(true);		
				break;
			default:
				break;
			}
		}
	};
	

	/**
	 * ��̬����״̬������ʾ
	 * @param enable
	 */
	private void full(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
	
   class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener { 
	    int curprogress; //Ҫ����Ϊȫ�ֱ���������
	@Override 
      public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {   	
		
		this.curprogress = progress * player.mediaPlayer.getDuration()    / seekBar.getMax(); 
        
	
	             }  
	@Override 
     public void onStartTrackingTouch(SeekBar seekBar) {  		
		} 
	@Override 
     public void onStopTrackingTouch(SeekBar seekBar) {   
	    circleP.setVisibility(View.VISIBLE); 
		
		player.mediaPlayer.seekTo(curprogress);//���ŵ�ʱ��λ��
		
			}
		}
  
  
   class Player1 implements OnBufferingUpdateListener, OnPreparedListener, OnCompletionListener,SurfaceHolder.Callback {  
	  
       int begningtime=0;
	   int videoWidth,videoHeight;
	   public MediaPlayer mediaPlayer; 
	   private SurfaceHolder surfaceHolder; 
	   private SeekBar skbProgress;
	   private ProgressBar circlp;
	   
	   
	  Timer mTimer=new Timer();  
	 
		public Player1(SurfaceView surfaceView, SeekBar skbProgress) {
			this.skbProgress = skbProgress;
			surfaceHolder = surfaceView.getHolder();
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			 mTimer.schedule(mTimerTask, 0, 1000);
			  
		}
		
/*************  * ͨ����ʱ����Handler�����½�����  ********/ 
	    TimerTask mTimerTask = new TimerTask() {  
		@Override 
		public void run() {  
			if(mediaPlayer==null)  
				return;  
			if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) { 
			
				handleProgress.sendEmptyMessage(0);  
				} 
			}
		};  
         Handler handleProgress = new Handler() {  
	     
	 public void handleMessage(Message msg) {  
	          int position = mediaPlayer.getCurrentPosition();//����ʱ��  
	      
	          playtime.setText(fun.Fun(position/1000));//���ݲ��Ž���������ʱ��
	          	     
	          int duration = mediaPlayer.getDuration();      
	          if (duration > 0) { 
		      long pos = skbProgress.getMax() * position / (sum);//��ռ�İٷֱ�
	          skbProgress.setProgress((int) pos); 		
					} 	
				} 
				}; 
//***************************************************** 
   public void play()  { 		
		mediaPlayer.start(); 
					} 
   public void playUrl(String str,int time)  {  					
		  try 
		   { 
			 if(mediaPlayer.isPlaying()){
				 begningtime=time;
				 mediaPlayer.seekTo(begningtime*1000);	
		     } else
		   {
			  mediaPlayer.reset();//
			  mediaPlayer.setDataSource(str);
			  
			  this.begningtime=time;
			  //mediaPlayer.prepare();//�Զ�����
			  mediaPlayer.prepareAsync();
			 }
		      }catch(Exception e){
			}
		   }
	   public void playUrl(String str)  {  					
	  	  try { 
	  		  this.begningtime=0;
	  		  mediaPlayer.reset();//
	  		  mediaPlayer.setDataSource(str);
	  		  
	  		  mediaPlayer.prepareAsync();//�Զ�����
	  		  String videotag_id=videotag+ID;//����Ƚ������videotag��id�����һ�𹹳�һ���ֶΣ�����sqlitedatabase��ֻ��idΪ��������ԲŻ���������	            
	       dbhelper.getReadableDatabase().execSQL("insert into videoplay values(null,?,?,?,?,?)",new String[]{videotag_id,u,arr_tape[0],imagetitle,sdf.format(new Date())});      
	           
	  	       
	  	       } catch(Exception e){
	  	    	   e.printStackTrace();
	  		     }
	  	       }				

   public void stop()  {  
						
			if (mediaPlayer != null) {   
							mediaPlayer.stop();      
							mediaPlayer.release();     
							mediaPlayer = null;     
							}  
						}
					@Override
					
   public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) { 
				Log.e("mediaPlayer", "surface changed"); 
		
				} 
		@Override
   public void surfaceCreated(SurfaceHolder arg0) { 
				try {  
					
					mediaPlayer = new MediaPlayer();  
					mediaPlayer.setDisplay(surfaceHolder); 
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);  
					mediaPlayer.setOnBufferingUpdateListener(this); 
				    mediaPlayer.setOnPreparedListener(this); 
					mediaPlayer.setOnCompletionListener(this);	
					playheight = surfaceView.getMeasuredHeight();
				} 
				catch (Exception e) {  
					Log.e("mediaPlayer", "error", e); 
								
				} 
				Log.e("mediaPlayer", "surface created"); 
								}  
	    @Override
	public void surfaceDestroyed(SurfaceHolder arg0) { 	
			Log.e("mediaPlayer", "surface destroyed"); 
						} 
	
	@Override
	public void onPrepared(MediaPlayer mp) {  
					mp.start();
		    	mp.seekTo(begningtime*1000);
		    	handler1.sendEmptyMessageDelayed(0x123, 3000);
						
			}   
	
	
	@Override 
	public void onCompletion(MediaPlayer mp) { 
		
		  if(mp.getCurrentPosition()>=mp.getDuration()){
			mediaPlayer.pause();
			btnPlayUrl.setImageResource(R.drawable.play);
			reLayout.setVisibility(View.VISIBLE);
			begningtime = 0;
			press = 1;
		  }
	} 
								
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int bufferingProgress) { 				 
	/*********��ǰ����Ļ�������ʾӦ����Ŀǰ����ļ���ǰ����Ѳ��ŵ�******����ı�����ʽ***/
    	skbProgress.setSecondaryProgress(bufferingProgress);
	     int currentProgress=skbProgress.getMax()* mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();  
	     if(mediaPlayer.getCurrentPosition()*100/mediaPlayer.getDuration()+1<bufferingProgress){
	    	  circleP.setVisibility(View.GONE); 
	     }
	        if(flag2==true)//�����ͣ��
	    	   circleP.setVisibility(View.GONE);    
	}
				}
  	   
   public   List<Map<String,Object>> getData(int startposition, int pagesize) {			   
			    System.out.println("��ʼ��"+startposition);			 
			    vc=new VideoComment(ID,videotag,startposition);//���ڴ����ݿ��ж�����Ƶ������Ϣ
				vc.getCommentMessage();
				c_mListTitle=vc.com_name;
				c_mListStr=vc.com_content;
				c_timelist=vc.com_time;
				c_total=vc.total;//��������
				maxpage= c_total/pagesize+1;// ���ҳ��
	 ArrayList<Map<String,Object>> c_list= new ArrayList<Map<String,Object>>();	
			    for(int i =0;i <pagesize&&i<c_timelist.length; i++) { 
			    //ע������Ҫ�к������������Ȼ���û����ҳ�������Խ����� 
			        Map<String,Object> item = new HashMap<String,Object>();  	      
			        item.put("name", c_mListTitle[i]);  
			        item.put("time",c_timelist[i] );
			        item.put("text", c_mListStr[i]);  
			        c_list.add(item);   
			    }
			        return c_list;
			    }
   private class MyASyncTask extends AsyncTask<String, Integer, ArrayList<Map<String,Object>>> {
	      protected ArrayList<Map<String,Object>> doInBackground(String... param) {
	    	  vc=new VideoComment(ID,videotag,Integer.valueOf(param[0]));//���ڴ����ݿ��ж�����Ƶ������Ϣ
				vc.getCommentMessage();
				c_mListTitle=vc.com_name;
				c_mListStr=vc.com_content;
				c_timelist=vc.com_time;
				c_total=vc.total;//��������
				maxpage= c_total/4+1;// ���ҳ��(����4�������pagesize)
	 ArrayList<Map<String,Object>> c_list= new ArrayList<Map<String,Object>>();	
			    for(int i =0;i <4&&i<c_timelist.length; i++) { 
			    //ע������Ҫ�к������������Ȼ���û����ҳ�������Խ����� 
			        Map<String,Object> item = new HashMap<String,Object>();  	      
			        item.put("name", c_mListTitle[i]);  
			        item.put("time",c_timelist[i] );
			        item.put("text", c_mListStr[i]);  
			        c_list.add(item);   
			    }
			        return c_list;
	         }
	      protected void onPostExecute(ArrayList<Map<String,Object>> Result){
	    	  mData.addAll(Result);
	  		c_adapter  = new SimpleAdapter(video_show2.this,mData,R.layout.comment_list,  
	  		        new String[]{"name","time","text"},new int[]{R.id.c_name,R.id.c_time,R.id.c_content});  
	  		c_lv.addFooterView(c_foot);
	  	    c_lv.setAdapter(c_adapter);
	  	    c_lv.removeFooterView(c_foot);
	      }
	      }	    	
   public class ScrollListener implements OnScrollListener {
			 int pagesize = 4;// ÿҳ��ʾ��Ŀ 		
		     int currentpage;// ��ǰҳ
		     int nextpage;		  
		     /**
		      * ��������״̬�ı䣺1-��ָ���ڻ��� 2-��ָֹͣ���� 3-���ֹͣ����
		      */
		     public void onScrollStateChanged(AbsListView view, int scrollState) {
		    
		     }
		     /**
		      * firstVisibleItem����һ���ɼ�item 
		      * visibleItemCount���ɼ�item����
		      * totalItemCount��Ŀǰ��������Ŀ����
		      */
		     public void onScroll(AbsListView view, int firstVisibleItem,
		             int visibleItemCount, int totalItemCount) {
		         final int total = totalItemCount;
		         /* ������������һ�� */
		      //����������һҳ  
		         System.out.println("total��"+total);
		         if (c_lv.getLastVisiblePosition() + 1 == totalItemCount) {
		             if (totalItemCount > 0) {
		                 /* ��ȡ��ǰҳ */
		                 currentpage = totalItemCount % pagesize == 0 ? totalItemCount
		                         / pagesize
		                         : totalItemCount / pagesize+1;
		                 nextpage = currentpage+1;
		                 /*
		                  * �����ǰҳС�ڹ涨�����ҳ�������Ҽ������(���Ϲ����ͻ᲻��ִ��onScroll������
		                  * ������finish_load������ҳ)
		                  */
		                 if (nextpage <= maxpage && finish_load) {
		                     finish_load = false;
		                     /* ÿ�η�ҳǰ���ҳ�� */
		                     c_lv.addFooterView(c_foot);
		                     /* �������̣߳�ִ�з�ҳ */
		                     new Thread(new Runnable() {
		                         public void run() {
		                             try {
		                                 Thread.sleep(1000);
		                             } catch (InterruptedException e) {
		                                 e.printStackTrace();
		                             }		                           
		                             List<Map<String,Object>> l = getData(total,
		                                     pagesize);
		                             handle.sendMessage(handle.obtainMessage(100, l));
		                         }
		                     }).start();
		                 }

		             }
		         }
		     }

		     /* ͨ��handle�����߳�ͨѶ�����߳̽�����Ϣ����UI */
		     Handler handle = new Handler() {
		         public void handleMessage(Message msg) {
		             mData.addAll((List<Map<String,Object>>) msg.obj);
		             c_adapter.notifyDataSetChanged();
		             /* ҳ����ʾ���ɾ�� */
		             if (c_lv.getFooterViewsCount() > 0)//����0��ʾ��footview
		                 c_lv.removeFooterView(c_foot);
		             finish_load = true;
		         };
		     };
		 }
	
}
	 