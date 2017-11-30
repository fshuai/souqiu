package com.dml.home;

import com.dml.adapter.HomeAdapter;
import com.dml.bean.VideoBean;
import com.dml.channel.ChannelActivity;
import com.dml.cluster.LeagueActivity;
import com.dml.internet.GetConnectionHome;
import com.dml.internet.GetConnectionUpdate;
import com.dml.souqiu.R;  //��Ҫ���ˣ����򱨴�
import com.dml.tvPlay.TvForecastActivity;
import com.dml.tvPlay.video_show;
import com.dml.tvPlay.video_show2;
import com.dml.utils.FileUtils;
import com.dml.utils.ImageDownLoader;
import com.dml.utils.ImageDownLoader.onImageLoaderListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class HomeActivity extends Activity implements OnClickListener{
	
	private ViewPager viewPager;     // android-support-v4�еĻ������
    private String[] titles;    // ����ͼƬ����
	private String[] imageUrls;  // viewpagerͼƬurl
	private String[] ids;
	private String[] videoUrls;
	private String[] origins;
	private List<View> dots;   // ͼƬ�������ĵ���Щ��
	
	private TextView tv_title;
	private int currentItem = 0;  // ����ͼƬ�������
	
	private final int CHANGE = 0;
	private final int CONNECT_SUCCESS = 1;
	private final int CONNECT_FAILURE=-1;
	
	//������GridView
	private GridView gridView1;
	private GridView gridView2;
//	private GridView gridView3;
//	private GridView gridView4;
//	private GridView gridView5;
//	private GridView gridView6;
//	private GridView gridView7;
	
	//������������
	private HomeAdapter imageAdapter1 = null;
	private HomeAdapter imageAdapter2 = null;
	private HomeAdapter imageAdapter3 = null;
	private HomeAdapter imageAdapter4 = null;
	private HomeAdapter imageAdapter5 = null;
	private HomeAdapter imageAdapter6 = null;
	private HomeAdapter imageAdapter7 = null;
	
	 //��������Ƶ��Ϣ
	private String[][] infoFootball = null;
	private String[][] infoBasketball = null;
	private String[][] infoTennis = null;
	private String[][] infoPingPong = null;
	private String[][] infoBadminton = null;
	private String[][] infoVolleyball = null;
	private String[][] infoBilliards = null;
	
	//����ࡱ��ť
	private Button btn1 = null;
	private Button btn2 = null;
	private Button btn3 = null;
	private Button btn4 = null;
	private Button btn5 = null;
	private Button btn6 = null;
	private Button btn7 = null;
	
	public ScheduledExecutorService scheduledExecutorService;
	
	private FileUtils fileUtils;
	private ProgressDialog progressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		fileUtils = new FileUtils(this);   //ֻ�ܷ��ڴ˴�
		progressDialog = ProgressDialog.show(HomeActivity.this, "缓存中", "请耐心等候...",true);
		progressDialog.setCancelable(false);//�����ؼ����˳�
		progressDialog.show();
	  
		init();
		   
		new Thread(){
			@Override
			public void run(){
			  infoFootball = GetConnectionHome.getCon(1); 
			  if(infoFootball==null)
					handler.sendEmptyMessage(CONNECT_FAILURE); 
				
				/*   infoBasketball = GetConnectionHome.getCon(2);
				
			  if(infoBasketball==null)//���==null��˵��û������
						handler.sendEmptyMessage(CONNECT_FAILURE); 
				   infoTennis = GetConnectionHome.getCon(3);
				
				if(infoTennis==null)//���==null��˵��û������
					handler.sendEmptyMessage(CONNECT_FAILURE);
				
				  infoPingPong = GetConnectionHome.getCon(4);
				 
				if(infoPingPong==null)//���==null��˵��û������
					handler.sendEmptyMessage(CONNECT_FAILURE);
				
				  infoBadminton = GetConnectionHome.getCon(5);
				
				if(infoBadminton==null)//���==null��˵��û������
					handler.sendEmptyMessage(CONNECT_FAILURE);*/
				
			   /*infoVolleyball = GetConnectionHome.getCon(6);
			        if(infoVolleyball==null)
				     handler.sendEmptyMessage(CONNECT_FAILURE);	
			   infoBilliards = GetConnectionHome.getCon(7);
			      if(infoBilliards==null)//
				     handler.sendEmptyMessage(CONNECT_FAILURE);*/
				infoBilliards = GetConnectionHome.getCon(7);
			    if(infoBilliards==null)//
				     handler.sendEmptyMessage(CONNECT_FAILURE);
				
			   handler.sendEmptyMessage(CONNECT_SUCCESS);
			}
		}.start();
		
	
	}

	 
	protected void init(){
		btn1 = (Button) findViewById(R.id.button_football);
		btn1.setOnClickListener(this);
		
//		btn2 = (Button) findViewById(R.id.button_basketball);
//		btn2.setOnClickListener(this);
//		
//		btn3 = (Button) findViewById(R.id.button_tennis);
//		btn3.setOnClickListener(this);
//		
//		btn4 = (Button) findViewById(R.id.button_pingpong);
//		btn4.setOnClickListener(this);
//		
//		btn5 = (Button) findViewById(R.id.button_badminton);
//		btn5.setOnClickListener(this);
//		
//		btn6 = (Button) findViewById(R.id.button_volleyball);
//		btn6.setOnClickListener(this);
		
		btn7 = (Button) findViewById(R.id.button_billiards);
		btn7.setOnClickListener(this);
		
		
		gridView1 = (GridView) findViewById(R.id.gridView1);
		gridView2 = (GridView) findViewById(R.id.gridView7);
//		gridView3 = (GridView) findViewById(R.id.gridView3);
//		gridView4 = (GridView) findViewById(R.id.gridView4);
//		gridView5 = (GridView) findViewById(R.id.gridView5);
//		gridView6 = (GridView) findViewById(R.id.gridView6); 
//		gridView7 = (GridView) findViewById(R.id.gridView7);
		
		//��ӵ�������¼�
		gridView1.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//更新点击次数
				//GetConnectionUpdate.updateClickNum(1, Integer.parseInt(infoFootball[position][0]));
				Intent intent = new Intent();
				intent.putExtra("videotag", "1");
				intent.putExtra("videoID", infoFootball[position][0]);
				intent.putExtra("videoTitle", infoFootball[position][2]);
				intent.putExtra("videoUrl", infoFootball[position][3]);
				intent.setClass(HomeActivity.this, video_show.class);
				startActivity(intent);
			}
		}); 
		
		
		gridView2.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				//GetConnectionUpdate.updateClickNum(2, Integer.parseInt(infoBasketball[position][0]));
				Intent intent = new Intent();
				intent.putExtra("videotag", "7");
				intent.putExtra("videoID", infoBilliards[position][0]);
				intent.putExtra("videoTitle", infoBilliards[position][2]);
				intent.putExtra("videoUrl", infoBilliards[position][3]);
				intent.setClass(HomeActivity.this, video_show.class); //原来是videoshow2
				startActivity(intent);
			}	
		});
		
