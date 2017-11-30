package com.dml.TvMao;


import java.util.ArrayList;
import java.util.HashMap;

import com.dml.souqiu.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TVOrder extends Activity {

	//显示的频道名
	String channelName[] = {   
			"CCTV-1(综合)","CCTV-2(经济)","CCTV-3(综艺)","CCTV-纪录","CCTV-5(体育)","CCTV-6(电影)",
			"CCTV-7(军事农业)","CCTV-8(电视剧)","CCTV-10(科教)","CCTV-11(戏曲)","CCTV-12(法制)",
			"湖南卫视", "江苏卫视", "东方卫视", "安徽卫视", "浙江卫视",
			"北京卫视", "山东卫视", "江西卫视", "河南卫视", "重庆卫视", 
			"福建东南卫视","广西卫视", "四川卫视","广东卫视", "吉林卫视",
			"山西卫视", "云南卫视", "天津卫视", "辽宁卫视", "湖北卫视", 
			"陕西卫视","贵州卫视", "河北卫视", "黑龙江卫视", "宁夏卫视", 
			"青海卫视", "甘肃卫视","内蒙古卫视","新疆卫视"
			};
	//与channelName[]对应的频道完整名，每项作为参数传递
	String channelKey[] = {
			"中央电视台-中央电视台综合频道(CCTV-1)","中央电视台-中央电视台经济频道(CCTV-2)","中央电视台-中央电视台综艺频道(CCTV-3)",
			"中央电视台-中央电视台纪录频道(CCTV-纪录)","中央电视台-中央电视台体育频道(CCTV-5)","中央电视台-中央电视台电影频道(CCTV-6)","中央电视台-中央电视台军事农业频道(CCTV-7)",
			"中央电视台-中央电视台电视剧频道(CCTV-8)","中央电视台-中央电视台科教频道(CCTV-10)","中央电视台-中央电视台戏曲频道(CCTV-11)","中央电视台-中央电视台法制频道(CCTV-12)",
			"湖南电视台-湖南卫视","江苏电视台-江苏卫视", "上海广播电视台-东方卫视", "安徽电视台-安徽卫视", "浙江电视台-浙江卫视",
			"北京电视台-北京卫视", "山东电视台-山东卫视", "江西电视台-江西卫视", "河南电视台-河南卫视", "重庆电视台-重庆卫视",
			"福建电视台-东南卫视","广西电视台-广西卫视", "四川电视台-四川卫视",	"广东电视台-广东卫视", "吉林电视台-吉林卫视", 
			"山西电视台-山西卫视", "云南电视台-云南卫视", 	"天津电视台-天津卫视", "辽宁电视台-辽宁卫视", "湖北电视台-湖北卫视", 
			"陕西电视台-陕西卫视","贵州电视台-贵州卫视", "河北电视台-河北卫视",	"黑龙江电视台-黑龙江卫视", "宁夏电视台-宁夏卫视", 
			"青海电视台-青海卫视", "甘肃电视台-甘肃卫视","内蒙古电视台-内蒙卫视","新疆电视台-新疆卫视"
			};
	
	
	private ListView listView;
	private TVProgramProAdapter adapter;

	// http://202.114.18.76/GetProgram.aspx?key=CCTV-1&way=a
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tv_order);

		listView = (ListView) this.findViewById(R.id.TVOrderPro);

		adapter = new TVProgramProAdapter(channelName);
		listView.setAdapter(adapter);                                                        //调用????????

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent(TVOrder.this, TVProgramInfo.class);                                                  //?
				it.putExtra("channelName", channelName[arg2]);  
				it.putExtra("channelKey",channelKey[arg2]);//与channelName[]对应的频道完整名，便于节目预告内容提取
				startActivity(it);
			}
		});
	}

	private class TVProgramProAdapter extends BaseAdapter {

		String[] programProInfos;

		public TVProgramProAdapter(String[]programProInfos) {
			this.programProInfos = programProInfos;
		}

		public int getCount() {
			return programProInfos.length;
		}

		public Object getItem(int position) {
			return programProInfos[position];
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.one_program_text, null);                                              /////这种方法很独特                                         、、、、、？？？？？
				
			}

			// 频道信息
			TextView tvPlayTime = (TextView) convertView
					.findViewById(R.id.tv_program_text);
			tvPlayTime.setText(programProInfos[position]);

			return convertView;
		}
	}

}
