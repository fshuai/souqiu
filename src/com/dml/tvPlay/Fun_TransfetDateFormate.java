package com.dml.tvPlay;

import java.text.DecimalFormat;

public class Fun_TransfetDateFormate {
 private  int hour;
 private  int minute;
 private  int second;
  public  String Fun(int n){
	  //ע�⴫�����Ĳ�����n��������
	   DecimalFormat df = new DecimalFormat("00"); //��1:2:3->01:02:03
	   int total=n;
		second=n%60;//�������
		n=n/60;
		hour=n%3600/60;	
		minute=(total-hour*3600-second)/60;	
		String s=df.format(hour)+":"+df.format(minute)+":"+df.format(second);
		return s;
	}
}
