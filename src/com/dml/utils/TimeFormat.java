package com.dml.utils;

public class TimeFormat {
    public static String format(int time){
    	int min = time / 60;
    	int second = time % 60;
    	StringBuilder s = new StringBuilder();
    	if(min < 10)
    		s.append("0");
    	s.append(min + ":");
    	if(second < 10)
    		s.append("0");
    	s.append(second);
    	return s.toString();
    }
}
