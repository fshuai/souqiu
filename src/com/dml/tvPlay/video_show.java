package com.dml.tvPlay;

import com.dml.adapter.GalleryAdapter;
import com.dml.adapter.HomeAdapter;
import com.dml.adapter.JingCaiAdapter;
import com.dml.cluster.SimilarActivity;
import com.dml.home.HomeActivity;
import com.dml.myInfo.InfoActivity;
import com.dml.myInfo.Personal;
import com.dml.myInfo.SelectPicPopupWindow;
import com.dml.share.Util;
import com.dml.souqiu.R;
import com.dml.souqiu.wxapi.WXEntryActivity;
import com.dml.utils.MyDataBaseHelper;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
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
import android.graphics.drawable.BitmapDrawable;
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
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import android.widget.PopupMenu;
import android.widget.PopupWindow;
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

public class video_show extends TabActivity {
	List<? extends Map<String, ?>> glist;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SurfaceView surfaceView;
	private ImageView btnPlayUrl, btnLarge, btnpre, btnnext, btnsound;
	private TextView playtime, totalTime, ltitle;
	private ImageView btn_back;

	private int u_playtime = 0;// 用户播放时间;

	private Date staDate;
	private Date endDate;
	private ImageView share;// 分享
	public static Tencent mTencent = null;

	private boolean first = true;
	private Button submit;// �ύ����
	private ImageView store;// �ղ�
	private EditText e_content; // ���ۿ���е�����
	String content = "";
	int maxpage = 0;// ���ڶ��������е����������ж���ҳ��
	SharedPreferences preference;
	SharedPreferences.Editor editor;
	ListView c_lv;
	boolean finish_load = true;// �����Ƿ���ɣ�Ĭ�����
	SimpleAdapter c_adapter;
	private View c_foot;
	private LayoutInflater c_inflater;

	private int c_total;// ��������
	private String[] c_mListTitle;
	private String[] c_timelist;
	private String[] c_mListStr;
	List<Map<String, Object>> mData;
	private VideoComment vc;

	private int press = 1;
	private int playheight;

	private SeekBar skbProgress;
	private VerticalSeekBar soundseekBar;
	private ProgressBar cachebar;
	private Player player;
	private RelativeLayout rlayout;
	private FrameLayout flayout;
	private TextView textView3;
	private RelativeLayout reLayout;
	private RelativeLayout reLayout2;
	private static String u;//存储视频url
	private ProgressBar circleP; // Բ�ν����
	private int mProgressStatus = 0; // ��ɽ����ʼֵ
	private Handler mHandler; // ����һ�����ڴ�����Ϣ��Handler��Ķ���
	private Handler progressHandler;
	private String ID = "";
	LinkedList<ArrayList> linkedlist = new LinkedList<ArrayList>();// ���ڱ����ε�ַ��ʱ����������Ƶ��ַ����
	ArrayList<String> videolist = new ArrayList<String>();// ���ڱ����ε�ַ����Ƶ��ַ����
	ArrayList<String> timelist = new ArrayList<String>();// ���ڱ����ε�ַ����Ƶʱ�������
	clipSegVideoUrl videoAddress;
	String[] arr_tape;// ���ڱ��澫�ʾ�ͷ��ͼƬ��ַ
	String[] arr_starttime;// ���ڱ��澫�ʾ�ͷ����ʼʱ��
	String[] arr_totaltime;// ���ڱ��澫�ʾ�ͷ����ʱ��
	String[] arr_preview;// ���ڱ�����ƵԤ����ͼƬ��ַ
	String[] arr_Pstarttime;// ���ڱ�����ƵԤ����ÿ����ͷ����ʼʱ��
	String[] arr_Pduration;// ���ڱ�����ƵԤ����ÿ����Ƶ�ĳ���ʱ��
	String imagetitle;// ����ƵԤ���У���Ϊ��ƵԤ����Ҳ�б���

	int sum = 0;// ��ʱ��
	int pre = 0;// ���ڼ���ǰ�沥�Ŷ�ε�ַ����ʱ��
	int jj = 0;// �����϶����ж����Ķε�ַ
	int i = 0;// ��������Ŀǰ���ŵ�ַ��������

	boolean flag = false;// �����ж��Ƿ�������Ϊ�϶�
	boolean flag2 = false;// �����ж�����ͣ���Ǽ����
	boolean flag3 = false;// �����ж���ȫ�����ǰ���
	boolean flag4 = true;// �����Ƿ����ر���
	boolean flag5 = false;// �����Ƿ���������seekbar
	boolean flag6 = false;

	private String username;
	private String DEVICE_ID;

