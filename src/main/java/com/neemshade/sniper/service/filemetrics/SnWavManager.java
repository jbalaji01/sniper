package com.neemshade.sniper.service.filemetrics;


import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

public class SnWavManager extends SnAudioManager {

	@Override
	public FileMetricsResult calculateMetrics() throws Exception {
		AudioFileFormat aff = AudioSystem.getAudioFileFormat(getSysFile());
		AudioFormat format = aff.getFormat();
	    long audioFileLength = getSysFile().length();
	    int frameSize = format.getFrameSize();
	    float frameRate = format.getFrameRate();
	    if(frameSize == 0 || frameRate == 0)
	    	throw new Exception("audio file returns 0 value");
	    float durationInSeconds = (audioFileLength / (frameSize * frameRate));
	    
	    fileMetricsResult.setAudioDuration((int) durationInSeconds);
	    
	    return fileMetricsResult;
	}

}
