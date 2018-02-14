package com.neemshade.sniper.service.filemetrics;


import java.io.FileReader;

public class SnDssManager extends SnAudioManager {

	@Override
	public FileMetricsResult calculateMetrics() throws Exception {

		if(!("ds2".equalsIgnoreCase(getExtension()) || "dss".equalsIgnoreCase(getExtension())) )
		{
			throw new Exception("Invalid format");
		}
		
		 int durationOffset = 62;
	      int durationLength = 6;
	      int headerLength = durationOffset + durationLength;

	      char[] fileHeader = new char[headerLength + 1];
	      FileReader fileReader = null;
	      
	      try {
	     fileReader = new FileReader(getSysFile());
	     fileReader.read(fileHeader, 0, headerLength);


	         String hours = fileHeader[durationOffset + 0] + "" + fileHeader[durationOffset + 1];
	         String min = fileHeader[durationOffset + 2] + "" + fileHeader[durationOffset + 3];
	         String sec = fileHeader[durationOffset + 4] + "" + fileHeader[durationOffset + 5];
	         
	         String timeStr = hours + ":" + min + ":" + sec;
//	         System.out.println(file.getName() + " " + timeStr);
	         long durationInSeconds = convertStringToSeconds(timeStr);
	    
	         fileMetricsResult.setAudioDuration((int) durationInSeconds);
	      }
	      finally {
	    	  fileReader.close();
	      }
	      
	      return fileMetricsResult;
	}

}