	private AudioManager audiomanager;
	Fun_TransfetDateFormate fun = new Fun_TransfetDateFormate();
	MyGridview gridview = null;
	Gallery gallery;
	List<Map<String, Object>> listItems;
	TabHost tabHost;
	private String videotag;
	SimpleAdapter adapter2 = null;
	private MyDataBaseHelper dbhelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.video_show);
		dbhelper = new MyDataBaseHelper(this, "play.db", 1);
		Intent it = getIntent();
		u = it.getStringExtra("videoUrl");
		imagetitle = it.getStringExtra("videoTitle");
		ID = it.getStringExtra("videoID");
		videotag = it.getStringExtra("videotag");
		System.out.println("videourl:"+u+" imagetitle"+imagetitle+" videoid:"+ID+" videotag:"+videotag);
		new Thread() {
			public void run() {
				// TODO Auto-generated method stub
				videoID videoId = new videoID(ID, videotag);
				videoId.getImageList();
				arr_tape = videoId.getArr_tape();
				arr_starttime = videoId.getArr_starttime();
				arr_totaltime = videoId.getArr_totaltime();
				arr_preview = videoId.getArr_preview();
				arr_Pstarttime = videoId.getArr_Pstarttime();
				arr_Pduration = videoId.getArr_Pduration();
				System.out.println("arr_tape:"+Arrays.toString(arr_tape));
				System.out.println("arr_starttime:"+Arrays.toString(arr_starttime));
				System.out.println("arr_totaltime:"+Arrays.toString(arr_totaltime));
				System.out.println("arr_preview:"+Arrays.toString(arr_preview));
				System.out.println("arr_Pstarttime:"+Arrays.toString(arr_Pstarttime));
				System.out.println("arr_Pduration:"+Arrays.toString(arr_Pduration));
				handler1.sendEmptyMessage(1);
			}
		}.start();

		btn_back = (ImageView) findViewById(R.id.videoshow_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});

		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		DEVICE_ID = tm.getDeviceId();
		SharedPreferences sp = getSharedPreferences("USERPASS",
				InfoActivity.MODE_PRIVATE);
		username = sp.getString("Login_name", "");
		// String username="keweibing";
		share = (ImageView) findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				initTencent();
				Intent it = new Intent();
				it.putExtra("videourl",
						"http://202.114.18.96:8080/SouQiuWang/VideoPlay.jsp?videoid="
								+ ID.trim());
				it.putExtra("videotitle", imagetitle);
				it.putExtra("videoid", ID.trim());
				it.putExtra("videotag", videotag);
				it.putExtra("imageurl", arr_tape[0]);
				it.setClass(video_show.this, WXEntryActivity.class);
				startActivity(it);
			}
		});
		surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);
		btnPlayUrl = (ImageView) this.findViewById(R.id.btnPlayUrl);
		btnLarge = (ImageView) this.findViewById(R.id.btnLarge);
		btnpre = (ImageView) this.findViewById(R.id.btnpre);
		btnnext = (ImageView) this.findViewById(R.id.btnnext);
		btnsound = (ImageView) this.findViewById(R.id.sound);
		store = (ImageView) findViewById(R.id.store);
		mData = new ArrayList<Map<String, Object>>();
		playtime = (TextView) findViewById(R.id.time1);
		totalTime = (TextView) findViewById(R.id.time2);
		ltitle = (TextView) findViewById(R.id.ltitle);
		rlayout = (RelativeLayout) findViewById(R.id.relativelayout);
		flayout = (FrameLayout) findViewById(R.id.framelayout);
		reLayout = (RelativeLayout) findViewById(R.id.reLayout);
		reLayout2 = (RelativeLayout) findViewById(R.id.reLayout2);
		circleP = (ProgressBar) findViewById(R.id.progressBar2); // ��ȡԲ�ν����
		circleP.setVisibility(View.GONE);
		ltitle.setText(imagetitle);
		ltitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		ltitle.getPaint().setFakeBoldText(true);// �Ӵ�
		btnpre.setEnabled(false);
		btnnext.setEnabled(false);
		audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabHost.setup(this.getLocalActivityManager());
		LayoutInflater inflater = LayoutInflater.from(this);
		inflater.inflate(R.layout.videoshow_tab1, tabHost.getTabContentView());
		inflater.inflate(R.layout.videoshow_tab2, tabHost.getTabContentView());
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("视频预览")
				.setContent(R.id.r1));
