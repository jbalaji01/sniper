package com.neemshade.sniper.service.filemetrics;

import java.io.FileInputStream;

import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;


public class SnDocRtfManager extends SnDocManager {

	@Override
	protected String extractText() throws Exception {
		  RTFEditorKit rtf = new RTFEditorKit();
	      Document doc = rtf.createDefaultDocument();
	  
	      FileInputStream fis = new FileInputStream(this.getSysFile());
	      rtf.read(fis,doc,0);
	      String text = doc.getText(0,doc.getLength());
	      
	      fis.close();
	      
	      return text;
	}
	
}
