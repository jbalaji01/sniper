package com.neemshade.sniper.service.filemetrics;


import java.io.FileInputStream;

import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;

public class SnDocManager extends SnDocumentManager {

	@Override
	public FileMetricsResult calculateMetrics() throws Exception {

		String text = extractText();
		if(text == null) 
		{
			throw new Exception("Unable to read " + this.getSysFile().getName());
		}
		
		Integer charCountWithSpace = text.length();
		
		String strippedText = text.replaceAll("\\s", "");
		Integer charCountWithoutSpace = strippedText.length();
		
		if(charCountWithSpace != null)
			fileMetricsResult.setWsLineCount( charCountWithSpace / CHARS_PER_LINE);
		
		if(charCountWithoutSpace != null)
			fileMetricsResult.setWosLineCount(charCountWithoutSpace / CHARS_PER_LINE);
		
		return fileMetricsResult;
	}
	
	
	protected void findLineCounts() throws Exception {
		
	}
	
	
	/**
	 * @return
	 * @throws Exception 
	 */
	protected String extractText() throws Exception {

		FileInputStream fis = new FileInputStream(this.getSysFile());
		
		final POITextExtractor extractor = ExtractorFactory.createExtractor(fis);
		String text = extractor.getText();
		extractor.close();
		fis.close();
		
		return text;
	       
	}

}