//		gridView3.setOnItemClickListener(new OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				//GetConnectionUpdate.updateClickNum(3, Integer.parseInt(infoTennis[position][0]));
//				Intent intent = new Intent();
//				intent.putExtra("videotag", "3");
//				intent.putExtra("videoID", infoTennis[position][0]);
//				intent.putExtra("videoTitle", infoTennis[position][2]);
//				intent.putExtra("videoUrl", infoTennis[position][3]);
//				intent.setClass(HomeActivity.this, video_show.class);
//				startActivity(intent);
//			}	
//		});
//		
//		gridView4.setOnItemClickListener(new OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				//GetConnectionUpdate.updateClickNum(4, Integer.parseInt(infoPingPong[position][0]));
//				Intent intent = new Intent();
//				intent.putExtra("videotag", "4");
//				intent.putExtra("videoID", infoPingPong[position][0]);
//				intent.putExtra("videoTitle", infoPingPong[position][2]);
//				intent.putExtra("videoUrl", infoPingPong[position][3]);
//				intent.setClass(HomeActivity.this, video_show.class);
//				startActivity(intent);
//			}	
//		});
//		
//		gridView5.setOnItemClickListener(new OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				//GetConnectionUpdate.updateClickNum(5, Integer.parseInt(infoBadminton[position][0]));
//				Intent intent = new Intent();
//				intent.putExtra("videotag", "5");
//				intent.putExtra("videoID", infoBadminton[position][0]);
//				intent.putExtra("videoTitle", infoBadminton[position][2]);
//				intent.putExtra("videoUrl", infoBadminton[position][3]);
//				intent.setClass(HomeActivity.this, video_show.class);
//				startActivity(intent);
//			}	
//		});
//		
//		gridView6.setOnItemClickListener(new OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				//GetConnectionUpdate.updateClickNum(6, Integer.parseInt(infoVolleyball[position][0]));
//				Intent intent = new Intent();
//				intent.putExtra("videotag", "6");
//				intent.putExtra("videoID", infoVolleyball[position][0]);
//				intent.putExtra("videoTitle", infoVolleyball[position][2]);
//				intent.putExtra("videoUrl", infoVolleyball[position][3]);
//				intent.setClass(HomeActivity.this, video_show.class);
//				startActivity(intent);
//			}	
//		});
//		
//		gridView7.setOnItemClickListener(new OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				//GetConnectionUpdate.updateClickNum(7, Integer.parseInt(infoBilliards[position][0]));
//				Intent intent = new Intent();
//				intent.putExtra("videotag", "7");
//				intent.putExtra("videoID", infoBilliards[position][0]);
//				intent.putExtra("videoTitle", infoBilliards[position][2]);
//				intent.putExtra("videoUrl", infoBilliards[position][3]);
//				intent.setClass(HomeActivity.this, video_show.class);
//				startActivity(intent);
//			}	
//		});
		
	}
	
	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(android.os.Message msg) {
			switch(msg.what){
			case CHANGE:
				viewPager.setCurrentItem(currentItem);  // �л���ǰ��ʾ��ͼƬ
				break;
			case CONNECT_SUCCESS:
				progressDialog.dismiss();
				
				initViewpager(); 
				
				imageAdapter1 = new HomeAdapter(HomeActivity.this, gridView1, infoFootball);
			    gridView1.setAdapter(imageAdapter1);
				
				imageAdapter2 = new HomeAdapter(HomeActivity.this, gridView2, infoBilliards);
				gridView2.setAdapter(imageAdapter2);
//				
//				imageAdapter3 = new HomeAdapter(HomeActivity.this, gridView3, infoTennis);
//				gridView3.setAdapter(imageAdapter3);
//				
//				imageAdapter4 = new HomeAdapter(HomeActivity.this, gridView4, infoPingPong);
//				gridView4.setAdapter(imageAdapter4);
//				
//				imageAdapter5 = new HomeAdapter(HomeActivity.this, gridView5, infoBadminton);
//				gridView5.setAdapter(imageAdapter5);
//////				
    			//imageAdapter6 = new HomeAdapter(HomeActivity.this, gridView6, infoVolleyball);
			    //gridView6.setAdapter(imageAdapter6);
			
			    //imageAdapter7 = new HomeAdapter(HomeActivity.this, gridView7, infoBilliards);
				//gridView7.setAdapter(imageAdapter7);     
				break;
			case CONNECT_FAILURE:
				progressDialog.dismiss();
				Toast.makeText(HomeActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
				break;
			default:  
				break;
			}
		}
	};
	
	
	//�˳�ȡ������
	@Override
	protected void onDestroy() {
		imageAdapter1.cancelTask();
//	    imageAdapter2.cancelTask();
//		imageAdapter3.cancelTask();
//		imageAdapter4.cancelTask();
//		imageAdapter5.cancelTask();
		imageAdapter6.cancelTask();
		imageAdapter7.cancelTask(); 
		super.onDestroy();   
	}

	protected void initViewpager(){
		ids = new String[7];
//		ids[0] = infoFootball[0][0];
//		ids[1] = infoBasketball[0][0];
//		ids[2] = infoTennis[0][0];
//		ids[3] = infoPingPong[0][0];
//		ids[4] = infoBadminton[0][0];
//		ids[5] = infoVolleyball[0][0];
//		ids[6] = infoBilliards[0][0];
		ids[0] = infoFootball[0][0];
		ids[1] = infoFootball[1][0];
		ids[2] = infoFootball[2][0];
		ids[3] = infoFootball[3][0];
		ids[4] = infoBilliards[0][0];
		ids[5] = infoBilliards[1][0];
		ids[6] = infoBilliards[2][0];
		
		imageUrls = new String[7];
//		imageUrls[0] = infoFootball[0][1];
//		imageUrls[1] = infoBasketball[0][1];
//		imageUrls[2] = infoTennis[0][1];
//		imageUrls[3] = infoPingPong[0][1];
//		imageUrls[4] = infoBadminton[0][1];
//		imageUrls[5] = infoVolleyball[0][1];
//		imageUrls[6] = infoBilliards[0][1];
		imageUrls[0] = infoFootball[0][1];
		imageUrls[1] = infoFootball[1][1];
		imageUrls[2] = infoFootball[2][1];
		imageUrls[3] = infoFootball[3][1];
		imageUrls[4] = infoBilliards[0][1];
		imageUrls[5] = infoBilliards[1][1];
		imageUrls[6] = infoBilliards[2][1];
		
		titles = new String[7];
		//原来的
//		titles[0] = infoFootball[0][2];
//		titles[1] = infoBasketball[0][2];
//		titles[2] = infoTennis[0][2];   
//		titles[3] = infoPingPong[0][2];
//		titles[4] = infoBadminton[0][2];
//		titles[5] = infoVolleyball[0][2];
//		titles[6] = infoBilliards[0][2];
		titles[0] = infoFootball[0][2];
		titles[1] = infoFootball[1][2];
		titles[2] = infoFootball[2][2];
		titles[3] = infoFootball[3][2];
		titles[4] = infoBilliards[0][2];
		titles[5] = infoBilliards[1][2];
		titles[6] = infoBilliards[2][2];
		
		
		videoUrls = new String[7];
//		videoUrls[0] = infoFootball[0][3];
//		videoUrls[1] = infoBasketball[0][3];
//		videoUrls[2] = infoTennis[0][3];   
//		videoUrls[3] = infoPingPong[0][3];
//		videoUrls[4] = infoBadminton[0][3];
//		videoUrls[5] = infoVolleyball[0][3];
//		videoUrls[6] = infoBilliards[0][3];
		videoUrls[0] = infoFootball[0][3];
		videoUrls[1] = infoFootball[1][3];
		videoUrls[2] = infoFootball[2][3];
		videoUrls[3] = infoFootball[3][3];
		videoUrls[4] = infoBilliards[0][3];
		videoUrls[5] = infoBilliards[1][3];
		videoUrls[6] = infoBilliards[2][3];
		
		origins = new String[7];
//		origins[0] = infoFootball[0][4];
//		origins[1] = infoBasketball[0][4];
//		origins[2] = infoTennis[0][4];   
//		origins[3] = infoPingPong[0][4];
//		origins[4] = infoBadminton[0][4];
//		origins[5] = infoVolleyball[0][4];
//		origins[6] = infoBilliards[0][4];
		origins[0] = infoFootball[0][4];
		origins[1] = infoFootball[1][4];
		origins[2] = infoFootball[2][4];
		origins[3] = infoFootball[3][4];
		origins[4] = infoBilliards[0][4];
		origins[5] = infoBilliards[1][4];
		origins[6] = infoBilliards[2][4];
		
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));
		dots.add(findViewById(R.id.v_dot3));
		dots.add(findViewById(R.id.v_dot4));
		dots.add(findViewById(R.id.v_dot5));
		dots.add(findViewById(R.id.v_dot6));
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(titles[0]);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new MyAdapter(HomeActivity.this)); 
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}
	
	//原来是注释
	@Override
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// ��Activity��ʾ������ÿ�������л�һ��ͼƬ��ʾ
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);
		super.onStart();
	}