//		if (videotag.equals("1")) { // 注意只有足球才做了这一块
//			Intent m_intent = new Intent(getApplicationContext(),
//					SimilarActivity.class);
//			m_intent.putExtra("id", ID.trim());
//			m_intent.putExtra("title", imagetitle);
//			tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("相关视频")
//					.setContent(m_intent));// 创建一个Activity
//		}
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("相关评论")
				.setContent(R.id.l1));

		c_inflater = getLayoutInflater();
		c_foot = (View) c_inflater.inflate(R.layout.comment_foot, null);
		submit = (Button) findViewById(R.id.bsubmit);
		cachebar = (ProgressBar) findViewById(R.id.cachebar);
		e_content = (EditText) findViewById(R.id.discuss);
		c_lv = (ListView) findViewById(R.id.c_list);
		c_lv.setOnScrollListener(new ScrollListener());
		MyASyncTask yncTask = new MyASyncTask();
		yncTask.execute("0");
		TextView textview = (TextView) findViewById(R.id.textview);
		TextView textview1 = (TextView) findViewById(R.id.textview1);
		textview.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// �Ӵ�
		textview1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		textview.getPaint().setFakeBoldText(true);// �Ӵ�
		textview1.getPaint().setFakeBoldText(true);// �Ӵ�
		gridview = (MyGridview) findViewById(R.id.gridView1); // ��ȡGridView���
		gallery = (Gallery) findViewById(R.id.gallery1); // ��ȡGallery���
		soundseekBar = (VerticalSeekBar) findViewById(R.id.soundseekbar);// ����������
		soundseekBar.setProgress(audiomanager
				.getStreamVolume(AudioManager.STREAM_MUSIC));
		soundseekBar.setMax(audiomanager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));// ������ϵͳ��������Ӧ����
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				circleP.setVisibility(View.VISIBLE);
				new Thread() {
					public void run() {
						// TODO Auto-generated method stub
						videoAddress = new clipSegVideoUrl();
						videoUrl address = new videoUrl();
						String uu = address.getUrl(u);
						Message m = new Message();
						m.what = 2;
						Bundle b = new Bundle();
						b.putString("videourl", uu);
						b.putInt("start", position);
						m.obj = b;
						handler1.sendMessage(m);
					}
				}.start();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int postion, long id) {
				// TODO Auto-generated method stub
				circleP.setVisibility(View.VISIBLE);
				new Thread() {
					public void run() {
						// TODO Auto-generated method stub
						videoAddress = new clipSegVideoUrl();
						videoUrl address = new videoUrl();
						String uu = address.getUrl(u);
						Message m = new Message();
						m.what = 4;
						Bundle b = new Bundle();
						b.putString("videourl", uu);
						b.putInt("start", postion);
						m.obj = b;
						handler1.sendMessage(m);
					}

				}.start();
			}
		});

		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences sp = getSharedPreferences("USERPASS",
						InfoActivity.MODE_PRIVATE);
				int Login_state = sp.getInt("Login_state", 0);
				final String s_name = sp.getString("Login_name", "");// ���ڽ���¼��Ա�����
				if (Login_state == 1) {
					Toast.makeText(video_show.this, "评论成功:", Toast.LENGTH_SHORT)
							.show();
					content = e_content.getText().toString();
					new Thread() {
						public void run() {
							String store_path = "http://202.114.18.96:8080/SouQiuNet/StoreServlet";
							try {
								URL url = new URL(store_path);
								HttpURLConnection conn = (HttpURLConnection) url
										.openConnection();
								conn.setRequestMethod("POST");
								conn.setDoInput(true);
								conn.setDoOutput(true);
								DataOutputStream out = new DataOutputStream(
										conn.getOutputStream());
								String params = "name="
										+ URLEncoder.encode(s_name, "utf-8")
										+ "&videoid="
										+ URLEncoder.encode(ID, "utf-8")
										+ "&videotag="
										+ URLEncoder.encode(videotag, "utf-8")
										+ "&content="
										+ URLEncoder.encode(content, "utf-8")
										+ "&videotitle="
										+ URLEncoder
												.encode(imagetitle, "utf-8");
								out.writeBytes(params);
								out.flush();
								out.close();
								InputStreamReader in = new InputStreamReader(
										conn.getInputStream());
								BufferedReader buff = new BufferedReader(in);
								String line = "";
								while ((line = buff.readLine()) != null) {
									System.out.println("信息：" + line);
								}
								in.close();
								conn.disconnect();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}.start();
					e_content.setText("");// �������Ժ�
					mData.clear();
					MyASyncTask yncTask = new MyASyncTask();
					yncTask.execute("0");
					finish_load = true;
					c_adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(video_show.this, "请先登录再做评论！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		Cursor cursor = dbhelper.getReadableDatabase().rawQuery(
				"select * from videostore where videotag_id = ?",
				new String[] { videotag + ID });
		if (cursor.getCount() > 0) {
			flag6 = true;
			store.setImageResource(R.drawable.likeit);
		} else {
			flag6 = false;
			store.setImageResource(R.drawable.like);
		}

		store.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String videotag_id = videotag + ID;
				if (flag6 == false) {
					store.setImageResource(R.drawable.likeit);
					try {
						dbhelper.getReadableDatabase()
								.execSQL(
										"insert into videostore values(null,?,?,?,?,?)",
										new String[] { videotag_id, u,
												arr_tape[0], imagetitle,
												sdf.format(new Date()) });
						Toast.makeText(video_show.this, "收藏成功",
								Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
					flag6 = true;

				} else {
					store.setImageResource(R.drawable.like);
					dbhelper.getWritableDatabase().delete("videostore",
							"videotag_id like ?", new String[] { videotag_id });
					Toast.makeText(video_show.this, "取消收藏", Toast.LENGTH_SHORT)
							.show();
					flag6 = false;
				}
			}
		});

		surfaceView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					if (player.mediaPlayer.isPlaying()) {// �����Ƶ���ڲ��ŵ�ǰ����
						if (flag4) {
							reLayout.setVisibility(View.INVISIBLE);
							flag4 = false;
						}

						else {
							reLayout.setVisibility(View.VISIBLE);
							flag4 = true;
							handler1.sendEmptyMessageDelayed(0x123, 3000);
						}
					} else if (player.mediaPlayer.isPlaying() == false) { // ���û����
						reLayout.setVisibility(View.VISIBLE);
						i = 0;
						player.mediaPlayer.pause();
						pre = 0;
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
				if (press == 0) {
					player.mediaPlayer.pause();
					btnPlayUrl.setImageResource(R.drawable.play);
					press = 1;
				} else {
					btnPlayUrl.setImageResource(R.drawable.pause);
					press = 0;
					if (!first) {
						player.mediaPlayer.start();

					} else {
						//第一次播放
						circleP.setVisibility(View.VISIBLE);
						first = false;

						new Thread() {
							public void run() {
								// TODO Auto-generated method stub
								videoAddress = new clipSegVideoUrl();
								videoUrl address = new videoUrl();
								//u是从intent中获取的
								String uu = address.getUrl(u);
								if (uu == null || uu.equals("")) {// 这样说明视频地址解析服务器坏了
									first = true;// 别忘了要把这里仍设置是第一次
									Message m = new Message();
									m.what = 5;
									handler1.sendMessage(m);
								} else {
									Message m = new Message();
									m.what = 3;
									m.obj = uu;
									handler1.sendMessage(m);
								}
							}
						}.start();

						// 动作历史记录用于视频流行度的预测,注意mode=1就是播放，mode=2就是分享
						new Thread() {
							public void run() {
								try {

									URL url = new URL(
											"http://202.114.18.96:8080//Prediction//play");// 原来对应的路径为Prediction//prediction,存储的数据格式有变化
									HttpURLConnection conn = (HttpURLConnection) url
											.openConnection();
									conn.setDoOutput(true);
									conn.setDoInput(true);
									conn.setRequestMethod("POST");
									OutputStream out = conn.getOutputStream();
									DataOutputStream obj = new DataOutputStream(
											out);
									String params = "videoid="
											+ URLEncoder.encode(ID.trim(),
													"utf-8") + "&mode="
											+ URLEncoder.encode("1", "utf-8");
									obj.writeBytes(params);
									obj.flush();
									obj.close();
									InputStreamReader in = new InputStreamReader(
											conn.getInputStream());
									BufferedReader buff = new BufferedReader(in);
									String line = "";
									while ((line = buff.readLine()) != null) {
										System.out.println("日期:" + line);
									}

								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}.start();

					}
				}
			}
		});

		btnLarge.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WindowManager wm = (WindowManager) getBaseContext()
						.getSystemService(Context.WINDOW_SERVICE);
				full(!flag3);
				if (flag3 == false) {
					int videoWidth = wm.getDefaultDisplay().getWidth();
					int videoHeight = wm.getDefaultDisplay().getHeight();
					btnLarge.setImageResource(R.drawable.halfscreen);
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					rlayout.setVisibility(RelativeLayout.GONE);
					reLayout2.setVisibility(View.GONE);
					android.view.ViewGroup.LayoutParams l = flayout
							.getLayoutParams();
					l.height = videoWidth;// 高度与宽度互换
					flag3 = true;
				} else {
					btnLarge.setImageResource(R.drawable.fullscreen);
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					android.view.ViewGroup.LayoutParams l = flayout
							.getLayoutParams();
					l.height = playheight;
					rlayout.setVisibility(RelativeLayout.VISIBLE);
					reLayout2.setVisibility(RelativeLayout.VISIBLE);
					flag3 = false;
				}
			}
		});

		btnpre.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (player.mediaPlayer.getCurrentPosition() > 15000) {
					player.mediaPlayer.seekTo(player.mediaPlayer
							.getCurrentPosition() - 15 * 1000);
				} else {
					player.mediaPlayer.seekTo(0);
				}
			}
		});

		btnnext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (player.mediaPlayer.getCurrentPosition() + 15000 > sum * 1000) {
					player.mediaPlayer.seekTo(sum * 1000);
					player.mediaPlayer.pause();
					press = 1;
					btnPlayUrl.setImageResource(R.drawable.play);
				} else {
					player.mediaPlayer.seekTo(player.mediaPlayer
							.getCurrentPosition() + 15 * 1000);
				}
			}
		});

		skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
		skbProgress.setEnabled(false);
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		player = new Player(surfaceView, skbProgress);

		btnsound.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag5 == false) {
					soundseekBar.setVisibility(View.VISIBLE);
					flag5 = true;
				} else {
					soundseekBar.setVisibility(View.GONE);
					flag5 = false;
				}

			}
		});

		soundseekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int Cprogress,
					boolean fromUser) {

				audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,
						Cprogress, 0);
				soundseekBar.setProgress(audiomanager
						.getStreamVolume(AudioManager.STREAM_MUSIC));
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("TAG", "-->onActivityResult " + requestCode + " resultCode="
				+ resultCode);
		if (requestCode == Constants.REQUEST_LOGIN
				|| requestCode == Constants.REQUEST_APPBAR) {
			Tencent.onActivityResultData(requestCode, resultCode, data,
					loginListener);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	IUiListener loginListener = new BaseUiListener() {
		@Override
		protected void doComplete(JSONObject values) {
			Log.d("SDKQQAgentPref",
					"AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
			initOpenidAndToken(values);

		}
	};

	public void initOpenidAndToken(JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(openId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
			}
		} catch (Exception e) {
		}
	}

	protected void initTencent() {// 初始化Tencent
		// TODO Auto-generated method stub
		if (mTencent == null) {
			mTencent = Tencent.createInstance("222222", this);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		player.stop();
		// 用户请求的资源
		new Thread() {

			@Override
			public void run() {
				try {
					URL url = new URL(
							"http://202.114.18.96:8080/SouQiuWang/playInfo");// 将每个用户的播放信息写入到对应的文件里
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.setRequestMethod("POST");
					OutputStream out = conn.getOutputStream();
					DataOutputStream obj = new DataOutputStream(out);
					// mod=1相当于保存该用户的播放历史记录
					String params = "playid="
							+ URLEncoder.encode(ID.trim(), "utf-8") + "&mod="
							+ URLEncoder.encode("1", "utf-8") + "&deviceid="
							+ URLEncoder.encode(DEVICE_ID, "utf-8")
							+ "&username="
							+ URLEncoder.encode(username, "utf-8")
							+ "&playtime="
							+ URLEncoder.encode("" + u_playtime, "utf-8")
							+ "&totaltime="
							+ URLEncoder.encode("" + sum, "utf-8");
					obj.writeBytes(params);
					obj.flush();
					obj.close();
					InputStreamReader in = new InputStreamReader(
							conn.getInputStream());
					BufferedReader buff = new BufferedReader(in);
					String line = "";
					while ((line = buff.readLine()) != null) {
						System.out.println("播放信息：" + line);
					}
					in.close();
					conn.disconnect();

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		}.start();

	}

	// 用户名:kwb设备:A0000044257213视频id:43640播放时间:6

	private Handler handler1 = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			int iii = 0;
			int sum_ = 0;
			Bundle b;
			int t = 0;
			int BegTime = 0;
			int g_totaltime = 0;
			int postion = 0;
			String uu;// ���϶�����ʱ����
			switch (msg.what) {
			case 0x123:
				reLayout.setVisibility(View.INVISIBLE);
				flag4 = false;
				break;
			case 1:
				cachebar.setVisibility(View.GONE);
				gallery.setAdapter(new GalleryAdapter(video_show.this,
						arr_preview, gallery));
				gallery.setSelection(1);
				gridview.setAdapter(new JingCaiAdapter(video_show.this,
						arr_tape, arr_totaltime, gridview));
				break;
			case 2:
				iii = 0;// �����ж�gallery�����Ƕε�ַ
				b = (Bundle) msg.obj;
				uu = b.getString("videourl");
				postion = b.getInt("start");
				sum_ = 0;// ����ʱ��sum_���Ƹ�sum
				BegTime = 0;// �����ж�gallery�п�ʼʱ��
				linkedlist = videoAddress.clipAdrress(uu);
				videolist = linkedlist.getFirst();// ���»�ȡ�õ��ֶε�ַ��
				timelist = linkedlist.getLast();// ���µõ�ʱ��δ�С��
				for (int k = 0; k < timelist.size(); k++) {

					sum_ += Integer.parseInt(timelist.get(k));// ���¼����ε�ַ����ʱ��

				}
				totalTime.setText(fun.Fun(sum_));
				sum = sum_;
				t = (int) (Float.parseFloat(arr_Pstarttime[postion]));

				g_totaltime = 0;// ���ڱȽϴӶ��ȡʱ�Ķε�ַ
				for (iii = 0; iii < timelist.size() - 1; iii++)// ע������һ��Ҫע��߽����������<n-1����Ȼ��ʱ���һ��ʱ�ᷢ��Խ�����
				{
					g_totaltime += Integer.parseInt(timelist.get(iii));
					if (g_totaltime > t)
						break;
					else {
						BegTime += Integer.parseInt(timelist.get(iii));// ���������Ϊ��ֱ��seek��׼��
					}
				}
				player.playUrl_2(iii, t - BegTime, t);

				btnpre.setEnabled(true);
				btnnext.setEnabled(true);
				skbProgress.setEnabled(true);
				btnPlayUrl.setImageResource(R.drawable.pause);
				break;
			case 3:
				/****** ���²��ź�Ҫ��ʼ��sum��pre��i��videoaddress *****/
				sum = 0;
				pre = 0;
				i = 0;
				player.Url((String) msg.obj);// ���ø÷���

				for (int k = 0; k < timelist.size(); k++) {
					sum += Integer.parseInt(timelist.get(k));
				}
				totalTime.setText(fun.Fun(sum));
				btnpre.setEnabled(true);
				btnnext.setEnabled(true);
				skbProgress.setEnabled(true);
				Toast.makeText(getApplicationContext(),
						"加载完成" + timelist.size(), Toast.LENGTH_LONG).show();

				break;
			case 4:
				iii = 0;// �����ж�grideview�����ַ
				b = (Bundle) msg.obj;
				uu = b.getString("videourl");
				postion = b.getInt("start");
				sum_ = 0;// ����ʱ��sum_���Ƹ�sum
				linkedlist = videoAddress.clipAdrress(uu);
				videolist = linkedlist.getFirst();// ���»�ȡ�õ��ֶε�ַ��
				timelist = linkedlist.getLast();// ���µõ�ʱ��δ�С��
				for (int k = 0; k < timelist.size(); k++) {

					sum_ += Integer.parseInt(timelist.get(k));// ���¼����ε�ַ����ʱ��
				}
				totalTime.setText(fun.Fun(sum_));
				sum = sum_;
				t = (int) (Float.parseFloat(arr_starttime[postion]));
				BegTime = 0;// �����ж�gallery�п�ʼʱ��
				g_totaltime = 0;// ���ڱȽϴӶ��ȡʱ�Ķε�ַ
				for (iii = 0; iii < timelist.size() - 1; iii++)// ע������һ��Ҫע��߽����������<n-1����Ȼ��ʱ���һ��ʱ�ᷢ��Խ�����
				{
					g_totaltime += Integer.parseInt(timelist.get(iii));
					if (g_totaltime > t)
						break;
					else {
						BegTime += Integer.parseInt(timelist.get(iii));
					}
				}
				player.playUrl_2(iii, t - BegTime, t);
				btnpre.setEnabled(true);
				btnnext.setEnabled(true);
				skbProgress.setEnabled(true);
				btnPlayUrl.setImageResource(R.drawable.pause);
				break;
			case 5:
				Toast.makeText(getApplicationContext(), "视频地址解析服务器出现异常",
						Toast.LENGTH_LONG).show();
				circleP.setVisibility(View.INVISIBLE);
				btnPlayUrl.setImageResource(R.drawable.play);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * ��̬����״̬������ʾ
	 * 
	 * @param enable
	 */
	private void full(boolean enable) {
		if (enable) {
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(lp);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams attr = getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setAttributes(attr);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int curprogress = 0; // Ҫ����Ϊȫ�ֱ��������

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

			curprogress = (progress * sum / seekBar.getMax() - pre) * 1000;// /������ʱ��-ǰ��Ķ��ʱ��
			// *******�������Ϊ���߸��϶�ʱ���ڼ��ص������Ƶ�У���ֱ��seekto**********
			if (fromUser == false
					|| (progress >= 100 * pre / sum)
					&& (progress < 100
							* (pre * 1000 + player.mediaPlayer.getDuration())
							/ (sum * 1000)))
				flag = false;
			else {
				ArrayList<Integer> percentlist = new ArrayList<Integer>();
				percentlist = videoAddress.getPercentage();// Ӧ�÷ŵ�ǰ��ȥ����
				for (jj = 0; jj < percentlist.size(); jj++) {
					if (progress < percentlist.get(jj))
						break;
				}
				flag = true;

			}
			// Log.i("tag","flag:"+flag);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			circleP.setVisibility(View.VISIBLE);
			if (flag == false) {
				player.mediaPlayer.seekTo(curprogress);// ���ŵ�ʱ��λ��
			} else {
				if (jj < videolist.size())
					player.playUrl_(videolist.get(jj), jj);
			}
		}
	}

	class Player implements OnBufferingUpdateListener, OnPreparedListener,
			OnCompletionListener, SurfaceHolder.Callback {

		int begningtime = 0;
		int videoWidth, videoHeight;
		public MediaPlayer mediaPlayer;
		private SurfaceHolder surfaceHolder;
		private SeekBar skbProgress;
		private ProgressBar circlp;
		int kk = 0;

		Timer mTimer = new Timer();

		public Player(SurfaceView surfaceView, SeekBar skbProgress) {
			this.skbProgress = skbProgress;
			surfaceHolder = surfaceView.getHolder();
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			mTimer.schedule(mTimerTask, 0, 1000);

		}

		/************* * ͨ��ʱ����Handler�����½���� ********/
		TimerTask mTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (mediaPlayer == null)
					return;
				if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
					System.out.println("播放时间：" + u_playtime++);

					handleProgress.sendEmptyMessage(0);
				}
			}
		};
		Handler handleProgress = new Handler() {

			public void handleMessage(Message msg) {
				int position = mediaPlayer.getCurrentPosition();// ����ʱ��
				/********* �������Ϊ���߸��϶�ʱ���ڼ��ص������Ƶ�У���ֱ��seekto ************/
				if (flag == false) {
					// Log.i("tag", "i:"+i+" kk: "+kk);
					playtime.setText(fun.Fun(pre + position / 1000));// ��ݲ��Ž��������ʱ��
					if (i == 1) {
						playtime.setText(fun.Fun(pre + position / 1000));// ��ݲ��Ž��������ʱ��
						// Log.i("tag",""+
						// "pre1: "+pre+"position1: "+position/1000);
					} else if (i > 1) {
						for (; kk < i - 1; kk++) {
							pre += Integer.parseInt(timelist.get(kk));// �����ۼӼ���öε�ַ����Ƶ�е����ʱ��
						}
						position = pre * 1000
								+ mediaPlayer.getCurrentPosition(); // �ܲ���ʱ��ת��Ϊ��
						// Log.i("tag",""+
						// " pre2:"+pre+" position2:"+position/1000);
						playtime.setText(fun.Fun(position / 1000));// ����Ĳ���Ӧ��Ϊ��
					}

				}
				/********* �����Ϊ�Ҹ��϶�ʱ�䲻�ڼ��ص������Ƶ�У������� ************/
				else if (flag == true) {
					pre = 0;// ���¸���Ϊ0
					if (jj == 0) {
						playtime.setText(fun.Fun(position / 1000));
						Log.i("tag", "position" + position / 1000);
					} else if (jj > 0) {
						for (kk = 0; kk < i - 1; kk++) {
							pre += Integer.parseInt(timelist.get(kk));// �����ۼӼ���öε�ַ����Ƶ�е����ʱ��
						}
						position = pre * 1000
								+ mediaPlayer.getCurrentPosition(); // �ܲ���ʱ��ת��Ϊ��
						Log.i("tag", "" + "pre3: " + pre + " position3 :"
								+ position / 1000);
						playtime.setText(fun.Fun(position / 1000));// ����Ĳ���Ӧ��Ϊ��
					}
				}
				int duration = mediaPlayer.getDuration();
				if (duration > 0) {
					long pos = skbProgress.getMax() * position / (sum * 1000);// ��ռ�İٷֱ�
					skbProgress.setProgress((int) pos);
				}
			}
		};

		// *****************************************************
		public void play() {
			mediaPlayer.start();
		}

		public void Url(String str) {
			linkedlist = videoAddress.clipAdrress(str);
			videolist = linkedlist.getFirst();// �õ��ֶε�ַ��
			timelist = linkedlist.getLast();// �õ�ʱ��δ�С��
			begningtime = 0;// ���㿪ʼ ��lһ���⣬��Ȼ�´ε�play��ťʱ������0��ʼ
			String videotag_id = videotag + ID;// ����Ƚ������videotag��id�����һ�𹹳�һ���ֶΣ�����sqlitedatabase��ֻ��idΪ��������ԲŻ�������
			try {
				dbhelper.getReadableDatabase().execSQL(
						"insert into videoplay values(null,?,?,?,?,?)",
						new String[] { videotag_id, u, arr_tape[0], imagetitle,
								sdf.format(new Date()) });

			} catch (Exception e) {// ����ס�쳣��

				e.printStackTrace();
			}

			playUrl(videolist.get(0));
		}

		public void playUrl(String str) { // ע��onComplition()ִ�����,��ת�����
			try {
				i++;
				mediaPlayer.setDataSource(str);
				mediaPlayer.prepareAsync();

			} catch (Exception e) {

			}
		}

		public void playUrl_(String str, int segment) {
			try {

				mediaPlayer.reset();// ����
				mediaPlayer.setDataSource(str);
				mediaPlayer.prepare();// �Զ�����
				i = segment + 1;
			} catch (Exception e) {
			}
		}

		public void playUrl_2(int segment, int start, int total_) {
			begningtime = start;// ��ָ��λ�ÿ�ʼ
			if (segment == kk) {
				System.out.println("00000000000: " + segment);
				pre = total_ - start;
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.seekTo(begningtime * 1000);// ���պü��ص��ǵ�ǰ�ص�ַ�����ڸ÷�Χ��ֱ����ת
				} else {// ���ʱû�в���

					try {
						Log.i("tag", "1111111111111: " + segment);
						mediaPlayer.reset();//
						mediaPlayer.setDataSource(videolist.get(segment));
						i = segment + 1;
						pre = 0;// /////////����Ҳ����䣬��Ϊ����gallery������һ��pre���������seekbar�ֻ���һ�飬���Ե���0����Ȼ��׼
						kk = 0;// ////////gridview �������������
						mediaPlayer.prepare();// �Զ�����

					} catch (Exception e) {
					}
				}
			}// �����صĲ��ǵ�ǰ�أ���ô����mediapayer
			else {
				try {
					Log.i("tag", "22222222222222222222: " + segment);
					mediaPlayer.reset();//
					mediaPlayer.setDataSource(videolist.get(segment));
					i = segment + 1;
					pre = 0;// /////////����Ҳ����䣬��Ϊ����gallery������һ��pre���������seekbar�ֻ���һ�飬���Ե���0����Ȼ��׼
					kk = 0;// ////////gridview �������������
					mediaPlayer.prepare();// �Զ�����

				} catch (Exception e) {
				}
			}
		}

		public void stop() {

			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			Log.e("mediaPlayer", "surface changed");

		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			try {

				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDisplay(arg0);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setOnBufferingUpdateListener(this);
				mediaPlayer.setOnPreparedListener(this);
				mediaPlayer.setOnCompletionListener(this);
				playheight = surfaceView.getMeasuredHeight();
			} catch (Exception e) {
				Log.e("mediaPlayer", "error", e);

			}
			Log.e("mediaPlayer", "surface created");
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			Log.i("tag", "surface destroyed");

		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			Log.e("tag", "prepare");
			mp.start();
			mp.seekTo(begningtime * 1000);
			handler1.sendEmptyMessageDelayed(0x123, 4000);

		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			Log.e("tag", "completeplay��" + i);
			if (i < videolist.size()) {
				if (mp.getCurrentPosition() >= mp.getDuration()) {
					playUrl(videolist.get(i));
				}
			}
		}

		@Override
		public void onBufferingUpdate(MediaPlayer mediaPlayer,
				int bufferingProgress) {

			System.out.println("BufferingProgress:  " + bufferingProgress);
			skbProgress.setSecondaryProgress(bufferingProgress
					* mediaPlayer.getDuration() / (1000 * sum) + pre / sum
					* 100);
			int currentProgress = skbProgress.getMax()
					* mediaPlayer.getCurrentPosition()
					/ mediaPlayer.getDuration();

			if (mediaPlayer.getCurrentPosition() * 100
					/ mediaPlayer.getDuration() + 2 < bufferingProgress)
				circleP.setVisibility(View.GONE);
			if (flag2 == true)// �����ͣ��
				circleP.setVisibility(View.GONE);
		}
	}

	public List<Map<String, Object>> getData(int startposition, int pagesize) {
		System.out.println("��ʼ��" + startposition);
		vc = new VideoComment(ID, videotag, startposition);// ���ڴ���ݿ��ж�����Ƶ������Ϣ
		vc.getCommentMessage();
		c_mListTitle = vc.com_name;
		c_mListStr = vc.com_content;
		c_timelist = vc.com_time;
		c_total = vc.total;// ��������
		maxpage = c_total / pagesize + 1;// ���ҳ��
		ArrayList<Map<String, Object>> c_list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < pagesize && i < c_timelist.length; i++) {
			// ע������Ҫ�к������������Ȼ���û����ҳ�������Խ�����
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", c_mListTitle[i]);
			item.put("time", c_timelist[i]);
			item.put("text", c_mListStr[i]);
			c_list.add(item);
		}
		return c_list;
	}

	private class MyASyncTask extends
			AsyncTask<String, Integer, ArrayList<Map<String, Object>>> {
		protected ArrayList<Map<String, Object>> doInBackground(String... param) {
			vc = new VideoComment(ID, videotag, Integer.valueOf(param[0]));// ���ڴ���ݿ��ж�����Ƶ������Ϣ
			vc.getCommentMessage();
			c_mListTitle = vc.com_name;
			c_mListStr = vc.com_content;
			c_timelist = vc.com_time;
			c_total = vc.total;// ��������
			maxpage = c_total / 4 + 1;// ���ҳ��(����4������pagesize)
			ArrayList<Map<String, Object>> c_list = new ArrayList<Map<String, Object>>();
			if (c_timelist == null) // 因为当前为null，时不存在.length
				return c_list;
			for (int i = 0; i < 4 && i < c_timelist.length; i++) {

				Map<String, Object> item = new HashMap<String, Object>();
				item.put("name", c_mListTitle[i]);
				item.put("time", c_timelist[i]);
				item.put("text", c_mListStr[i]);
				c_list.add(item);
			}
			return c_list;
		}

		protected void onPostExecute(ArrayList<Map<String, Object>> Result) {
			mData.addAll(Result);
			c_adapter = new SimpleAdapter(video_show.this, mData,
					R.layout.comment_list, new String[] { "name", "time",
							"text" }, new int[] { R.id.c_name, R.id.c_time,
							R.id.c_content });
			c_lv.addFooterView(c_foot);
			c_lv.setAdapter(c_adapter);
			c_lv.removeFooterView(c_foot);
		}
	}

	public class ScrollListener implements OnScrollListener {
		int pagesize = 4;// ÿҳ��ʾ��Ŀ
		int currentpage;// ��ǰҳ
		int nextpage;

		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			final int total = totalItemCount;

			System.out.println("total" + total);
			if (c_lv.getLastVisiblePosition() + 1 == totalItemCount) {
				if (totalItemCount > 0) {

					currentpage = totalItemCount % pagesize == 0 ? totalItemCount
							/ pagesize
							: totalItemCount / pagesize + 1;
					nextpage = currentpage + 1;

					if (nextpage <= maxpage && finish_load) {
						finish_load = false;
						c_lv.addFooterView(c_foot);
						new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								List<Map<String, Object>> l = getData(total,
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
				mData.addAll((List<Map<String, Object>>) msg.obj);
				c_adapter.notifyDataSetChanged();
				/* ҳ����ʾ���ɾ�� */
				if (c_lv.getFooterViewsCount() > 0)// ����0��ʾ��footview
					c_lv.removeFooterView(c_foot);
				finish_load = true;
			};
		};
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			if (null == response) {
				Util.showResultDialog(video_show.this, "返回为空", "登录失败");
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				Util.showResultDialog(video_show.this, "返回为空", "登录失败");
				return;
			}
			Util.showResultDialog(video_show.this, response.toString(), "登录成功");

			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {
		}

		@Override
		public void onError(UiError e) {
			Util.toastMessage(video_show.this, "onError: " + e.errorDetail);
			Util.dismissDialog();
		}

		@Override
		public void onCancel() {
			Util.toastMessage(video_show.this, "onCancel: ");
			Util.dismissDialog();

		}
	}
}
