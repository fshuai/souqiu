package com.dml.bean;

public class VideoBean {
	private String id;
    private String imageUrl;
    private String title;
    private int clickNum;
    private String length;
    private String origin;
    private String time;
    private String videoUrl;
    private String videotag;   

	public VideoBean(){
    	
    }
    public VideoBean(String id, String imageUrl, String title, int clickNum, String length, String origin, String time, String videoUrl){
    	this.id = id;
    	this.imageUrl = imageUrl;
    	this.title = title;
    	this.clickNum = clickNum;
    	this.length = length;
    	this.origin = origin;
    	this.time = time;
    	this.videoUrl = videoUrl;
    }
    public String getId(){
    	return id;
    }
    
    public String getImageUrl(){
    	return imageUrl;
    }
    
    public String getTitle(){
    	return title;
    }
    
    public int getClickNum(){
    	return clickNum;
    }
    
    public String getLength(){
    	return length;
    }
    
    public String getOrigin(){
    	return origin;
    }
    
    public String getTime(){
    	return time;
    }
    
    public String getVideoUrl(){
    	return videoUrl;
    }
    
    public void setId(String id){
    	this.id = id;
    }
    
    public void setImageUrl(String imageUrl){
    	this.imageUrl = imageUrl;
    }
    
    public void setTitle(String title){
    	this.title = title;
    }
    
    public void setClickNum(int clickNum){
    	this.clickNum = clickNum;
    }
    
    public void setLength(String length){
    	this.length = length;
    }
    
    public void setOrigin(String origin){
    	this.origin = origin;
    }
    
    public void setTime(String time){
    	this.time = time;
    }
    
    public void setVideoUrl(String videoUrl){
    	this.videoUrl = videoUrl;
    }
    
	public String getVideotag() {
		return videotag;
	}
	public void setVideotag(String videotag) {
		this.videotag = videotag;
	}
	
	
	
	
}
