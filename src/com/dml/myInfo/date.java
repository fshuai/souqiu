package com.dml.myInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.dml.souqiu.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;

public class date extends Activity{
	private int year; // ��
	private int month;
	private int day;
	private TextView cmp;//��ɣ�
	Intent it;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date);
		cmp=(TextView)findViewById(R.id.cmp);
		it=getIntent();
		DatePicker datepicker = (DatePicker) findViewById(R.id.datePicker1); // ��ȡ����ʰȡ���
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR); // ��ȡ��ǰ���
		month = calendar.get(Calendar.MONTH); // ��ȡ��ǰ�·�
		day = calendar.get(Calendar.DAY_OF_MONTH); // ��ȡ��ǰ��
		// ��ʼ������ʰȡ�������ڳ�ʼ��ʱָ��������
		datepicker.init(year, month, day, new OnDateChangedListener() {
					@Override
		public void onDateChanged(DatePicker arg0, int year, int month,int day) {
			date.this.year = year; // �ı�year���Ե�ֵ
		    date.this.month = month; // �ı�month���Ե�ֵ
		    date.this.day = day; // �ı�day���Ե�ֵ
			show(year, month, day); // ͨ����Ϣ����ʾ����ʱ��
					}
				});
		cmp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			 it.putExtra("age", show(year,month,day));
			 date.this.setResult(3, it);
			 date.this.finish();
			}
		});
	}
	private String show(int year, int month, int day) {
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		String result =year + "-" + (month+1) + "-" + day  ;
		try {
			Date date = sdf.parse(result);
			result=sdf.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	    	finish();
	    	return true;
	    }
	
}