//
//	@Override
//	protected void onStop() {
//		// ��Activity���ɼ��ʱ��ֹͣ�л�
//		scheduledExecutorService.shutdown();
//		super.onStop();
//	}
		
	//�л�����
	private class ScrollTask implements Runnable {
		public void run() {
			try{
			synchronized (viewPager) {
				System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % imageUrls.length;
				handler.sendEmptyMessage(CHANGE);
			}
			}catch(Exception e){         //�����쳣��Ӱ�춨ʱ�л�
				e.printStackTrace();
			}
		}

	} 
	
	
	/** 
	 * ��ViewPager��ҳ���״̬����ı�ʱ����
	 * @author wangxun
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
	public void onPageSelected(int position) {
			currentItem = position;
			tv_title.setText(titles[position]);
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
	}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * ���ViewPagerҳ���������
	 * @author wangxun
	 */
   
	private class MyAdapter extends PagerAdapter {

	    Context context;
	    
		public MyAdapter(Context context){
			this.context = context;
		}
		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object instantiateItem(View arg0, final int arg1) {
		    ImageView imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.FIT_XY);
			//Bitmap bitmap = null;
		
			if(fileUtils.isFileExists(imageUrls[arg1].replaceAll("[^\\w]", ""))){
				imageView.setImageBitmap(fileUtils.getBitmap(imageUrls[arg1].replaceAll("[^\\w]", ""))); 
			}else{
				imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_empty));
			}
			
			imageView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//GetConnectionUpdate.updateClickNum(arg1 + 1, Integer.parseInt(ids[arg1]));
					Intent intent = new Intent();
					//
					String tag2title="1";
					if(arg1>3){
						tag2title="7";
					}
					intent.putExtra("videotag", tag2title);
					intent.putExtra("videoID", ids[arg1]);
					intent.putExtra("videoTitle", titles[arg1]);
					intent.putExtra("videoUrl", videoUrls[arg1]);
