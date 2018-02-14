package com.neemshade.sniper.service.filemetrics;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class SnFileManager {

	public static final Integer CHARS_PER_LINE = 65;
	
	protected File sysFile;
	protected String extension;
	
	protected FileMetricsResult fileMetricsResult = new FileMetricsResult();
	
	
	// all supported file formats have an entry here
	// the file extension is stored as key.  lower case of ext is stored
	public static Map<String, Class> fetchSupportedFormatsMap() {
		Map<String, Class> supportedFormats = new HashMap<String, Class>();
		
		supportedFormats.put("doc", SnDocManager.class);
		supportedFormats.put("docx", SnDocxManager.class);
		
		supportedFormats.put("wav", SnWavManager.class);
		supportedFormats.put("mp3", SnMp3Manager.class);
		supportedFormats.put("m4a", SnM4aManager.class);
		
		// note that dss and ds2 point to same class
		supportedFormats.put("dss", SnDssManager.class);
		supportedFormats.put("ds2", SnDssManager.class);
		
		return supportedFormats;
	}

	

	/**
	 * given h:mm:ss calculate the seconds
	 * @param str
	 */
	public static Long convertStringToSeconds(String str)
	{
		if(str == null) return null;
		
		String[] splits = str.split(":");
		int timeMultiplier = 1;
		Long totalTime = (long) 0;
		
		for (int i = splits.length - 1; i >= 0; i--) {
			int num = 0;
			try {
				num = Integer.parseInt(splits[i]);
			}
			catch(Exception ex)
			{
//				ex.printStackTrace();
				return null;
			}
			
			totalTime += num * timeMultiplier;
			timeMultiplier *= 60;
		}
		
		return totalTime;
		
	}
	
	/**
	 * given seconds is formatted as h:mm:ss
	 * @param seconds
	 * @return
	 */
	public static String convertSecondsToString(Long seconds)
	{
		long ms = seconds * 1000;
		
		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(ms),
		        TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)),
		        TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));

		
		
		return hms;
		
	}
	
	
	
	
	
	abstract public FileMetricsResult calculateMetrics() throws Exception;
	
	abstract public boolean isAudio();

	public File getSysFile() {
		return sysFile;
	}

	public void setSysFile(File sysFile) {
		this.sysFile = sysFile;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public FileMetricsResult getFileMetricsResult() {
		return fileMetricsResult;
	}

	public void setFileMetricsResult(FileMetricsResult fileMetricsResult) {
		this.fileMetricsResult = fileMetricsResult;
	}
	
}
