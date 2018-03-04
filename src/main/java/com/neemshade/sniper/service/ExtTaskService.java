package com.neemshade.sniper.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.domain.TaskGroup;
import com.neemshade.sniper.domain.TaskHistory;
import com.neemshade.sniper.domain.User;
import com.neemshade.sniper.domain.UserInfo;
import com.neemshade.sniper.domain.enumeration.TaskStatus;
import com.neemshade.sniper.repository.CompanyRepository;
import com.neemshade.sniper.repository.DoctorRepository;
import com.neemshade.sniper.repository.HospitalRepository;
import com.neemshade.sniper.repository.PatientRepository;
import com.neemshade.sniper.repository.TaskGroupRepository;
import com.neemshade.sniper.repository.TaskHistoryRepository;
import com.neemshade.sniper.repository.UserInfoRepository;
import com.neemshade.sniper.repository.UserRepository;
import com.neemshade.sniper.security.SecurityUtils;

@Service
@Transactional
public class ExtTaskService {	
	
	private final Logger log = LoggerFactory.getLogger(ExtTaskService.class);
	
	@Autowired
    private TaskGroupRepository taskGroupRepository;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private SnFileService snFileService;
	
	@Autowired
	private TaskHistoryRepository taskHistoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private HospitalRepository hospitalRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	public enum TASK_UPDATE_PARAM {
	    TASKS ("tasks"),
	    HISTORY_OBE ("historyObe"),
	    FIELD_NAMES ("fieldNames");
		
		private String field;
		
		public String getField()
		{
			return field;
		}
	    
	 // enum constructor - cannot be public or protected
	    private TASK_UPDATE_PARAM(String field)
	    {
	        this.field = field;
	    }
	}
	
	
	
	public  static enum BUNDLE_FIELD {
		USER ("USER"),
	    COMPANY ("COMPANY"),
	    HOSPITAL ("HOSPITAL"),
	    DOCTOR ("DOCTOR"),
	    PATIENT("PATIENT"),
	    OWNER ("OWNER"),
	    TRANSCRIPT ("TRANSCRIPT"),
	    EDITOR ("EDITOR"),
	    MANAGER ("MANAGER"),
	    DIRECTOR ("DIRECTOR");
		
		private String field;
		
		public String getField()
		{
			return field;
		}
	    
	 // enum constructor - cannot be public or protected
	    private BUNDLE_FIELD(String field)
	    {
	        this.field = field;
	    }
	}
	
	
	
	

    public ExtTaskService() {
    }
    
    /**
     * find the userInfo of the logged in user
     * @return
     * @throws Exception 
     */
    public UserInfo fetchLoggedInUserInfo() throws Exception {
    	try {
    		Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
    		log.info("ets login=" + userLogin.get());
    		Optional<User> user = userRepository.findOneByLogin(userLogin.get());
    		Optional<UserInfo> userInfo = userInfoRepository.findOneByUserId(user.get().getId());

    		return userInfo.get();
    	}
    	catch(Exception ex)
    	{
    		throw new Exception("Unable to find logged in user " + ex.getMessage());
    	}
	}

    // retrieve taskgroup in range given date range
	public Page<TaskGroup> findTaskGroupByDates(Instant fromDate, Instant toDate, Pageable pageable) {
		
		return taskGroupRepository.findAllByCreatedTimeBetweenOrderByCreatedTimeDesc(fromDate, toDate, pageable);
	}
	

