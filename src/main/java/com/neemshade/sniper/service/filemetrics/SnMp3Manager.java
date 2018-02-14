package com.neemshade.sniper.service.filemetrics;


import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;


public class SnMp3Manager extends SnAudioManager {

	@Override
	public FileMetricsResult calculateMetrics() throws Exception {
		AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(getSysFile());
	    if (fileFormat instanceof TAudioFileFormat) {
	        Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
	        String key = "duration";
	        Long microseconds = (Long) properties.get(key);
	        int mili = (int) (microseconds / 1000);
//	        int sec = (mili / 1000) % 60;
//	        int min = (mili / 1000) / 60;
//	        System.out.println("time = " + min + ":" + sec);
	        
	        fileMetricsResult.setAudioDuration(mili / 1000);
	    } else {
	        throw new UnsupportedAudioFileException();
	    }
	    
	    return fileMetricsResult;
	}

}
