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

	private ArrayList<OneProgramInfo> programInfos;   //��Ŀ��
	private int index[]; // listView��������ʾ��Ŀ��Ϣ
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

		// ������Ŀ��Ϣ
		new searchProgramTask().execute((Void) null);

		// ���ڰ�ť��Ӧ�¼�
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

		// ��ǰ���Ӻ�ʱ��
		java.text.DateFormat df = new java.text.SimpleDateFormat(
				"yyyy��MM��dd�� E ");
		String curTime = df.format(new Date());
		channel_time_week.setText(it.getStringExtra("channelName") + " " + curTime);                                  /////*

		// ���ڰ�ť����
//		String week = curTime.substring(curTime.indexOf("��"),
//				curTime.indexOf("��") + 2);
//		week = "����" + week.substring(1, 2); // ����һ�����ڶ�......
		String week=curTime.substring(curTime.indexOf("����"),curTime.length());                //kwb�޸ĺ�
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
		String Program[]; // ʱ��ͽ�Ŀ�� һ��    00:00@���Ǹ���@@
		String oneProgram = "";
		String oneLine="";

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(TVProgramInfo.this);
			progressDialog.setMessage("���ڲ�ѯ�����Ժ�...");
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
				resultStr = HttpDownloadTxt.download(xmlUrl); // �õ���Ŀ����txt�ļ�
				
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
			 * ����txt�ļ�
			 */
			if (!resultStr.equals("")) {

				Program = resultStr.split("\n");

				// ���������� 
				//ǰ5��ΪƵ����Ϣ���Ŀ�޹�
				for (int i = 5, j = 1; i < Program.length; i++) {
					oneLine = Program[i];
					if (oneLine.contains("$"))
						{
						  index[j++] = ++i; // ����j �����					
						}
				}
				// ����Ŀ�ŵ���Ŀ��programInfos��
				//ǰ5��ΪƵ����Ϣ���Ŀ�޹�
				for (int i = 5, j = 1, k = 5; i < Program.length; i++) // index[1]����ǰ��
				{
					if (i == index[j]) { // �������ڱ�ʶ
						index[j] = index[j] - k++;// �����ں�����ǰ��kλ,��ʱ����index[j]��¼programInfos������j����ʼ��Ŀ���
						j++;
						continue; // ���ڲ�����
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
					oneProgram = Program[i].trim(); //15:00@��������2(4)@@		
					int iTemp = oneProgram.indexOf("@");//�ָ���
					
					oneProgramInfo = new OneProgramInfo(); //�������new
					oneProgramInfo.setPlaytime(oneProgram.substring(0,iTemp)); // 06:45

					oneProgramInfo.setTitle(oneProgram.substring(iTemp+1,oneProgram.indexOf("@", iTemp+1)));
					
					programInfos.add(oneProgramInfo);
					//System.out.println(oneProgramInfo.toString());

				}
				index[8] = programInfos.size(); // ����������ս�Ŀ��
				
				
//				System.out.println("Week="+weekday);
//				for(int i=1;i<=7;i++){
//					System.out.println("index"+i+"="+index[i]);
//				}
				

				/**
				 * ��Ŀ��ʾ
				 */
				adapter = new TVOrderAdapter(programInfos, TVProgramInfo.this);
				listView.setAdapter(adapter);
			}// resultStr!=""
			super.onPostExecute(result);
		}
	}

	/**
	 * 
	 * ******ListView����֮�Զ���������
	 * 
	 */
	private class TVOrderAdapter extends BaseAdapter {

		private ArrayList<OneProgramInfo> programInfos;// �����Ƶ��
		private Context context = null;
		// View����Ļ��棬rowViews
		private Map<Integer, View> rowViews = new HashMap<Integer, View>(); // �ܳ��ı��,�洢���е�view

		// ���캯��
		public TVOrderAdapter(ArrayList<OneProgramInfo> programInfoss, Context context) {
			this.programInfos = programInfoss;
			this.context = context;
		}

		// ���ص�ǰAdapter���еĶ����������Item��,��ListView�ĳ���
		public int getCount() {
			// return Resultlist.size();
			// ��ҳ
			return index[weekday + 1] - index[weekday];

		}

		// ����λ�õõ���Ӧ��Item����
		public Object getItem(int position) {
			return programInfos.get(position);
		}

		// ����λ�õõ���Ӧ�����ID
		public long getItemId(int position) {
			return position;
		}

		// ListViewͨ������getView()�����õ���Ӧ��View����
		public View getView(int position, View convertView, ViewGroup parent) {

			int tempPosition;
			tempPosition = position + index[weekday]; // position=0,1,2.....

			View rowView = rowViews.get(tempPosition);

			if (rowView == null) {

				// ����һ��layoutinflater����
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				// ����LayoutInflater��inflate��������һ��View�����������listview��item����
				rowView = layoutInflater.inflate(R.layout.one_order_program,
						null);

				// �õ�View���еĿؼ�
				TextView tvPlayTime = (TextView) rowView
						.findViewById(R.id.one_program_channel_time);
				TextView tvTitle = (TextView) rowView
						.findViewById(R.id.one_program_channel_title);

				// ����getItem()�õ���Ӧλ�õ�OneProgramInfo����
				OneProgramInfo oneProgramInfo = (OneProgramInfo) getItem(tempPosition);                                      /////?

				tvPlayTime.setText(oneProgramInfo.getPlaytime());
				tvTitle.setText(oneProgramInfo.getTitle());
				
			//	System.out.println("Program "+tempPosition+":"+oneProgramInfo.getPlaytime()+oneProgramInfo.getTitle());

				// �����ɵ�View��ӵ�Map��ȥ
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

