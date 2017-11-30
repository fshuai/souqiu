package com.dml.search;

import java.util.ArrayList;
import java.util.Arrays;

import com.dml.adapter.SearchAutoAdapter;
import com.dml.adapter.SearchAutoData;
import com.dml.souqiu.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener  {

	public static final String SEARCH_HISTORY = "search_history";
	private ListView mAutoListView;//������¼�б�
	private TextView mSearchButtoon;//������ť
	private TextView mAutoEdit;
	private TextView mDeleteButton;
	private SearchAutoAdapter mSearchAutoAdapter;
	private Button mBtn1;
	private Button mBtn2;
	private Button mBtn3;
	private ImageView pic;
	
	//���ݵĲ���
	//原来是0
	private int query1 = 0;   
	private int query2 = 0;  
	private int query3 = 0;   

	PopupMenu popup = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_main);
		init();
	}

	private void init() {
		mSearchAutoAdapter = new SearchAutoAdapter(this, -1, this);
		mAutoListView = (ListView) findViewById(R.id.auto_listview);
		mAutoListView.setAdapter(mSearchAutoAdapter);
		mAutoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
		      SearchAutoData data = (SearchAutoData) mSearchAutoAdapter.getItem(position);
			  mAutoEdit.setText(data.getContent());
			}
		});

		mSearchButtoon = (TextView) findViewById(R.id.search_button);
		mSearchButtoon.setOnClickListener(this);
		mDeleteButton = (TextView) findViewById(R.id.delete_button);
		mDeleteButton.setOnClickListener(this);
		mBtn1 = (Button) findViewById(R.id.btn1);
		mBtn1.setOnClickListener(this);
		mBtn2 = (Button) findViewById(R.id.btn2);
		mBtn2.setOnClickListener(this);
		mBtn3 = (Button) findViewById(R.id.btn3);
		mBtn3.setOnClickListener(this);
		pic = (ImageView) findViewById(R.id.pic_button);
		pic.setOnClickListener(this);
		mAutoEdit = (TextView) findViewById(R.id.auto_edit);
		//����TextView�е����ֱ仯
		mAutoEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mSearchAutoAdapter.performFiltering(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn1:
			createPopMenu1();
			break;
		case R.id.btn2:
			createPopMenu2();
			break;
		case R.id.btn3:
			createPopMenu3();
			break;
		case R.id.search_button:
			if(query1 * (query2 + 1) * (query3 + 1) == 0){  //����Ϊѡ��
				Toast.makeText(SearchActivity.this, "请完善搜索设置", Toast.LENGTH_SHORT).show();
			}
		else if(mAutoEdit.getText().toString().equals("")){
				Toast.makeText(SearchActivity.this, "关键字不可为空", Toast.LENGTH_SHORT).show();
			}
		else{
			Intent intent = new Intent();
			intent.putExtra("mode", 1);
			intent.putExtra("query1", query1);
			intent.putExtra("query2", query2);
			intent.putExtra("query3", query3);
			intent.putExtra("key", mAutoEdit.getText().toString());
			intent.setClass(SearchActivity.this, SearchResultActivity.class);
			saveSearchHistory();//����������¼
			mSearchAutoAdapter.initSearchHistory();//���³�ʼ��������¼
			startActivity(intent);
			}
			break;
		case R.id.delete_button:
			SharedPreferences sp = getSharedPreferences(SEARCH_HISTORY, 0);
			sp.edit().clear().commit();
			Toast.makeText(SearchActivity.this, "已清除历史记录", Toast.LENGTH_SHORT).show();
			mAutoListView.setAdapter(new SearchAutoAdapter(this, -1, this));
			break;
		case R.id.auto_add:
			//��ȡlistViewItem�е�content
			SearchAutoData data = (SearchAutoData) v.getTag();
			mAutoEdit.setText(data.getContent());
			break;
		case R.id.pic_button:
		if(query1 * (query2 + 1) * (query3 + 1) == 0){  //����Ϊѡ��
			Toast.makeText(SearchActivity.this, "请完善搜索设置", Toast.LENGTH_SHORT).show();
		}else{
			Intent intent = new Intent();
			intent.putExtra("query1", query1);
			intent.putExtra("query2", query2);
			intent.putExtra("query3", query3);
			intent.setClass(SearchActivity.this, SelectPicPopupWindow.class);
			startActivity(intent);
		}
			break;
		default:
			break;
		}
	}
	
	/*
	 * ��������ʽ�˵�1
	 */
	private void createPopMenu1(){
		popup = new PopupMenu(this, mBtn1);
		getMenuInflater().inflate(R.menu.popup_menu1, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				switch (item.getItemId())
				{
			     	case R.id.ball1:
					     query1 = 1;
					     mBtn1.setText("足球");
					     break;
					case R.id.ball2:
						query1 = 2;
						mBtn1.setText("篮球");
						break;
					case R.id.ball3:
						query1 = 3;
						mBtn1.setText("网球");
						break;
					case R.id.ball4:
						query1 = 4;
						mBtn1.setText("乒乓球");
						break;
					case R.id.ball5:
						query1 = 5;
						mBtn1.setText("羽毛球");
						break;
					case R.id.ball6:
						query1 = 6;
						mBtn1.setText("排球");
						break;
					case R.id.ball7:
						query1 = 7;
						mBtn1.setText("台球");
						break;
					default:
						break;
				}
				return true;
			}
		});
		popup.show();
	
	}

	private void createPopMenu2(){
		popup = new PopupMenu(this, mBtn2);
		getMenuInflater().inflate(R.menu.popup_menu2, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				switch (item.getItemId())
				{
			     	case R.id.order1:
				    	query2 = 0;
					    mBtn2.setText("相关程度");
					    break;
					case R.id.order2:
						query2 = 1;
						mBtn2.setText("最新发布");
						break;
					case R.id.order3:
						query2 = 2;
						mBtn2.setText("最多播放");
						break;
					case R.id.order4:
						query2= 3;
						mBtn2.setText("最多评论");
						break;
					default:
						break;
						
				}
				return true;
			}
		});
		popup.show();
		
	}
	
	private void createPopMenu3(){
		popup = new PopupMenu(this, mBtn3);
		getMenuInflater().inflate(R.menu.popup_menu3, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				switch (item.getItemId())
				{
				    case R.id.length1:
					    query3 = 0;
					    mBtn3.setText("全部");
					    break;
					case R.id.length2:
						query3 = 1;
						mBtn3.setText("0-10分钟");
						break;
					case R.id.length3:
						query3 = 2;
						mBtn3.setText("10-30分钟");
						break;
					case R.id.length4:
						query3 = 3;
						mBtn3.setText("30-60分钟");
						break;
					case R.id.length5:
						query3 = 4;
						mBtn3.setText("大于60分钟");
						break;
					default:
						break;
				}
				return true;
			}	
		});
		popup.show();
		
	}
	
	/*
	 * ����������¼
	 */
	private void saveSearchHistory() {
		String text = mAutoEdit.getText().toString().trim();
		if (text.length() < 1) {
			return;
		}
		SharedPreferences sp = getSharedPreferences(SEARCH_HISTORY, 0);
		String longhistory = sp.getString(SEARCH_HISTORY, "");
		String[] tmpHistory = longhistory.split(",");
		ArrayList<String> history = new ArrayList<String>(Arrays.asList(tmpHistory));
		if (history.size() > 0) {
			int i;
			for (i = 0; i < history.size(); i++) {
				if (text.equals(history.get(i))) {
					history.remove(i);
					break;
				}
			}
			history.add(0, text);
		}
		if (history.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < history.size(); i++) {
				sb.append(history.get(i) + ",");
			}
			sp.edit().putString(SEARCH_HISTORY, sb.toString()).commit();
		} else {
			sp.edit().putString(SEARCH_HISTORY, text + ",").commit();
		}
	}

	@Override
	protected void onResume(){
		mAutoEdit.setText("");
		super.onResume();
	}
}

