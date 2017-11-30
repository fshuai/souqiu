package com.dml.TvMao;

import java.util.Date;
import java.util.List;




import com.dml.souqiu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TVCurrent extends Activity {
	private ProgressDialog progressDialog = null;
	private TextView tvMainTime;
	private List<OneProgramInfo> programInfos;
	private ListView listView;
	private TVCurrentAdapter adapter;
	private AlertDialog orderTVDialog;
	private View orderTVView;

	private String title;
	private String channel;
	private String playTime;
	private String subInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tv_current);

		java.text.DateFormat df = new java.text.SimpleDateFormat(
				"yyyy年MM月dd日 E HH:mm");
		String curTime = df.format(new Date());

		tvMainTime = (TextView) this.findViewById(R.id.tv_current_main_time);
		tvMainTime.setText(curTime);
		listView = (ListView) this.findViewById(R.id.TVCurrentProgram);                                          //*

		// new getCurrentProgramTask().execute((Void) null);
	}

	@Override
	protected void onResume() {
		java.text.DateFormat df = new java.text.SimpleDateFormat(
				"yyyy年MM月dd日 E HH:mm");
		String curTime = df.format(new Date());
		tvMainTime.setText(curTime);

		new getCurrentProgramTask().execute((Void) null);                                                    //?

		super.onResume();
	}

	private class getCurrentProgramTask extends AsyncTask<Void, Void, Integer> {
		String resultStr = "";

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(TVCurrent.this);
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
				url = "http://202.114.18.76/GetProgram.aspx?key=&way=c";

				xmlUrl = HttpDownload.download(url);                                                                           //?
				xmlUrl = xmlUrl.substring(0, xmlUrl.indexOf(".xml") + 4);

				resultStr = HttpDownload.download(xmlUrl);

			} catch (Exception e) {
				Log.e("TVCurrent", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			progressDialog.dismiss();

			if (!resultStr.equals("")) {
				TVProgramXMLParse tvProgramXMLParse = new TVProgramXMLParse();                                       //?
				programInfos = tvProgramXMLParse.parse(resultStr);

				adapter = new TVCurrentAdapter(programInfos);
				listView.setAdapter(adapter);                                //调用下面的TvcurrentAdapter                     //?

				listView.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						title = programInfos.get(arg2).getTitle();
						channel = programInfos.get(arg2).getChannel();
						playTime = programInfos.get(arg2).getPlaytime();
						subInfo = programInfos.get(arg2).getSubinfo();                                //简介

						LayoutInflater li = LayoutInflater.from(TVCurrent.this);
						orderTVView = li.inflate(R.layout.order_program, null);                       //显示对话框

						((TextView) orderTVView
								.findViewById(R.id.order_program_title))
								.setText(title);

						((TextView) orderTVView
								.findViewById(R.id.order_program_channel))
								.setText(channel);

						((TextView) orderTVView
								.findViewById(R.id.order_program_time))
								.setText(playTime);

						((TextView) orderTVView
								.findViewById(R.id.order_program_subinfo))
								.setText(subInfo);

						if (orderTVDialog != null) {
							orderTVDialog.dismiss();
							orderTVDialog = null;
						}

						if (orderTVDialog == null) {
							orderTVDialog = new AlertDialog.Builder(
									TVCurrent.this)
									.setView(orderTVView)
									.setTitle("节目信息")
//									.setPositiveButton("预约",
//											new OnClickListener() {
//
//												public void onClick(
//														DialogInterface dialog,
//														int which) {
//
//													startActivity(new Intent(TVCurrent.this,DeskClockMainActivity.class));
//
//												}
//											})
									.setNegativeButton("确定", null)
									.create();

							orderTVDialog.show();
						}
					}
				});
			}

			super.onPostExecute(result);
		}
	}

	private class TVCurrentAdapter extends BaseAdapter {

		List<OneProgramInfo> programInfos;

		public TVCurrentAdapter(List<OneProgramInfo> programInfos) {
			this.programInfos = programInfos;
		}

		public int getCount() {
			return programInfos.size();
		}

		public Object getItem(int position) {
			return programInfos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.one_program_channel, null);
			}

			// 节目播出时间
			TextView tvPlayTime = (TextView) convertView
					.findViewById(R.id.one_program_channel_time);
			tvPlayTime.setText(programInfos.get(position).getPlaytime());

			// 节目标题
			TextView tvTitle = (TextView) convertView
					.findViewById(R.id.one_program_channel_title);
			tvTitle.setText(programInfos.get(position).getTitle());

			// 节目频道
			TextView tvChannel = (TextView) convertView
					.findViewById(R.id.one_program_channel_channel);
			tvChannel.setText(programInfos.get(position).getChannel());

			return convertView;
		}
	}

}
