package com.dml.TvMao;



import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.dml.souqiu.R;


public class TVSearch extends Activity {

	private Button btSearch;
	private boolean hasSearchResult =false;  //�������Ӹ���                                                                                                                                               ��Ŀ��ѯ�������к��ã�                                                                                             ///?
	private AutoCompleteTextView atSearchInput;
	private String searchKey;
	private ProgressDialog progressDialog = null;

	private ListView listView;
	private TVSearchAdapter adapter;
	
	private List<OneProgramInfo> programInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tv_search);

		btSearch = (Button) this.findViewById(R.id.TVSearchButton);
		atSearchInput = (AutoCompleteTextView) this
				.findViewById(R.id.TVSearchInput);
		listView = (ListView) this.findViewById(R.id.TVSearchResult);

		//atSearchInput.setFocusable(false);

		btSearch.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				searchKey = atSearchInput.getText().toString().trim();                                 
				if (!searchKey.equals("")) {
					new searchProgramTask().execute((Void) null);                                      /////������������������ĺ���
				} else {
					atSearchInput.setFocusable(true);
				}
			}
		});

	 
	/**
	 * ��Ŀ�Ƽ�������ʾ
	 */
		
		
	}

	@Override
	protected void onResume() { //��Activity�����£���ý���ʱ����������ʾ
		
		if(hasSearchResult){
			adapter = new TVSearchAdapter(programInfos);
			listView.setAdapter(adapter);
		}
		
		super.onResume();
	}

	private class searchProgramTask extends AsyncTask<Void, Void, Integer> {
		String resultStr = "";

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(TVSearch.this);
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
						+ GBEncode.formatUrl(searchKey) + "&way=s";
				

				xmlUrl = HttpDownload.download(url);
				xmlUrl = xmlUrl.substring(0, xmlUrl.indexOf(".xml") + 4);

				String keyword = xmlUrl.substring(xmlUrl.lastIndexOf("/") + 1,
						xmlUrl.indexOf(".xml"));

				xmlUrl = xmlUrl.replace(keyword, GBEncode.formatUrl(keyword));                                 ///?

				resultStr = HttpDownload.download(xmlUrl);

			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				Log.e("TVSearch", e1.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			progressDialog.dismiss();

			if (!resultStr.equals("")) {
				TVProgramXMLParse tvProgramXMLParse = new TVProgramXMLParse();
				programInfos = tvProgramXMLParse
						.parse(resultStr);
                
				hasSearchResult = true;
				adapter = new TVSearchAdapter(programInfos);                             //���������
				listView.setAdapter(adapter);

			}

			super.onPostExecute(result);
		}
	}

	private class TVSearchAdapter extends BaseAdapter {

		List<OneProgramInfo> programInfos;
	

		public TVSearchAdapter(List<OneProgramInfo> programInfos) {
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
						R.layout.one_program_info, null);
			}

			// ��Ŀ����
			TextView tvTitle = (TextView) convertView
					.findViewById(R.id.tv_search_program_title);
			tvTitle.setText(programInfos.get(position).getTitle());

			// ��ĿƵ��
			TextView tvChannel = (TextView) convertView
					.findViewById(R.id.tv_search_program_channel);
			tvChannel.setText(programInfos.get(position).getChannel());

			// ��Ŀ����ʱ��
			TextView tvPlayTime = (TextView) convertView
					.findViewById(R.id.tv_search_program_time);
			tvPlayTime.setText(programInfos.get(position).getPlaytime());
			
			// ��Ŀ��������
			TextView tvPlayWeek = (TextView) convertView
					.findViewById(R.id.tv_search_program_week);
			tvPlayWeek.setText(WeekTran(programInfos.get(position).getPlayweek()));                         ////���������weektran
			
	
			return convertView;
		}
		
	
	}
 
	private String WeekTran(String playweek){
		if(playweek.equals("����һ"))
			return "��һ";
		else if(playweek.equals("���ڶ�"))
			return "�ܶ�";
		else if(playweek.equals("������"))
			return "����";
		else if(playweek.equals("������"))
			return "����";
		else if(playweek.equals("������"))
			return "����";
		else if(playweek.equals("������"))
			return "����";
		else 
			return "����";
	}
}
