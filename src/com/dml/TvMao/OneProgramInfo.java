package com.dml.TvMao;

public class OneProgramInfo {
    private String id;
	private String title;
	private String channel;
	private String playtime;
	private String playweek;
	private String playdate;
	private String subinfo;

	public String getSubinfo() {
		return subinfo;
	}

	public void setSubinfo(String subinfo) {
		this.subinfo = subinfo;
	}

	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPlaytime() {
		return playtime;
	}
	
	public void setPlaytime(String playtime) {
		this.playtime = playtime;
	}
	
	public String getPlayweek() {
		return playweek;
	}

	public void setPlayweek(String playweek) {
		this.playweek = playweek;
	}
	
	public String getPlaydate() {
		return playdate;
	}

	public void setPlaydate(String playdate) {
		this.playdate = playdate;
	}

	@Override
	public String toString() {
		return "OneProgramInfo [id="+id +",title=" + title + ", channle=" + channel
				+ ", playtime=" + playtime +", playweek=" + playweek + ", playdate=" + playdate + ", subinfo=" + subinfo + "]";
	}

}
