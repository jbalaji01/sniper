package com.neemshade.sniper.service;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.domain.TaskGroup;
import com.neemshade.sniper.domain.UserInfo;
import com.neemshade.sniper.domain.enumeration.ChosenFactor;
import com.neemshade.sniper.domain.enumeration.TaskStatus;
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
	private TaskRepository taskRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private ExtTaskService extTaskService;

    @PersistenceContext
    private EntityManager em;

	public static final String ROOT_TEMP_DIR = System.getProperty("java.io.tmpdir") +
			File.separator + "sniper" + File.separator;

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
	public synchronized void handleFileUpload(String source, Long id, List<MultipartFile> mpFileList) throws Exception {

		if(source == null)
		{
			throw new Exception("Invalid source param");
		}

		if(source.equalsIgnoreCase("taskGroup"))
		{
			uploadFilesOfTaskGroup(id, mpFileList);
			cleanup();
			return;
		}

		if(source.equalsIgnoreCase("task") && id != null && id > 0)
		{
			uploadFilesOfTask(id, mpFileList);
			cleanup();
			return;
		}

		throw new Exception("Invalid data " + source + " " + id);
	}

	public void cleanup() {
		// create root dir, if not exists
				// delete files in root dir
				File directory = new File(ROOT_TEMP_DIR);
				FileSystemUtils.deleteRecursively(directory);

				if (!directory.exists()) {
					directory.mkdirs();
				}
	}
	
	// upload files of task.
	@Transactional
    void uploadFilesOfTask(Long taskId, List<MultipartFile> mpFileList) throws Exception {
		Boolean isInput = false;  // note that any file from Task will be output only

		

		Task task = fetchTask(taskId);

		if(task == null)
		{
			throw new Exception("Invalid task null for " + taskId);
		}

		TaskGroup taskGroup = fetchTaskGroupOfTask(taskId);

		if(taskGroup == null)
		{
			throw new Exception("Invalid taskGroup null for taskId " + taskId);
		}

		task.setTaskGroup(taskGroup);

		List<SnFile> snFileList = convertToSnFiles(task, mpFileList, isInput);

		storeSnFiles(task, snFileList);
//		taskService.save(task);
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

	private TaskGroup fetchTaskGroupOfTask(Long taskId) {
		TaskGroup taskGroup = taskGroupService.findFirstByTasksId(taskId);
		return taskGroup;
	}



	/**
	 * store snFileList of given task
	 * @param task
	 * @param snFileList
	 * @throws Exception
	 */
	public void storeSnFiles(Task task, List<SnFile> snFileList) throws Exception {

		Integer lastPeckOrder = findMaxSnFilePeckOrder(task) + 10;

		for(SnFile snFile: snFileList)
		{
			 snFile.getTasks().add(task);
			snFile.setPeckOrder(lastPeckOrder);
			extTaskService.adjustLineCount(task, snFile);
			updateChosenType(task, snFile);

			lastPeckOrder += 10;
			SnFile newSnFile = snFileService.save(snFile);
			
//			if(task != null) {
//				task.getSnFiles().add(snFile);
//			}
//			else {
//				SnFile newSnFile = snFileService.save(snFile);
//			}

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
    void uploadFilesOfTaskGroup(Long taskGroupId, List<MultipartFile> mpFileList) throws Exception {

//		log.debug("eus num of files = " + mpFileList.size());

		Boolean isInput = true;  // note that any file from TaskGroup will be input only

		TaskGroup taskGroup = fetchTaskGroup(taskGroupId);
		
		LinkedHashMap<Task, List<MultipartFile>> taskMap = clubFilesIntoTasks(taskGroup, mpFileList);
		

		for(Entry<Task, List<MultipartFile>> entry: taskMap.entrySet()){
	        Task task = entry.getKey();
	        List<MultipartFile> subMpFileList = entry.getValue();

	        if(task == null || subMpFileList == null) continue;

	        List<SnFile> snFileList = convertToSnFiles(task, subMpFileList, isInput);
	        storeSnFiles(task, snFileList);
		}

		

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

//		String groupName = DateTimeFormatter.ofPattern("dd MMM yy HH:mm:ss").format(taskGroup.getCreatedTime());
		DateTimeFormatter formatter =
			    DateTimeFormatter.ofLocalizedDateTime( FormatStyle.MEDIUM )
			                     .withLocale( Locale.UK )
			                     .withZone( ZoneId.systemDefault() );
		String groupName = formatter.format(taskGroup.getCreatedTime());
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
	private LinkedHashMap<Task, List<MultipartFile>> clubFilesIntoTasks(TaskGroup taskGroup, List<MultipartFile> mpFileList) throws Exception
	{
		LinkedHashMap<Task, List<MultipartFile>> taskMap = new LinkedHashMap<Task, List<MultipartFile>>();
		Map<String, Task> fileNameMap = new HashMap<String, Task>();

		Integer lastPeckOrder = findMaxTaskPeckOrder(taskGroup) + 10;

		for(MultipartFile mpFile: mpFileList)
		{
			if(mpFile == null) continue;
			
			String fullFilename = mpFile.getOriginalFilename();
			int pos = fullFilename.indexOf(".");
//			String extension = pos < 0 ? "" : fullFilename.substring(pos + 1);
			String filename = pos < 0 ? fullFilename : fullFilename.substring(0,  pos);

			
			Task matchingTask = fetchMatchingTask(fileNameMap, filename);

			// if no match, then create task and set the maps
			if(matchingTask == null)
			{
				Task task = extTaskService.createTask(taskGroup, lastPeckOrder, fullFilename);
				lastPeckOrder += 10;
				taskMap.put(task, new ArrayList<MultipartFile>());
				fileNameMap.put(filename, task);
				matchingTask = task;
			}

			taskMap.get(matchingTask).add(mpFile);
		}

		return taskMap;
	}



	/**
	 * update chosenType of snFile based on doctor and hospital of task
	 * @param task
	 * @param snFile
	 */
	private void updateChosenType(Task task, SnFile snFile) {
		if(task == null || snFile == null) {
			return;
		}

		if(task.getDoctor() != null && task.getDoctor().getChosenFactor() != null) {
			snFile.setChosenFactor(task.getDoctor().getChosenFactor());
		} else {
			if(task.getHospital() != null && task.getHospital().getChosenFactor() != null) {
				snFile.setChosenFactor(task.getHospital().getChosenFactor());
			}
		}

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
		// if(snFile == null || snFile.getFileName() == null)
		// 	return null;
		// if(snFile.getFileName().matches("(?i:.*_vr$)"))
		// {
		// 	int pos = snFile.getFileName().lastIndexOf("_");
		// 	String searchFileName = snFile.getFileName().substring(0, pos);
		// 	return fileNameMap.get(searchFileName);
		// }

		// return null;

		if(snFile != null) 
			return fetchMatchingTask(fileNameMap, snFile.getFileName());
		return null;
	}

	/**
	 * look for the file name is already placed in map
	 * @param fileNameMap
	 * @param snFile
	 * @return a task that is matching above criteria
	 */
	private Task fetchMatchingTask(Map<String, Task> fileNameMap, String fileName) {
		if(fileName == null)
			return null;
		
		return fileNameMap.get(fileName);
		
	}


	/**
	 * multipart files are placed in storage and corresponding snFiles are created
	 * taskGroup uploaded files have isInput=true, task uploaded files have isInput=false
	 * @param mpFileList - mpfiles from the client module
	 * @param isInput - input audio, vr files or doc output files
	 * @return
	 * @throws Exception
	 */
	private List<SnFile> convertToSnFiles(Task task, List<MultipartFile> mpFileList, java.lang.Boolean isInput) throws Exception {

		List<SnFile> snFileList = new ArrayList<SnFile>();

		// user who is uploading this file
		UserInfo userInfo = extTaskService.fetchLoggedInUserInfo();

		for(MultipartFile mpFile : mpFileList)
		{
			SnFile snFile = convertToSnFile(task, mpFile, isInput);
			snFile.setUploader(userInfo);

			snFileList.add(snFile);
		}

//		// create root dir, if not exists
//		// delete files in root dir
//		File directory = new File(ROOT_TEMP_DIR);
//		FileSystemUtils.deleteRecursively(directory);
//
//		if (!directory.exists()) {
//			directory.mkdirs();
//		}

		return snFileList;
	}


	/**
	 * convert single multipart file into SnFile
	 * has a side effect of storing content in dir
	 * @param mpFile
	 * @param isInput
	 * @return
	 * @throws Exception
	 */
	private SnFile convertToSnFile(Task task, MultipartFile mpFile, java.lang.Boolean isInput) throws Exception {

//		log.debug("ofn=" + mpFile.getOriginalFilename());
//		log.debug("tmpdir = " + System.getProperty("java.io.tmpdir"));

		SnFile snFile = createSnFile(task, mpFile, isInput);
		initializeSnFile(snFile, mpFile);
		
		
		return snFile;
	}

	/**
	 * create required snFile and blob
	 * @param content
	 * @param contentSize
     * @param isInput
     * @return
	 */
	public SnFile createSnFile(Task task, MultipartFile mpFile, Boolean isInput) throws Exception{
				
		SnFile snFile = new SnFile();
		if(task != null)
			snFile.getTasks().add(task);
		
		snFile.setFileSize(mpFile == null ? 0 : mpFile.getSize());
		snFile.setIsInput(isInput);

	
		if(mpFile == null) return snFile;
		
		InputStream content = mpFile.getInputStream();
		
		// if there is juice, do these additional actions
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
		
		snFile.setFilePath(composeFilePath(task, snFile));
		File file = writeToFileSystem(snFile, content);

		fillMetrics(snFile, file);
		

		return snFile;
	}

	public static String composeFilePath(Task task, SnFile snFile) {
		String relativePath = "sniperStore";
//		Task task = (Task) snFile.getTasks().toArray()[0];

		if(task == null) return null;
		TaskGroup taskGroup = task.getTaskGroup();
		if(taskGroup == null) return null;

		relativePath = relativePath + File.separatorChar + "tg_" + taskGroup.getId() +
			File.separatorChar + "t_"  + task.getId();

		return relativePath;
	}

	/**
	 * write the file into dir
	 */
	private File writeToFileSystem(SnFile snFile, InputStream content) throws Exception {
		String fullPathName = getFullPath(snFile);

		// if(file == null) return null;
		// Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
		// Files.write(path, bytes);
		
//		synchronized (fullPathName) {
			Path path = Paths.get(fullPathName);
			// Files.write(path, content.);
			Files.copy(content, path);
//		}
		
		File file = new File(fullPathName);
		return file;
	}

	/**
	 * full path of the snFile
	 */
	public static String getFullPath(SnFile snFile) throws UnsupportedEncodingException {
		String fullPathName = getStorePath() + File.separatorChar + snFile.getFilePath();
		File directory = new File(fullPathName);
		directory.mkdirs();

		fullPathName += File.separatorChar + snFile.getFileName() + "." + snFile.getFileExt();
		return fullPathName;
	}

	/**
	 * path of the storage in the server
	 */
	public static String getStorePath() throws UnsupportedEncodingException {

		String path = ExtUploaderService.class.getClassLoader().getResource("").getPath();
		
		String fullPath = URLDecoder.decode(path, "UTF-8");
		
		String pathArr[] = fullPath.split("/WEB-INF/classes/");
		
		System.out.println(fullPath);
		
		System.out.println(pathArr[0]);
		
		fullPath = pathArr[0];
		
		String reponsePath = "";
		
		// to read a file from webcontent
		
		reponsePath = new File(fullPath).getPath();
		
		return reponsePath;
		
		}


	/**
	 * set all initial param
	 * @param snFile
	 * @param mpFile
	 * @throws Exception
	 */
	private void initializeSnFile(SnFile snFile, MultipartFile mpFile) throws Exception {
//		String fullFilename = mpFile.getOriginalFilename();
//		if(fullFilename == null)
//		{
//			throw new Exception("empty filename");
//		}
//
//		int pos = fullFilename.indexOf(".");
//		String extension = pos < 0 ? "" : fullFilename.substring(pos + 1);
//		String filename = pos < 0 ? fullFilename : fullFilename.substring(0,  pos);
//
//		snFile.setFileName(filename);
//		snFile.setFileExt(extension);
		snFile.setUploadedTime(Instant.now());
		snFile.setChosenFactor(ChosenFactor.NONE);

		//fillMetrics(snFile, mpFile);
	}

	private void fillMetrics(SnFile snFile, File file) throws Exception {
		
		if(file == null) return;
		
		FileMetrics fileMetrics = new FileMetrics();

		if(!fileMetrics.isSupported(snFile.getFileExt()))
		{
			throw new Exception("Invalid format " + snFile.getFileName() + "." + snFile.getFileExt());
		}

		// File file = new File(ROOT_TEMP_DIR + mpFile.getOriginalFilename());
		// mpFile.transferTo(file);

		FileMetricsResult fmr = fileMetrics.calculateMetrics(file, snFile.getFileExt());
//		file.delete();


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

	public void initializeFromSnFile(SnFile sourceSnFile, SnFile destSnFile) {
		destSnFile.setFileName(sourceSnFile.getFileName());
		destSnFile.setFileExt(sourceSnFile.getFileExt());
		destSnFile.setFilePath(sourceSnFile.getFilePath());
		destSnFile.setFileSize(sourceSnFile.getFileSize());
		destSnFile.setUploadedTime(Instant.now());
		destSnFile.setChosenFactor(sourceSnFile.getChosenFactor());

		destSnFile.setIsAudio(sourceSnFile.isIsAudio());

	  	if(destSnFile.isIsAudio())
	  	{
	  		destSnFile.setActualTimeFrame(sourceSnFile.getActualTimeFrame());
	  		destSnFile.setAdjustedTimeFrame(sourceSnFile.getAdjustedTimeFrame());
	  		destSnFile.setFinalTimeFrame(sourceSnFile.getFinalTimeFrame());
	  	}
	  	else
	  	{
	  		destSnFile.setWsActualLineCount(sourceSnFile.getWsActualLineCount());
	  		destSnFile.setWsAdjustedLineCount(sourceSnFile.getWsAdjustedLineCount());
	  		destSnFile.setWsFinalLineCount(sourceSnFile.getWsFinalLineCount());

	  		destSnFile.setWosActualLineCount(sourceSnFile.getWosActualLineCount());
	  		destSnFile.setWosAdjustedLineCount(sourceSnFile.getWosAdjustedLineCount());
	  		destSnFile.setWosFinalLineCount(sourceSnFile.getWosFinalLineCount());
	  	}
	}
}
