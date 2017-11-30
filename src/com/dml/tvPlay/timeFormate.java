package com.dml.tvPlay;

public class timeFormate {
	 private  int hour;
	 private  int minute;
	 private  int second;
	  public  String timeFormate(String str){
		  //ע�⴫�����Ĳ�����n��������
		  String s="";
		  int n=Integer.parseInt(str);
		   int total=n;
			second=n%60;//�������
			n=n/60;
			hour=n%3600/60;	
			minute=(total-hour*3600-second)/60;	
			if(hour==0)
			 s=minute+"分"+second+"秒";
			else
				s=hour+"时"+minute+"分"+second+"秒";	
			return s;
		}
	  public  String timeFormate2(int n){
		  //ע�⴫�����Ĳ�����n��������
		     String s="";
		     int total=n;
			 second=n%60;//�������
			 n=n/60;
			 hour=n%3600/60;	
			 minute=(total-hour*3600-second)/60;	
			 if(hour==0)
			 s=minute+"分"+second+"秒";
			 else
				s=hour+"时"+minute+"分"+second+"秒";	
			return s;
		}
}
