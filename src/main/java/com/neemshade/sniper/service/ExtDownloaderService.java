package com.neemshade.sniper.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.domain.SnFileBlob;
import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.repository.SnFileBlobRepository;

@Service
@Transactional
public class ExtDownloaderService {
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private SnFileService snFileService;
	
	@Autowired
	private SnFileBlobRepository snFileBlobRepository;

	/**
	 * invoke appropriate module to download files
	 * @param source - taskGroup or task
	 * @param id - non-zero for existing taskGroup or task
	 * @param selectedIds 
	 * @throws Exception
	 */
	public byte[] downloadFiles(String source, Long id, String selectedIds) throws Exception {
		if(source == null)
		{
			throw new Exception("Invalid source param");
		}
		
		if(source.equalsIgnoreCase("taskGroup") && id != null && id > 0)
		{
			return downloadFilesOfTaskGroup(id);
		}
		
		if(source.equalsIgnoreCase("task") && id != null && id > 0)
		{
			return downloadFilesOfTask(id);
		}
		
		if(source.equalsIgnoreCase("selectedTasks") && selectedIds != null)
		{
			return downloadFilesOfSelectedTasks(selectedIds);
		}
		
		throw new Exception("Invalid data " + source + " " + id + " " + selectedIds);
	}

	/**
	 * the ids of selected tasks are given in comma separated string.
	 * download all those files
	 * @param selectedIds
	 * @return
	 */
	private byte[] downloadFilesOfSelectedTasks(String selectedIds) throws Exception {
		if(selectedIds == null) return null;
		
		String[] selectedIdArr = selectedIds.split(",");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(baos);
	    
	    for (String selectedId : selectedIdArr) {
	    	Long taskId = Long.parseLong(selectedId);
			downloadFilesOfTask(taskId, zos);
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
	private byte[] downloadFilesOfTaskGroup(Long taskGroupId) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(baos);
	    
	    downloadFilesOfTaskGroup(taskGroupId, zos);
	    
	    zos.closeEntry();
	    zos.close();
	    return baos.toByteArray();
	}

	private void downloadFilesOfTaskGroup(Long taskGroupId, ZipOutputStream zos) throws Exception {
		List<Task> tasks = taskService.findTasksOfTaskGroup(taskGroupId);
		
		for (Task task : tasks) {
			downloadFilesOfTask(task.getId(), zos);
		}
	}

	/**
	 * collect all the files in zip format
	 * @param taskId
	 * @return
	 * @throws Exception 
	 */
	private byte[] downloadFilesOfTask(Long taskId) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(baos);
	    
	    downloadFilesOfTask(taskId, zos);
	    
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
	private void downloadFilesOfTask(Long taskId, ZipOutputStream zos) throws Exception {
		
		String path = "task_" + taskId + "/";
		zos.putNextEntry(new ZipEntry(path));
		
		// fetch all the files of this task and write them into zos
		List<SnFile> snFiles = snFileService.findSnFilesOfTask(taskId);
		
		for (SnFile snFile : snFiles) {
			
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



	
}
