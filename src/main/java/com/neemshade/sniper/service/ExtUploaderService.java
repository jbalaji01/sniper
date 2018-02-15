package com.neemshade.sniper.service;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.domain.SnFileBlob;
import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.domain.TaskGroup;
import com.neemshade.sniper.domain.UserInfo;
import com.neemshade.sniper.domain.enumeration.ChosenFactor;
import com.neemshade.sniper.domain.enumeration.TaskStatus;
import com.neemshade.sniper.repository.SnFileBlobRepository;
import com.neemshade.sniper.repository.SnFileRepository;
import com.neemshade.sniper.repository.TaskRepository;
import com.neemshade.sniper.repository.UserInfoRepository;
import com.neemshade.sniper.repository.UserRepository;
import com.neemshade.sniper.service.filemetrics.FileMetrics;
import com.neemshade.sniper.service.filemetrics.FileMetricsResult;

@Service
@Transactional
public class ExtUploaderService {
	private final Logger log = LoggerFactory.getLogger(ExtUploaderService.class);

	@Autowired
    private TaskGroupService taskGroupService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private SnFileService snFileService;
	
	@Autowired
	private SnFileRepository snFileRepository;
	
	@Autowired
	private SnFileBlobRepository snFileBlobRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private ExtTaskService extTaskService;

	public ExtUploaderService()
	{
		
	}

	/**
	 * invoke appropriate module to upload files
	 * @param source - taskGroup or task
	 * @param id - 0 for new taskGroup, non-zero for existing taskGroup or task
	 * @param mpFileList - list of multipart files
	 * @throws Exception
	 */
	public void handleFileUpload(String source, Long id, List<MultipartFile> mpFileList) throws Exception {
		
		if(source == null)
		{
			throw new Exception("Invalid source param");
		}
		
		if(source.equalsIgnoreCase("taskGroup"))
		{
			uploadFilesOfTaskGroup(id, mpFileList);
			return;
		}
		
		if(source.equalsIgnoreCase("task") && id != null && id > 0)
		{
			uploadFilesOfTask(id, mpFileList);
			return;
		}
		
		throw new Exception("Invalid data " + source + " " + id);
	}

	// upload files of task.
	@Transactional
	private void uploadFilesOfTask(Long taskId, List<MultipartFile> mpFileList) throws Exception {
		Boolean isInput = false;  // note that any file from Task will be output only
		
		List<SnFile> snFileList = convertToSnFiles(mpFileList, isInput);
		
		Task task = fetchTask(taskId);
		
		if(task == null)
		{
			throw new Exception("Invalid task null for " + taskId);
		}
		
		storeSnFiles(task, snFileList);
	}

	/**
	 * retrieve task
	 * @param taskId
	 * @return
	 */
	private Task fetchTask(Long taskId) {
		Task task = taskService.findOne(taskId);
		return task;
	}
	
	/**
	 * create a new task.  The title is given by the filename
	 * @param taskGroup
	 * @param peckOrder
	 * @param snFile
	 * @return
	 * @throws Exception 
	 */
	private Task createTask(TaskGroup taskGroup, Integer peckOrder, SnFile snFile) throws Exception {
		Task task = new Task();
		
		task.setTaskGroup(taskGroup);
		task.setTaskTitle(snFile.getFileName() + "." + snFile.getFileExt());
		task.setTaskStatus(TaskStatus.CREATED);
		task.setCreatedTime(Instant.now());
		task.setHasPMSignedOff(false);
		task.setIsActive(true);
		task.setPeckOrder(peckOrder);
		task.setNotes("created task");
		Task newTask = taskService.save(task);
		
		extTaskService.createTaskHistory(newTask, TaskStatus.CREATED, newTask.getNotes());
		
		return newTask;
	}

	

	/**
	 * store snFileList of given task
	 * @param task
	 * @param snFileList
	 * @throws Exception 
	 */
	private void storeSnFiles(Task task, List<SnFile> snFileList) throws Exception {
		
		Integer lastPeckOrder = findMaxSnFilePeckOrder(task) + 10;
		
		for(SnFile snFile: snFileList)
		{
			snFile.getTasks().add(task);
			snFile.setPeckOrder(lastPeckOrder);
			lastPeckOrder += 10;
			snFileService.save(snFile);
			
			extTaskService.createTaskHistory(task, TaskStatus.UPLOADED, "uploaded " + snFile.getFileName() + "." + snFile.getFileExt());
		}
		
		
	}
	