	/**
	 * multi handling fn.  gets tasks based on source.  it could be taskgroup's tasks, active tasks or all tasks of user
	 * @param source - taskGroup or activeTasks or allTasks
	 * @param taskGroupId - applicable when source = taskGroup
	 * @param fromDate
	 * @param toDate
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Page<Task> findTasks(
			String source, Long taskGroupId, 
			Instant fromDate, Instant toDate, Pageable pageable) 
					throws Exception {
		if(source == null || "".equals(source))
			throw new Exception("Invalid source " + source);

		if(source.equals("taskGroup"))
			return taskService.findTasksOfTaskGroup(
				taskGroupId,
				fromDate,
				toDate,
				pageable);
		
		if(source.equals("activeTasks"))
			return taskService.findActiveTasksOfUser(
				fromDate,
				toDate,
				pageable);
		
		if(source.equals("allTasks"))
			return taskService.findAllTasksOfUser(
				fromDate,
				toDate,
				pageable);
		
		throw new Exception("Invalid source " + source);
	}
	
	
	public Map<BUNDLE_FIELD, Object> fetchBundle() {
		Map<BUNDLE_FIELD, Object> map = new HashMap<BUNDLE_FIELD, Object>();
		
		map.put(BUNDLE_FIELD.COMPANY, companyRepository.findAllByOrderByCompanyName());
		map.put(BUNDLE_FIELD.HOSPITAL, hospitalRepository.findAllByOrderByHospitalName());
		map.put(BUNDLE_FIELD.DOCTOR, doctorRepository.findAllByOrderByDoctorName());
//		map.put(BUNDLE_FIELD.PATIENT, patientRepository.findAllByOrderByPatientName());
		map.put(BUNDLE_FIELD.USER, userInfoRepository.findAllByOrderByEmpCode());
		
		return map;
	}

	/**
	 * duplicate all the tasks.  If 3 tasks are given, there will be 3 more copies of these given tasks
	 * @param tasks
	 * @return
	 * @throws Exception 
	 */
	public String cloneTasks(List<Task> tasks) throws Exception {
		
		if(tasks == null || tasks.size() <= 0)
        {
        	throw new Exception("Error! empty tasks");
        }
		
		TaskHistory historyObe = new TaskHistory();
		historyObe.setTaskStatus(TaskStatus.CREATED);
		
		for (Task givenTask : tasks) {
			Task newTask = createTask(givenTask.getTaskGroup(), givenTask.getPeckOrder() + 5, null);
			xerox(givenTask, newTask);
			xeroxFiles(givenTask, newTask);
			taskService.save(newTask);
			
			historyObe.setNotes("Cloned from Task " + givenTask.getId());
			createTaskHistory(newTask, historyObe);
		}

		return "{ \"msg\" : \"Cloned " + tasks.size() + " tasks \" }";
	}
	
	/**
	 * merge tasks into one task.  For eg, if 3 tasks are given, one new task is created
	 * the new task will have data from the first task out of the given list
	 * all 3 given tasks will be marked inActive with the status Merged
	 * @param tasks
	 * @return
	 * @throws Exception 
	 */
	public String mergeTasks(List<Task> tasks) throws Exception {
		if(tasks == null || tasks.size() <= 1)
        {
        	throw new Exception("Error! too few tasks");
        }
		
		Task firstTask = tasks.get(0);
		Task newTask = createTask(firstTask.getTaskGroup(), firstTask.getPeckOrder() + 5, null);
		xerox(firstTask, newTask);
		
		TaskHistory historyObe = new TaskHistory();
		historyObe.setTaskStatus(TaskStatus.MERGED);
		historyObe.setNotes("Merged into Task " + newTask.getId());
		
		String taskIds = "";
		
		for (Task givenTask : tasks) {
			
			xeroxFiles(givenTask, newTask);
			
			givenTask.setIsActive(false);
			givenTask.setTaskStatus(TaskStatus.MERGED);
			taskService.save(givenTask);
			createTaskHistory(givenTask, historyObe);
			
			taskIds += ("".equals(taskIds) ? "" : ", ") + givenTask.getId();
		}
		
		taskService.save(newTask);
		
		historyObe.setTaskStatus(TaskStatus.CREATED);
		historyObe.setNotes("Merged from Tasks " + taskIds);
		createTaskHistory(newTask, historyObe);

		return "{ \"msg\" : \"Merged " + tasks.size() + " tasks \" }";
	}

	

