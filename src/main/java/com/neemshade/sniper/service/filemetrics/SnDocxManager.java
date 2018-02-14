package com.neemshade.sniper.service.filemetrics;


import java.io.FileInputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;



public class SnDocxManager extends SnDocManager {
	/**
	 * @return
	 * @throws Exception 
	 */
	protected String extractText() throws Exception {
		FileInputStream fis = new FileInputStream(this.getSysFile());
		
		XWPFDocument doc = new XWPFDocument(fis);

		
		XWPFWordExtractor docExtractor =  new XWPFWordExtractor(doc);
		String text = docExtractor.getText();
		
		docExtractor.close();
		doc.close();
		fis.close();
		
		return text;
		
	}
}
