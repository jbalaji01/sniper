package com.neemshade.sniper.service.filemetrics;

import org.mp4parser.IsoFile;

public class SnM4aManager extends SnAudioManager {

	@Override
	public FileMetricsResult calculateMetrics() throws Exception {
//	    IsoFile isoFile = new IsoFile (new MemoryDataSourceImpl(content));
		IsoFile isoFile = new IsoFile(getSysFile());
	    double lengthInSeconds = (double)isoFile.getMovieBox().getMovieHeaderBox().getDuration() / isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
	    isoFile.close();
	    
	    fileMetricsResult.setAudioDuration((int) lengthInSeconds);
	    return fileMetricsResult;
	}

}