	/**
	 * copy data from source to dest
	 * @param sourceTask
	 * @param destTask
	 */
	private void xerox(Task sourceTask, Task destTask) {
		destTask.setTaskTitle(sourceTask.getTaskTitle());
		destTask.setTaskStatus(sourceTask.getTaskStatus());
		destTask.setHasPMSignedOff(sourceTask.isHasPMSignedOff());
		destTask.setIsActive(sourceTask.isIsActive());
		destTask.setNotes(sourceTask.getNotes());
		
		destTask.setCompany(sourceTask.getCompany());
		destTask.setOwner(sourceTask.getOwner());
		destTask.setTranscript(sourceTask.getTranscript());
		destTask.setEditor(sourceTask.getEditor());
		destTask.setManager(sourceTask.getManager());
		destTask.setDoctor(sourceTask.getDoctor());
		destTask.setHospital(sourceTask.getHospital());
	}
	
	/**
	 * copy files from sourceTask into destTask
	 * @param sourceTask
	 * @param destTask
	 */
	private void xeroxFiles(Task sourceTask, Task destTask) {
		List<SnFile> snFiles = snFileService.findSnFilesOfTask(sourceTask.getId());
		destTask.getSnFiles().addAll(snFiles);
	}
	

	/**
	 * create a new task.  The title is given by the filename
	 * @param taskGroup
	 * @param peckOrder
	 * @param snFile
	 * @return
	 * @throws Exception 
	 */
	public Task createTask(TaskGroup taskGroup, Integer peckOrder, SnFile snFile) throws Exception {
		Task task = new Task();
		
		task.setTaskGroup(taskGroup);
		task.setTaskTitle(snFile == null ? "" : snFile.getFileName() + "." + snFile.getFileExt());
		task.setTaskStatus(TaskStatus.CREATED);
		task.setCreatedTime(Instant.now());
		task.setManager(fetchLoggedInUserInfo());
		task.setHasPMSignedOff(false);
		task.setIsActive(false);
		task.setPeckOrder(peckOrder);
		task.setNotes("created task");
		Task newTask = taskService.save(task);
		
		createTaskHistory(newTask, TaskStatus.CREATED, newTask.getNotes());
		
		return newTask;
	}
	
	/**
	   * update given list of tasks,  Given map contains the following
	   * tasks - list of tasks to be updated
	   * historyObe - history records of are created based on status and notes from historyObe
	   * fieldNames - comma separated fields of task that are updated.  Only for display purpose, the fieldNames are not modified
	 * @throws Exception 
	   */
	public String updateTasks(List<Object> result) throws Exception {
//		if(paramObj == null)
//		{
//			throw new Exception("Invalid tasks list.  param is empty");
//		}
		
//		JsonFactory factory = new JsonFactory();
//	    ObjectMapper mapper = new ObjectMapper();
//	    TypeFactory typeFactory = mapper.getTypeFactory();
//	    MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);

//	    Map<String, Object> result = mapper.convertValue(paramObj, mapType);
	    
//	    org.springframework.boot.json.JsonParser springParser = JsonParserFactory.getJsonParser();
//	    Map<String, Object> result = springParser.parseMap(response.get);
		
//		List<Task> tasks = (List<Task>) result.get("tasks");
//		TaskHistory historyObe = null;
//		String fieldNames = null;
//		historyObe = (TaskHistory) result.get("historyObe");
//		fieldNames = (String) result.get("fieldNames");
		
		List<Task> tasks = (List<Task>) result.get(0);
		TaskHistory historyObe = (TaskHistory) result.get(1);
		String fieldNames = (String) result.get(2);
		
//		LinkedHashMap lhmTasks = (LinkedHashMap) result.get(0);
//		Set keys = lhmTasks.keySet();
//		for (Object object : keys) {
//			log.debug("in ets updateTasks " + object.toString());
//		}
//		List<Task> tasks = lhmTasks.get(keys.);
//		TaskHistory historyObe = (TaskHistory) result.get(1);
//		String fieldNames = (String) result.get(2);
//		
		if(historyObe == null)
		{
			historyObe = new TaskHistory();
			historyObe.setNotes("Unknown operation");
			historyObe.setTaskStatus(TaskStatus.SETTING);
		}
				
		for(Task task : tasks) {
			taskService.save(task);
			createTaskHistory(task, historyObe);
		}
//		
		return "Updated " + tasks.size() + " tasks with " + fieldNames + " properties";
	}
	
