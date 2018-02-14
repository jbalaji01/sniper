package com.neemshade.sniper.service.filemetrics;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileMetrics {


	private Map<String, Class> supportedFormats = new HashMap<String, Class>();
	
	public FileMetrics() {
		loadSupportedFormats();
	}
	
	private void loadSupportedFormats() {
		supportedFormats = SnFileManager.fetchSupportedFormatsMap();
	}

	public boolean isSupported(String extension)
	{
		if(extension == null) return false;
		
		return supportedFormats.containsKey(extension.toLowerCase());
	}
	

	private Class fetchFileManagerClass(String extension) {
		return supportedFormats.get(extension.toLowerCase());
	}

	private SnFileManager fetchFileManager(File file, String extension)
			throws InstantiationException, IllegalAccessException, Exception {
		Class choosenClass = fetchFileManagerClass(extension);
		
		Object object = choosenClass.newInstance();
		
		if(!(object instanceof SnFileManager))
		{
			throw new Exception("Invalid file manager object! " + extension);
		}
		
		SnFileManager fileManager = (SnFileManager) object;
		fileManager.setExtension(extension);
		fileManager.setSysFile(file);
		return fileManager;
	}
	

	public FileMetricsResult calculateMetrics(File file, String extension) throws Exception
	{
		if(!isSupported(extension))
		{
			throw new Exception("format not supported! "  + extension);
		}
		
		SnFileManager fileManager = fetchFileManager(file, extension);
		FileMetricsResult fmr = fileManager.calculateMetrics();
		fmr.setIsAudio(fileManager.isAudio());
		return fmr;
	}

}
