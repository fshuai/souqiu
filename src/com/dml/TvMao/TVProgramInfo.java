package com.dml.TvMao;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dml.souqiu.R;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TVProgramInfo extends Activity {

	private ArrayList<OneProgramInfo> programInfos;   //节目表
	private int index[]; // listView按星期显示节目信息
	private int weekday; // index[weekday]
	private ListView listView;
	private TVOrderAdapter adapter;
	private ProgressDialog progressDialog = null;

	private TextView channel_time_week;
	private Button Mon;
	private Button Tues;
	private Button Wed;
	private Button Thur;
	private Button Fri;
	private Button Sat;
	private Button Sun;
	Intent it;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.program_info);

		channel_time_week = (TextView) findViewById(R.id.channel_time_week);
		Mon = (Button) findViewById(R.id.Mon);
		Tues = (Button) findViewById(R.id.Tues);
		Wed = (Button) findViewById(R.id.Wed);
		Thur = (Button) findViewById(R.id.Thur);
		Fri = (Button) findViewById(R.id.Fri);
		Sat = (Button) findViewById(R.id.Sat);
		Sun = (Button) findViewById(R.id.Sun);
		listView = (ListView) this.findViewById(R.id.TVProgramInfo);

		it = getIntent();
		Init();

		// 搜索节目信息
		new searchProgramTask().execute((Void) null);

		// 星期按钮响应事件
		View.OnClickListener listener = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.Mon:
					Monday();
					break;
				case R.id.Tues:
					Tuesday();
					break;
				case R.id.Wed:
					Wednesday();
					break;
				case R.id.Thur:
					Thursday();
					break;
				case R.id.Fri:
					Friday();
					break;
				case R.id.Sat:
					Saturday();
					break;
				case R.id.Sun:
					Sunday();
					break;
				}
			}
		};

		Mon.setOnClickListener(listener);
		Tues.setOnClickListener(listener);
		Wed.setOnClickListener(listener);
		Thur.setOnClickListener(listener);
		Fri.setOnClickListener(listener);
		Sat.setOnClickListener(listener);
		Sun.setOnClickListener(listener);
	}

	private void Init() {

		// 当前卫视和时间
		java.text.DateFormat df = new java.text.SimpleDateFormat(
				"yyyy年MM月dd日 E ");
		String curTime = df.format(new Date());
		channel_time_week.setText(it.getStringExtra("channelName") + " " + curTime);                                  /////*

		// 星期按钮标亮
//		String week = curTime.substring(curTime.indexOf("周"),
//				curTime.indexOf("周") + 2);
//		week = "星期" + week.substring(1, 2); // 星期一，星期二......
		String week=curTime.substring(curTime.indexOf("星期"),curTime.length());                //kwb修改后
		if (Mon.getText().equals(week)) {
			Mon.setTextColor(android.graphics.Color.BLUE);
			weekday = 1;
		} else if (Tues.getText().equals(week)) {
			Tues.setTextColor(android.graphics.Color.BLUE);
			weekday = 2;
		} else if (Wed.getText().equals(week)) {
			Wed.setTextColor(android.graphics.Color.BLUE);
			weekday = 3;
			Wednesday();
		} else if (Thur.getText().equals(week)) {
			Thur.setTextColor(android.graphics.Color.BLUE);
			weekday = 4;
		} else if (Fri.getText().equals(week)) {
			Fri.setTextColor(android.graphics.Color.BLUE);
			weekday = 5;
		} else if (Sat.getText().equals(week)) {
			Sat.setTextColor(android.graphics.Color.BLUE);
			weekday = 6;
		} else if (Sun.getText().equals(week)) {
			Sun.setTextColor(android.graphics.Color.BLUE);
			weekday = 7;
		}

		programInfos = new ArrayList<OneProgramInfo>();
		index = new int[10];
       
	}

	private class searchProgramTask extends AsyncTask<Void, Void, Integer> {
		String resultStr = "";
		OneProgramInfo oneProgramInfo;
		String Program[]; // 时间和节目名 一起    00:00@我是歌手@@
		String oneProgram = "";
		String oneLine="";

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(TVProgramInfo.this);
			progressDialog.setMessage("正在查询，请稍候...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			String url = "";
			String xmlUrl = "";

			try {
				url = "http://202.114.18.76/GetProgram.aspx?key="
						+ GBEncode.formatUrl(it.getStringExtra("channelKey"))
						+ "&way=a";                                                                                     /////*
				                                                                                                   
               // System.out.println("channelKey:"+it.getStringExtra("channelKey"));
                
				xmlUrl = HttpDownload.download(url);
				
				System.out.println("first xmlUrl:"+xmlUrl);
				
				
				xmlUrl = xmlUrl.substring(0, xmlUrl.indexOf(".txt") + 4);

				String keyword = xmlUrl.substring(xmlUrl.lastIndexOf("\\") + 1,
						xmlUrl.indexOf(".txt"));                                                             ////*

				xmlUrl = xmlUrl.replace(keyword, GBEncode.formatUrl(keyword));                                          //////?
				resultStr = HttpDownloadTxt.download(xmlUrl); // 得到节目单的txt文件
				
				System.out.println("second xmlUrl:"+resultStr);

			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				Log.e("TVOrder", e1.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			progressDialog.dismiss();
			/**
			 * 解析txt文件
			 */
			if (!resultStr.equals("")) {

				Program = resultStr.split("\n");

				// 找星期索引 
				//前5行为频道信息与节目无关
				for (int i = 5, j = 1; i < Program.length; i++) {
					oneLine = Program[i];
					if (oneLine.contains("$"))
						{
						  index[j++] = ++i; // 星期j 的序号					
						}
				}
				// 将节目放到节目表programInfos中
				//前5行为频道信息与节目无关
				for (int i = 5, j = 1, k = 5; i < Program.length; i++) // index[1]不用前移
				{
					if (i == index[j]) { // 遇到星期标识
						index[j] = index[j] - k++;// 除星期后，索引前移k位,此时索引index[j]记录programInfos中星期j的起始节目序号
						j++;
						continue; // 星期不加入
					}
					if(Program[i].contains("$"))
					{
						k++;
						continue;
					}
					if(Program[i].contains("&"))
					{
						k++;
						continue;
					}
					oneProgram = Program[i].trim(); //15:00@百万新娘2(4)@@		
					int iTemp = oneProgram.indexOf("@");//分隔符
					
					oneProgramInfo = new OneProgramInfo(); //务必重新new
					oneProgramInfo.setPlaytime(oneProgram.substring(0,iTemp)); // 06:45

					oneProgramInfo.setTitle(oneProgram.substring(iTemp+1,oneProgram.indexOf("@", iTemp+1)));
					
					programInfos.add(oneProgramInfo);
					//System.out.println(oneProgramInfo.toString());

				}
				index[8] = programInfos.size(); // 方便计算周日节目数
				
				
//				System.out.println("Week="+weekday);
//				for(int i=1;i<=7;i++){
//					System.out.println("index"+i+"="+index[i]);
//				}
				

				/**
				 * 节目显示
				 */
				adapter = new TVOrderAdapter(programInfos, TVProgramInfo.this);
				listView.setAdapter(adapter);
			}// resultStr!=""
			super.onPostExecute(result);
		}
	}

	/**
	 * 
	 * ******ListView布局之自定义适配器
	 * 
	 */
	private class TVOrderAdapter extends BaseAdapter {

		private ArrayList<OneProgramInfo> programInfos;// 结果视频集
		private Context context = null;
		// View对象的缓存，rowViews
		private Map<Integer, View> rowViews = new HashMap<Integer, View>(); // 很长的表格,存储所有的view

		// 构造函数
		public TVOrderAdapter(ArrayList<OneProgramInfo> programInfoss, Context context) {
			this.programInfos = programInfoss;
			this.context = context;
		}

		// 返回当前Adapter当中的对象个数，即Item数,即ListView的长度
		public int getCount() {
			// return Resultlist.size();
			// 分页
			return index[weekday + 1] - index[weekday];

		}

		// 根据位置得到相应的Item对象
		public Object getItem(int position) {
			return programInfos.get(position);
		}

		// 根据位置得到相应对象的ID
		public long getItemId(int position) {
			return position;
		}

		// ListView通过调用getView()方法得到相应的View对象
		public View getView(int position, View convertView, ViewGroup parent) {

			int tempPosition;
			tempPosition = position + index[weekday]; // position=0,1,2.....

			View rowView = rowViews.get(tempPosition);

			if (rowView == null) {

				// 生成一个layoutinflater对象
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				// 调用LayoutInflater的inflate方法生成一个View对象，用于填充listview的item对象
				rowView = layoutInflater.inflate(R.layout.one_order_program,
						null);

				// 得到View当中的控件
				TextView tvPlayTime = (TextView) rowView
						.findViewById(R.id.one_program_channel_time);
				TextView tvTitle = (TextView) rowView
						.findViewById(R.id.one_program_channel_title);

				// 调用getItem()得到对应位置的OneProgramInfo对象
				OneProgramInfo oneProgramInfo = (OneProgramInfo) getItem(tempPosition);                                      /////?

				tvPlayTime.setText(oneProgramInfo.getPlaytime());
				tvTitle.setText(oneProgramInfo.getTitle());
				
			//	System.out.println("Program "+tempPosition+":"+oneProgramInfo.getPlaytime()+oneProgramInfo.getTitle());

				// 将生成的View添加到Map中去
				rowViews.put(tempPosition, rowView);
			}
			return rowView;
		}
	} // TVOrderAdapter

	void Monday() {
		weekday = 1;
		Mon.setTextColor(android.graphics.Color.BLUE);
		Tues.setTextColor(android.graphics.Color.BLACK);
		Wed.setTextColor(android.graphics.Color.BLACK);
		Thur.setTextColor(android.graphics.Color.BLACK);
		Fri.setTextColor(android.graphics.Color.BLACK);
		Sat.setTextColor(android.graphics.Color.BLACK);
		Sun.setTextColor(android.graphics.Color.BLACK);
		adapter.notifyDataSetChanged();
	}

	void Tuesday() {
		weekday = 2;
		Mon.setTextColor(android.graphics.Color.BLACK);
		Tues.setTextColor(android.graphics.Color.BLUE);
		Wed.setTextColor(android.graphics.Color.BLACK);
		Thur.setTextColor(android.graphics.Color.BLACK);
		Fri.setTextColor(android.graphics.Color.BLACK);
		Sat.setTextColor(android.graphics.Color.BLACK);
		Sun.setTextColor(android.graphics.Color.BLACK);
		adapter.notifyDataSetChanged();
	}

	void Wednesday() {
		weekday = 3;
		Mon.setTextColor(android.graphics.Color.BLACK);
		Tues.setTextColor(android.graphics.Color.BLACK);
		Wed.setTextColor(android.graphics.Color.BLUE);
		Thur.setTextColor(android.graphics.Color.BLACK);
		Fri.setTextColor(android.graphics.Color.BLACK);
		Sat.setTextColor(android.graphics.Color.BLACK);
		Sun.setTextColor(android.graphics.Color.BLACK);
		adapter.notifyDataSetChanged();
	}

	void Thursday() {
		weekday = 4;
		Mon.setTextColor(android.graphics.Color.BLACK);
		Tues.setTextColor(android.graphics.Color.BLACK);
		Wed.setTextColor(android.graphics.Color.BLACK);
		Thur.setTextColor(android.graphics.Color.BLUE);
		Fri.setTextColor(android.graphics.Color.BLACK);
		Sat.setTextColor(android.graphics.Color.BLACK);
		Sun.setTextColor(android.graphics.Color.BLACK);
		adapter.notifyDataSetChanged();
	}

	void Friday() {
		weekday = 5;
		Mon.setTextColor(android.graphics.Color.BLACK);
		Tues.setTextColor(android.graphics.Color.BLACK);
		Wed.setTextColor(android.graphics.Color.BLACK);
		Thur.setTextColor(android.graphics.Color.BLACK);
		Fri.setTextColor(android.graphics.Color.BLUE);
		Sat.setTextColor(android.graphics.Color.BLACK);
		Sun.setTextColor(android.graphics.Color.BLACK);
		adapter.notifyDataSetChanged();
	}

	void Saturday() {
		weekday = 6;
		Mon.setTextColor(android.graphics.Color.BLACK);
		Tues.setTextColor(android.graphics.Color.BLACK);
		Wed.setTextColor(android.graphics.Color.BLACK);
		Thur.setTextColor(android.graphics.Color.BLACK);
		Fri.setTextColor(android.graphics.Color.BLACK);
		Sat.setTextColor(android.graphics.Color.BLUE);
		Sun.setTextColor(android.graphics.Color.BLACK);
		adapter.notifyDataSetChanged();
	}

	void Sunday() {
		weekday = 7;
		Mon.setTextColor(android.graphics.Color.BLACK);
		Tues.setTextColor(android.graphics.Color.BLACK);
		Wed.setTextColor(android.graphics.Color.BLACK);
		Thur.setTextColor(android.graphics.Color.BLACK);
		Fri.setTextColor(android.graphics.Color.BLACK);
		Sat.setTextColor(android.graphics.Color.BLACK);
		Sun.setTextColor(android.graphics.Color.BLUE);
		adapter.notifyDataSetChanged();
	}
}