	/**
	 * if doctor or hospital info available, update ws and wos line count in the snFile
	 * @param task
	 * @param snFile
	 */
	public void adjustLineCount(Task task, SnFile snFile) {
		Integer templateCount = null;
		
		if(task != null && task.getDoctor() != null && task.getDoctor().getTemplateCount() != null) {
			templateCount = task.getDoctor().getTemplateCount();
		}
		else {
			if(task != null && task.getHospital() != null && task.getHospital().getTemplateCount() != null) {
				templateCount = task.getHospital().getTemplateCount();
			}
		}
		
		if(templateCount == null || templateCount == 0)
			return; // nothing to update
		
		snFile.setWsAdjustedLineCount(snFile.getWsActualLineCount() - templateCount);
		snFile.setWsFinalLineCount(snFile.getWsAdjustedLineCount());
		
		snFile.setWosAdjustedLineCount(snFile.getWosActualLineCount() - templateCount);
		snFile.setWosFinalLineCount(snFile.getWosAdjustedLineCount());
	}
	
	/**
	 * most likely the pm would have changed the final count.  update all the given snFiles
	 * @param snFiles
	 * @return
	 */
	public String updateSnFiles(List<SnFile> snFiles) {
		
		
		for(SnFile snFile : snFiles) {
			SnFile updatedSnFile = snFileService.save(snFile);
		}
//		
		return "{ \"msg\" : \"Updated " + snFiles.size() + " files \" }";
	}

	
	public String updateTasks(List<Task> tasks, TaskHistory historyObe, String fieldNames) throws Exception {
		if(historyObe == null)
		{
			historyObe = new TaskHistory();
			historyObe.setNotes("Unknown operation");
			historyObe.setTaskStatus(TaskStatus.SETTING);
		}
				
		for(Task task : tasks) {
			Task updatedTask = taskService.save(task);
			createTaskHistory(updatedTask, historyObe);
		}
//		
		return "{ \"msg\" : \"Updated " + tasks.size() + " tasks with " + fieldNames + " properties\" }";
	}
	
	/**
	 * key method to create history record
	 * @param task
	 * @param taskStatus
	 * @param notes
	 * @throws Exception 
	 */
	public void createTaskHistory(Task task, com.neemshade.sniper.domain.enumeration.TaskStatus taskStatus, String notes) throws Exception
	{
		TaskHistory taskHistory = new TaskHistory();
		taskHistory.setTask(task);
		taskHistory.setTaskStatus(taskStatus);
		taskHistory.setPunchTime(Instant.now());
		taskHistory.setNotes(notes);
		taskHistory.setUserInfo(fetchLoggedInUserInfo());
		
		taskHistoryRepository.save(taskHistory);
	}


	/**
	 * create a history for a given task and given historyObe - a template with status and notes
	 * @param task
	 * @param historyObe
	 * @throws Exception
	 */
	public void createTaskHistory(Task task, TaskHistory historyObe) throws Exception
	{
		createTaskHistory(task, historyObe.getTaskStatus(), historyObe.getNotes());
	}

	public List<TaskHistory> findHistoryOfTask(Long taskId) {
		return taskHistoryRepository.findByTaskIdOrderByPunchTimeDesc(taskId);
	}

	

		
}