	private int findMaxSnFilePeckOrder(Task task) {
		Optional<SnFile> snFileOptional = snFileRepository.findFirstByTasksIdOrderByPeckOrderDesc(task.getId());
		return snFileOptional != null && snFileOptional.isPresent() ? snFileOptional.get().getPeckOrder() : 0;
	}

	/**
	 * go thru each task and store corresponding snFiles
	 * @param taskMap
	 * @throws Exception 
	 */
	private void storeSnFiles(LinkedHashMap<Task, List<SnFile>> taskMap) throws Exception {
		for(Entry<Task, List<SnFile>> entry: taskMap.entrySet()){    
	        Task task = entry.getKey();  
	        List<SnFile> snFileList = entry.getValue();  
	        
	        if(task == null || snFileList == null) continue;
	        
	        storeSnFiles(task, snFileList);
		}
	}

	// taskgroup's files are uploaded
	@Transactional
	private void uploadFilesOfTaskGroup(Long taskGroupId, List<MultipartFile> mpFileList) throws Exception {
		
		log.debug("eus num of files = " + mpFileList.size());
		
		Boolean isInput = true;  // note that any file from TaskGroup will be input only
		
		List<SnFile> snFileList = convertToSnFiles(mpFileList, isInput);
		
		TaskGroup taskGroup = fetchTaskGroup(taskGroupId);
		
		LinkedHashMap<Task, List<SnFile>> taskMap = clubFilesIntoTasks(taskGroup, snFileList);
		
		storeSnFiles(taskMap);
	}
	

	/**
	 * tries to retrieve taskGroup for taskGroupId
	 * If not available, creates taskGroup
	 * @param taskGroupId
	 * @return
	 * @throws Exception 
	 */
	private TaskGroup fetchTaskGroup(Long taskGroupId) throws Exception {
		TaskGroup taskGroup = taskGroupService.findOne(taskGroupId);
		if(taskGroup != null) return taskGroup;
		
		taskGroup = createTaskGroup();
		
		if(taskGroup == null)
		{
			throw new Exception("Unable to retrieve or create taskGroup.  taskGroupId= " + taskGroupId);
		}
		
		return taskGroup;
	}

	private TaskGroup createTaskGroup() {
		TaskGroup taskGroup = new TaskGroup();
		taskGroup.setCreatedTime(Instant.now());
		String groupName = taskGroup.getCreatedTime().toString();
		taskGroup.setGroupName(groupName);
		
		return taskGroupService.save(taskGroup);
	}

	/**
	 * if the files have common names, then place them in the same task
	 * @param taskGroup
	 * @param snFileList
	 * @return
	 * @throws Exception 
	 */
	private LinkedHashMap<Task, List<SnFile>> clubFilesIntoTasks(TaskGroup taskGroup, List<SnFile> snFileList) throws Exception
	{
		LinkedHashMap<Task, List<SnFile>> taskMap = new LinkedHashMap<Task, List<SnFile>>();
		Map<String, Task> fileNameMap = new HashMap<String, Task>();
		
		Integer lastPeckOrder = findMaxTaskPeckOrder(taskGroup) + 10;
		
		for(SnFile snFile: snFileList)
		{
			Task matchingTask = fetchMatchingTask(fileNameMap, snFile);
			
			// if no match, then create task and set the maps
			if(matchingTask == null)
			{
				Task task = createTask(taskGroup, lastPeckOrder, snFile);
				lastPeckOrder += 10;
				taskMap.put(task, new ArrayList<SnFile>());
				fileNameMap.put(snFile.getFileName(), task);
				matchingTask = task;
			}
			
			taskMap.get(matchingTask).add(snFile);
		}
		
		return taskMap;
	}
	
	

	private int findMaxTaskPeckOrder(TaskGroup taskGroup) {
		Optional<Task> taskOptional = taskRepository.findFirstByTaskGroupIdOrderByPeckOrderDesc(taskGroup.getId());
		return taskOptional != null && taskOptional.isPresent() ? taskOptional.get().getPeckOrder() : 0;
	}

	/**
	 * look for file name with _vr suffix.  If so, ignore that portion and see if the first part of the file name is already placed in map
	 * @param fileNameMap
	 * @param snFile
	 * @return a task that is matching above criteria
	 */
	private Task fetchMatchingTask(Map<String, Task> fileNameMap, SnFile snFile) {
		if(snFile == null || snFile.getFileName() == null)
			return null;
		if(snFile.getFileName().matches("(?i:.*_vr$)"))
		{
			int pos = snFile.getFileName().lastIndexOf("_");
			String searchFileName = snFile.getFileName().substring(0, pos);
			return fileNameMap.get(searchFileName);
		}
		
		return null;
	}

