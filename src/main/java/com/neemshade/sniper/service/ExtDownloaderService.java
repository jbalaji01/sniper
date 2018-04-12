package com.neemshade.sniper.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neemshade.sniper.domain.Doctor;
import com.neemshade.sniper.domain.Hospital;
import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.domain.SnFileBlob;
import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.domain.enumeration.ChosenFactor;
import com.neemshade.sniper.repository.SnFileBlobRepository;
import com.neemshade.sniper.service.ExtTaskService.BUNDLE_FIELD;

@Service
@Transactional
public class ExtDownloaderService {
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private SnFileService snFileService;
	
	@Autowired
	private SnFileBlobRepository snFileBlobRepository;
	
	private Font boldFont;
	private CellStyle cellStyle;

	/**
	 * invoke appropriate module to download files
	 * @param source - taskGroup or task
	 * @param id - non-zero for existing taskGroup or task
	 * @param selectedIds 
	 * @throws Exception
	 */
	public byte[] downloadFiles(String source, Long id, boolean isEditorOnly, String selectedIds) throws Exception {
		if(source == null)
		{
			throw new Exception("Invalid source param");
		}
		
		if((source.equalsIgnoreCase("taskGroup") || source.equalsIgnoreCase("editorOnly")) && id != null && id > 0)
		{
			return downloadFilesOfTaskGroup(id, isEditorOnly);
		}
		
		if(source.equalsIgnoreCase("task") && id != null && id > 0)
		{
			Task task = taskService.findOne(id);
			return downloadFilesOfTask(task, isEditorOnly);
		}
		
		if(source.equalsIgnoreCase("selectedTasks") && selectedIds != null)
		{
			return downloadFilesOfSelectedTasks(selectedIds, isEditorOnly);
		}
		
		throw new Exception("Invalid data " + source + " " + id + " " + selectedIds);
	}

	/**
	 * the ids of selected tasks are given in comma separated string.
	 * download all those files
	 * @param selectedIds
	 * @return
	 */
	private byte[] downloadFilesOfSelectedTasks(String selectedIds, boolean isEditorOnly) throws Exception {
		if(selectedIds == null) return null;
		
		String[] selectedIdArr = selectedIds.split(",");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(baos);
	    
	    for (String selectedId : selectedIdArr) {
	    	Long taskId = Long.parseLong(selectedId);
	    	Task task = taskService.findOne(taskId);
			downloadFilesOfTask(task, zos, isEditorOnly);
		}
	    
	    zos.closeEntry();
	    zos.close();
	    return baos.toByteArray();
	}

	/**
	 * browse all tasks of this group and collect the files as zip
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	private byte[] downloadFilesOfTaskGroup(Long taskGroupId, boolean isEditorOnly) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(baos);
	    
	    downloadFilesOfTaskGroup(taskGroupId, zos, isEditorOnly);
	    
	    zos.closeEntry();
	    zos.close();
	    return baos.toByteArray();
	}

	private void downloadFilesOfTaskGroup(Long taskGroupId, ZipOutputStream zos, boolean isEditorOnly) throws Exception {
		List<Task> tasks = taskService.findTasksOfTaskGroup(taskGroupId);
		
		for (Task task : tasks) {
			downloadFilesOfTask(task, zos, isEditorOnly);
		}
	}

	/**
	 * collect all the files in zip format
	 * @param taskId
	 * @return
	 * @throws Exception 
	 */
	private byte[] downloadFilesOfTask(Task task, boolean isEditorOnly) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(baos);
	    
	    downloadFilesOfTask(task, zos, isEditorOnly);
	    