//					 if(arg1==1)//������Ϊ�����Ǵ�0��ʼ�ģ�
//						 intent.setClass(HomeActivity.this, video_show2.class); 
//					 else
					 intent.setClass(HomeActivity.this, video_show.class);
					startActivity(intent);
				}
				
			});
			
			((ViewPager) arg0).addView(imageView);
			return imageView;
			
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		
		switch(v.getId()){
		case R.id.button_football:
			intent.putExtra("videotag", 1);
			break;
//		case R.id.button_basketball:
//			intent.putExtra("videotag", 2);
//			break;
//		case R.id.button_tennis:
//			intent.putExtra("videotag", 3);
//			break;
//		case R.id.button_pingpong:
//			intent.putExtra("videotag", 4);
//			break;
//		case R.id.button_badminton:
//			intent.putExtra("videotag", 5);
//			break;
//		case R.id.button_volleyball:
//			intent.putExtra("videotag", 6);
//			break;
		case R.id.button_billiards:
			intent.putExtra("videotag", 7);
			break;
		default: 
			break; 
		}
//		if(v.getId()==R.id.button_football)
//		  intent.setClass(HomeActivity.this, LeagueActivity.class);	
//		else
		  intent.setClass(HomeActivity.this, MoreActivity.class);
		startActivity(intent);
	}
		
}