	/**
	 * multipart files are placed in db and corresponding snFiles are created
	 * taskGroup uploaded files have isInput=true, task uploaded files have isInput=false
	 * @param mpFileList - mpfiles from the client module
	 * @param isInput - input audio, vr files or doc output files
	 * @return
	 * @throws Exception 
	 */
	private List<SnFile> convertToSnFiles(List<MultipartFile> mpFileList, java.lang.Boolean isInput) throws Exception {
		
		List<SnFile> snFileList = new ArrayList<SnFile>();
		
		// user who is uploading this file
		UserInfo userInfo = extTaskService.fetchLoggedInUserInfo();				
		
		for(MultipartFile mpFile : mpFileList)
		{
			SnFile snFile = convertToSnFile(mpFile, isInput);
			snFile.setUploader(userInfo);
			
			snFileList.add(snFile);
		}
		
		return snFileList;
	}


	/**
	 * convert single multipart file into SnFile
	 * @param mpFile
	 * @param isInput2
	 * @return 
	 * @throws Exception 
	 */
	private SnFile convertToSnFile(MultipartFile mpFile, java.lang.Boolean isInput) throws Exception {
		
		byte[] byteContent = mpFile.getBytes();
		log.debug("ofn=" + mpFile.getOriginalFilename());
		log.debug("tmpdir = " + System.getProperty("java.io.tmpdir"));
		
		SnFileBlob snFileBlob = new SnFileBlob();
		snFileBlob.setFileContent(byteContent);
		SnFileBlob newSnFileBlob = snFileBlobRepository.save(snFileBlob);
		
		// delete tmpfile
		
		SnFile snFile = new SnFile();
		snFile.setSnFileBlob(newSnFileBlob);
		snFile.setIsInput(isInput);
		initializeSnFile(snFile, mpFile);
		
		return snFile;
	}

	/**
	 * set all initial param
	 * @param snFile
	 * @param mpFile 
	 * @throws Exception 
	 */
	private void initializeSnFile(SnFile snFile, MultipartFile mpFile) throws Exception {
		String fullFilename = mpFile.getOriginalFilename();
		if(fullFilename == null)
		{
			throw new Exception("empty filename");
		}
		
		int pos = fullFilename.indexOf(".");
		String extension = pos < 0 ? "" : fullFilename.substring(pos + 1);
		String filename = pos < 0 ? fullFilename : fullFilename.substring(0,  pos);
		
		snFile.setFileName(filename);
		snFile.setFileExt(extension);
		snFile.setUploadedTime(Instant.now());
		snFile.setChosenFactor(ChosenFactor.NONE);
		
		fillMetrics(snFile, mpFile);
	}

	private void fillMetrics(SnFile snFile, MultipartFile mpFile) throws Exception {
		FileMetrics fileMetrics = new FileMetrics();
		
		if(!fileMetrics.isSupported(snFile.getFileExt()))
		{
			throw new Exception("Invalid format " + snFile.getFileName() + "." + snFile.getFileExt());
		}
		
		File file = new File(System.getProperty("java.io.tmpdir") + "/" + mpFile.getOriginalFilename());
		mpFile.transferTo(file);
		
		FileMetricsResult fmr = fileMetrics.calculateMetrics(file, snFile.getFileExt());
		file.delete();
		
		snFile.setIsAudio(fmr.getIsAudio());
		
	  	if(snFile.isIsAudio())
	  	{
	  		snFile.setActualTimeFrame(fmr.getAudioDuration());
	  		snFile.setAdjustedTimeFrame(fmr.getAudioDuration());
	  		snFile.setFinalTimeFrame(fmr.getAudioDuration());
	  	}
	  	else
	  	{
	  		snFile.setWsActualLineCount(fmr.getWsLineCount());
	  		snFile.setWsAdjustedLineCount(fmr.getWsLineCount());
	  		snFile.setWsFinalLineCount(fmr.getWsLineCount());
	  		
	  		snFile.setWosActualLineCount(fmr.getWosLineCount());
	  		snFile.setWosAdjustedLineCount(fmr.getWosLineCount());
	  		snFile.setWosFinalLineCount(fmr.getWosLineCount());
	  	}
		
	}


/*
    
    peckOrder Integer
    */
}
