package com.dml.tvPlay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class clipSegVideoUrl {
	//传进来的地址是{XX 12=>xX 34=> } 
	LinkedList<ArrayList>linkedlist=new LinkedList<ArrayList>();
	ArrayList <String>list =new ArrayList<String>();//视频地址数组
	ArrayList<String >timeList=new ArrayList<String>();//时间段数组
	public  LinkedList clipAdrress(String str){ 
		str=str.substring(1, str.length()-4);//除去-> }
		String []str1=str.split("=>"); 
		for(int i=0;i<str1.length;i++){
			String tem=str1[i].substring(0,str1[i].indexOf(" "));//用于保持每段的video地址
			String temp=str1[i].substring(str1[i].indexOf(" ")+1, str1[i].length());//用于保持每段的时间大小
			list.add(tem);
			timeList.add(temp);
		}
		linkedlist.add(list);
		linkedlist.add(timeList); 
		return linkedlist; 
	}
	
	public  ArrayList getPercentage(){
		ArrayList<Integer> percentlist=new ArrayList<Integer>();//用于还回每段所占的百分比
		int total=0;
		for(int j=0;j<timeList.size();j++){
			total+=Integer.parseInt(timeList.get(j)); 
		}
		for(int i=0;i<timeList.size();i++){
			int sum=0;
			for(int j=0;j<=i;j++){
			  sum+=Integer.parseInt(timeList.get(j));
			}
			percentlist.add(sum*100/total);
	  }
	  return  percentlist;
  }
}