	    zos.closeEntry();
	    zos.close();
	    return baos.toByteArray();
	}

	/**
	 * this method may be called by both task and taskGroup zip generators
	 * @param taskId
	 * @param zos
	 * @throws Exception 
	 */
	private void downloadFilesOfTask(Task task, ZipOutputStream zos, boolean isEditorOnly) throws Exception {
		
		if(task == null) return;
		
		String path = "task_" + task.getId() + "/";
		zos.putNextEntry(new ZipEntry(path));
		
		// fetch all the files of this task and write them into zos
		List<SnFile> snFiles = snFileService.findSnFilesOfTask(task.getId());
		
		for (SnFile snFile : snFiles) {
			
			if(!allowedSnFile(task, snFile, isEditorOnly)) continue;
			
			String filename = snFile.getFileName() + "." + snFile.getFileExt();
			
			// get the blob of this snFile
			SnFileBlob snFileBlob = snFileBlobRepository.findTopBySnFileId(snFile.getId());
			
			if(snFileBlob == null || snFileBlob.getFileContent() == null)
				throw new Exception("Unable to read " + filename);
			
			ZipEntry entry = new ZipEntry(path + filename);
			entry.setSize(snFileBlob.getFileContent().length);
			zos.putNextEntry(entry);
			zos.write(snFileBlob.getFileContent());
		}
	}


	/**
	 * check if snFile can be included in the download
	 * @param task
	 * @param snFile
	 * @param isEditorOnly
	 * @return
	 */
	private boolean allowedSnFile(Task task, SnFile snFile, boolean isEditorOnly) {
		if(task == null || snFile == null) return false;
		
		if(isEditorOnly) 
		{
			try {
				if(snFile.isIsInput()) return false;
				
				return task.getEditor().getId() == snFile.getUploader().getId();
			} catch(Exception ex) {
				return false;
			}
		}
		
		return true;
			
	}

	/**
	 * collect all tasks and snFiles of the given taskGroups
	 * compose spread sheets classifying hospitals and doctors
	 * audio length, ws, wos counts are listed and summed 
	 * @param os 
	 * @param selectedTaskGroupIds
	 * @throws Exception
	 */
	public void exportXlsx(OutputStream os, String selectedTaskGroupIds) throws Exception {
		if(selectedTaskGroupIds == null || "".equals(selectedTaskGroupIds)) {
			return;
		}
		
		Map<ExtTaskService.BUNDLE_FIELD, Map<Object, List<SnFile>>> snCountMap = new HashMap<ExtTaskService.BUNDLE_FIELD, Map<Object, List<SnFile>>>();
		
		String[] selectedIdArr = selectedTaskGroupIds.split(",");
		
		for (String selectedId : selectedIdArr) {
	    	Long taskGroupId = Long.parseLong(selectedId);
	    	
	    	if(taskGroupId == null || taskGroupId <= 0) continue;
	    	
	    	List<Task> tasks = taskService.findTasksOfTaskGroup(taskGroupId);
	    	
	    	if(tasks == null) continue;
	    	
	    	for (Task task : tasks) {
	    		
	    		if(task == null) continue;
	    		
				List<SnFile> snFiles = snFileService.findSnFilesOfTask(task.getId());
				
				if(snFiles == null) continue;
				
				for(SnFile snFile : snFiles) {
					if(snFile == null) continue;
					
					appendData(snCountMap, task, snFile);
				}
			}
		}
		
		Workbook wb = generateXlsx(selectedTaskGroupIds, snCountMap);
		wb.write(os);
	}

	

	/**
	 * get hospital and doctor from task.  place that in the map
	 * @param snCountMap
	 * @param task
	 * @param snFile 
	 */
	private void appendData(Map<BUNDLE_FIELD, Map<Object, List<SnFile>>> snCountMap, Task task, SnFile snFile) {
		appendData(snCountMap, task.getHospital(), snFile, ExtTaskService.BUNDLE_FIELD.HOSPITAL);
		appendData(snCountMap, task.getDoctor(), snFile, ExtTaskService.BUNDLE_FIELD.DOCTOR);
	}

	private void appendData(Map<BUNDLE_FIELD, Map<Object, List<SnFile>>> snCountMap, Object object, SnFile snFile,
			BUNDLE_FIELD field) {
		if(snCountMap == null || snFile == null) return;
		
		if(!snCountMap.containsKey(field)) {
			snCountMap.put(field, new HashMap<Object, List<SnFile>>());
		}
		
		Map<Object, List<SnFile>> fieldMap = snCountMap.get(field);
		
		if(object == null) {
			object = "_Unknown";
		}
		
		if(!fieldMap.containsKey(object)) {
			fieldMap.put(object, new ArrayList<SnFile>());
		}
		
		fieldMap.get(object).add(snFile);
	}

	/**
	 * calculate total counts and place them in sheets
	 * @param selectedTaskGroupIds 
	 * @param os 
	 * @param snCountMap
	 * @return 
	 */
	private Workbook generateXlsx(String selectedTaskGroupIds, Map<BUNDLE_FIELD, Map<Object, List<SnFile>>> snCountMap) {
		Workbook wb = new XSSFWorkbook();
		
		setBoldFont(wb);
		
		generateInfoSheet(wb, selectedTaskGroupIds);
		generateHospitalsSheet(wb, snCountMap);
		generateDoctorsSheet(wb, snCountMap);
		
//		autoSizeColumns(wb);
		
		return wb;
	}
	
	public void autoSizeColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	        Sheet sheet = workbook.getSheetAt(i);
	        
	            Row row = null;
	            for(int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
	            	row = sheet.getRow(j);
	            	if(row != null) break;
	            }
	            		
	            if(row == null) continue;
	            
	            for(int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
//	    	    	Cell cell = getCell(row, k);
//	    	    	if(cell == null) continue;
	    	    	
	    	    	sheet.autoSizeColumn(k);
	    	    }
	            
