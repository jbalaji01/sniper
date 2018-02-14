package com.neemshade.sniper.service.filemetrics;

public class FileMetricsResult {
	
	private Boolean isAudio;
	
	private Integer audioDuration;
	
	private Integer wsLineCount;
	private Integer wosLineCount;
	
	
	public Integer getAudioDuration() {
		return audioDuration;
	}
	public void setAudioDuration(Integer audioDuration) {
		this.audioDuration = audioDuration;
	}
	public Integer getWsLineCount() {
		return wsLineCount;
	}
	public void setWsLineCount(Integer wsLineCount) {
		this.wsLineCount = wsLineCount;
	}
	public Integer getWosLineCount() {
		return wosLineCount;
	}
	public void setWosLineCount(Integer wosLineCount) {
		this.wosLineCount = wosLineCount;
	}
	public Boolean getIsAudio() {
		return isAudio;
	}
	public void setIsAudio(Boolean isAudio) {
		this.isAudio = isAudio;
	}
	
	
}
