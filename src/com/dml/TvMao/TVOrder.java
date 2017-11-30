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

	//��ʾ��Ƶ����
	String channelName[] = {   
			"CCTV-1(�ۺ�)","CCTV-2(����)","CCTV-3(����)","CCTV-��¼","CCTV-5(����)","CCTV-6(��Ӱ)",
			"CCTV-7(����ũҵ)","CCTV-8(���Ӿ�)","CCTV-10(�ƽ�)","CCTV-11(Ϸ��)","CCTV-12(����)",
			"��������", "��������", "��������", "��������", "�㽭����",
			"��������", "ɽ������", "��������", "��������", "��������", 
			"������������","��������", "�Ĵ�����","�㶫����", "��������",
			"ɽ������", "��������", "�������", "��������", "��������", 
			"��������","��������", "�ӱ�����", "����������", "��������", 
			"�ຣ����", "��������","���ɹ�����","�½�����"
			};
	//��channelName[]��Ӧ��Ƶ����������ÿ����Ϊ��������
	String channelKey[] = {
			"�������̨-�������̨�ۺ�Ƶ��(CCTV-1)","�������̨-�������̨����Ƶ��(CCTV-2)","�������̨-�������̨����Ƶ��(CCTV-3)",
			"�������̨-�������̨��¼Ƶ��(CCTV-��¼)","�������̨-�������̨����Ƶ��(CCTV-5)","�������̨-�������̨��ӰƵ��(CCTV-6)","�������̨-�������̨����ũҵƵ��(CCTV-7)",
			"�������̨-�������̨���Ӿ�Ƶ��(CCTV-8)","�������̨-�������̨�ƽ�Ƶ��(CCTV-10)","�������̨-�������̨Ϸ��Ƶ��(CCTV-11)","�������̨-�������̨����Ƶ��(CCTV-12)",
			"���ϵ���̨-��������","���յ���̨-��������", "�Ϻ��㲥����̨-��������", "���յ���̨-��������", "�㽭����̨-�㽭����",
			"��������̨-��������", "ɽ������̨-ɽ������", "��������̨-��������", "���ϵ���̨-��������", "�������̨-��������",
			"��������̨-��������","��������̨-��������", "�Ĵ�����̨-�Ĵ�����",	"�㶫����̨-�㶫����", "���ֵ���̨-��������", 
			"ɽ������̨-ɽ������", "���ϵ���̨-��������", 	"������̨-�������", "��������̨-��������", "��������̨-��������", 
			"��������̨-��������","���ݵ���̨-��������", "�ӱ�����̨-�ӱ�����",	"����������̨-����������", "���ĵ���̨-��������", 
			"�ຣ����̨-�ຣ����", "�������̨-��������","���ɹŵ���̨-��������","�½�����̨-�½�����"
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
		listView.setAdapter(adapter);                                                        //����????????

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent(TVOrder.this, TVProgramInfo.class);                                                  //?
				it.putExtra("channelName", channelName[arg2]);  
				it.putExtra("channelKey",channelKey[arg2]);//��channelName[]��Ӧ��Ƶ�������������ڽ�ĿԤ��������ȡ
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
						R.layout.one_program_text, null);                                              /////���ַ����ܶ���                                         ��������������������
				
			}

			// Ƶ����Ϣ
			TextView tvPlayTime = (TextView) convertView
					.findViewById(R.id.tv_program_text);
			tvPlayTime.setText(programProInfos[position]);

			return convertView;
		}
	}

}