//	            Iterator<Cell> cellIterator = row.cellIterator();
//	            
//	            while (cellIterator.hasNext()) {
//	                Cell cell = cellIterator.next();
//	                int columnIndex = cell.getColumnIndex();
//	                sheet.autoSizeColumn(columnIndex);
//	            }
	        
	    }
	}

	private void generateInfoSheet(Workbook wb, String selectedTaskGroupIds) {
		
		Sheet sheet = wb.createSheet("Info");
		
		int nextRow = 4;
		int nextCol = 2;
		
		Row r = sheet.getRow(nextRow);
		if (r == null) {
		    r = sheet.createRow(nextRow);
		}
		nextRow++;
		
		Cell c = getCell(r, nextCol);
		c.setCellValue("taskGroupIds");
		
		nextCol++;
		
		c = getCell(r, nextCol);
		c.setCellValue(selectedTaskGroupIds);
		
		nextCol++;
	}

	private void generateHospitalsSheet(Workbook wb, Map<BUNDLE_FIELD, Map<Object, List<SnFile>>> snCountMap) {
		
		Map<Object, List<SnFile>> hospitalMap = snCountMap.get(ExtTaskService.BUNDLE_FIELD.HOSPITAL);
		
		if(hospitalMap == null) {
			return;
		}
		
		Sheet sheet = wb.createSheet(ExtTaskService.BUNDLE_FIELD.HOSPITAL.name());
		
		int nextRow = 4;
		
		nextRow = addHeader(sheet, nextRow);
				
		for(Object object : hospitalMap.keySet()) {
			if(object == null) continue;
			
			String hospitalName = "";
			if(object instanceof String) {
				hospitalName = (String) object;
			} else {
				if(object instanceof Hospital) {
					Hospital hospital = (Hospital) object;
					hospitalName = hospital.getHospitalName();
				}
				else {
					hospitalName = "Invalid";
				}
			}
			
			List<SnFile> snFiles = hospitalMap.get(object);
			
			SnFile aggratedSnFile = calculateAggratedSnFile(snFiles);
			nextRow = addRow(sheet, nextRow, hospitalName, aggratedSnFile, false );
			
			nextRow++;
		}
		
	}

	/**
	 * place doctors detail in the sheet
	 * @param wb
	 * @param snCountMap
	 */
	private void generateDoctorsSheet(Workbook wb, Map<BUNDLE_FIELD, Map<Object, List<SnFile>>> snCountMap) {
		
		Map<Object, List<SnFile>> doctorMap = snCountMap.get(ExtTaskService.BUNDLE_FIELD.DOCTOR);
		
		if(doctorMap == null) {
			return;
		}
		
		Sheet sheet = wb.createSheet(ExtTaskService.BUNDLE_FIELD.DOCTOR.name());
		
		int nextRow = 4;
		
		nextRow = addHeader(sheet, nextRow);
				
		for(Object object : doctorMap.keySet()) {
			if(object == null) continue;
			
			String doctorName = "";
			if(object instanceof String) {
				doctorName = (String) object;
			} else {
				if(object instanceof Doctor) {
					Doctor doctor = (Doctor) object;
					doctorName = doctor.getDoctorName();
				}
				else {
					doctorName = "Invalid";
				}
			}
			
			List<SnFile> snFiles = doctorMap.get(object);
			
			SnFile aggratedSnFile = calculateAggratedSnFile(snFiles);
			nextRow = addRow(sheet, nextRow, doctorName, aggratedSnFile, true );
			
			for(SnFile snFile : snFiles) {
				if(snFile.getChosenFactor() == ChosenFactor.NONE) continue;
				
				switch(snFile.getChosenFactor()) {
				case TIME_FRAME :
						snFile.setWsFinalLineCount(0);
						snFile.setWosFinalLineCount(0);
						break;
						
				case WS_LINE_COUNT :
						snFile.setFinalTimeFrame(0);
						snFile.setWosFinalLineCount(0);
						break;
						
				case WOS_LINE_COUNT :
						snFile.setFinalTimeFrame(0);
						snFile.setWsFinalLineCount(0);
						break;
				}
				
				
				nextRow = addRow(sheet, nextRow, snFile.getFileName() + "." + snFile.getFileExt(), snFile, false );
			}
			
			nextRow++;
		}
		
	}
	
	
	/*
	 * add data from snFiles and generate resultant snFile
	 */
	private SnFile calculateAggratedSnFile(List<SnFile> snFiles) {
		SnFile aggratedSnFile = new SnFile();
		aggratedSnFile.setFinalTimeFrame(0);
		aggratedSnFile.setWsFinalLineCount(0);
		aggratedSnFile.setWosFinalLineCount(0);
		
		for (SnFile snFile : snFiles) {
			switch(snFile.getChosenFactor()) {
			case TIME_FRAME :
					aggratedSnFile.setFinalTimeFrame(
							aggratedSnFile.getFinalTimeFrame() + 
							(snFile.getFinalTimeFrame() == null ? 0 : snFile.getFinalTimeFrame())
							);
					break;
					
			case WS_LINE_COUNT :
					aggratedSnFile.setWsFinalLineCount(
							aggratedSnFile.getWsFinalLineCount() + 
							(snFile.getWsFinalLineCount() == null ? 0 : snFile.getWsFinalLineCount())
							);
					break;
					
			case WOS_LINE_COUNT :
					aggratedSnFile.setWosFinalLineCount(
							aggratedSnFile.getWosFinalLineCount() + 
							(snFile.getWosFinalLineCount() == null ? 0 : snFile.getWosFinalLineCount())
							);
					break;
			}
		}
		
		return aggratedSnFile;
	}

	private int addHeader(Sheet sheet, int nextRow) {
		Row r = getRow(sheet, nextRow);
		nextRow++;
		
		int nextCol = 2;
		
		Cell c = getCell(r, nextCol);
		c.setCellValue("Name");
		nextCol++;
		
		c = getCell(r, nextCol);
		c.setCellValue("Audio time");
		nextCol++;
		
		c = getCell(r, nextCol);
		c.setCellValue("Ws");
		nextCol++;
		
		c = getCell(r, nextCol);
		c.setCellValue("Wos");
		nextCol++;
		
		markRowBold(r);
		
		return ++nextRow;
	}

	
	/**
	 * create a row in the sheet with the given data
	 * @param sheet
	 * @param nextRow
	 * @param displayName
	 * @param snFile
	 * @param isSpecialRow
	 * @return
	 */
	private int addRow(Sheet sheet, int nextRow, String displayName, SnFile snFile, boolean isSpecialRow) {
		Row r = getRow(sheet, nextRow);
		nextRow++;
		

		
		int nextCol = 2;
		
		Cell c = getCell(r, nextCol);
		c.setCellValue(displayName);
		
		nextCol++;
		
		if(snFile.getFinalTimeFrame() != null && snFile.getFinalTimeFrame() > 0) {
			c = getCell(r, nextCol);
			c.setCellValue(snFile.getFinalTimeFrame());
		}
		
		nextCol++;
		
		if(snFile.getWsFinalLineCount() != null && snFile.getWsFinalLineCount() > 0) {
			c = getCell(r, nextCol);
			c.setCellValue(snFile.getWsFinalLineCount());
		}
		
		nextCol++;
		
		if(snFile.getWosFinalLineCount() != null && snFile.getWosFinalLineCount() > 0) {
			c = getCell(r, nextCol);
			c.setCellValue(snFile.getWosFinalLineCount());
		}
		
		nextCol++;
		
		if(isSpecialRow) {
			markRowBold(r);
		}
		
		
		return nextRow;
	}

	private Row getRow(Sheet sheet, int nextRow) {
		Row r = sheet.getRow(nextRow);
		if (r == null) {
		    r = sheet.createRow(nextRow);
		}
		return r;
	}
	
	public Cell getCell(Row r, int nextCol) {
		Cell c = r.getCell(nextCol);
		if(c == null) {
			c = r.createCell(nextCol);
		}
		
		return c;
	}
	
	
	private void setBoldFont(Workbook wb) {
		Font defaultFont= wb.createFont();
	    defaultFont.setFontHeightInPoints((short)10);
	    defaultFont.setFontName("Arial");
	    defaultFont.setColor(IndexedColors.BLACK.getIndex());
	    defaultFont.setBold(false);
	    defaultFont.setItalic(false);

	    Font font= wb.createFont();
	    font.setFontHeightInPoints((short)10);
	    font.setFontName("Arial");
	    font.setColor(IndexedColors.WHITE.getIndex());
	    font.setBold(true);
	    font.setItalic(false);
	    boldFont = font;
	    
	    cellStyle = wb.createCellStyle();
	}
	
	public void markRowBold(Row row) {
		CellStyle style = row.getRowStyle();
		if(style == null) {
			style = cellStyle;
			row.setRowStyle(style);
		}
	    style.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
//		style.setFillBackgroundColor(arg0);
	    style.setFillPattern(FillPatternType.FINE_DOTS);
	    style.setAlignment(HorizontalAlignment.CENTER);
	    style.setFont(boldFont);
	    
	    for(int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
	    	Cell cell = getCell(row, i);
	    	if(cell == null) continue;
	    	
	    	cell.setCellStyle(style);
	    }
	}
}
