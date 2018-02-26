package com.neemshade.sniper.web.rest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.domain.TaskGroup;
import com.neemshade.sniper.domain.TaskHistory;
import com.neemshade.sniper.security.AuthoritiesConstants;
import com.neemshade.sniper.service.ExtDownloaderService;
import com.neemshade.sniper.service.ExtTaskService;
import com.neemshade.sniper.service.ExtUploaderService;
import com.neemshade.sniper.service.SnFileService;
import com.neemshade.sniper.service.TaskService;

@RestController
@RequestMapping("/api/ext")
public class ExtTaskResource {

	private final Logger log = LoggerFactory.getLogger(ExtTaskResource.class);
	
	@Autowired
	private ExtTaskService extTaskService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private SnFileService snFileService;
	
	@Autowired
	private ExtUploaderService extUploaderService;
	
	@Autowired
	private ExtDownloaderService extDownloaderService;

    public ExtTaskResource() {
    }
	
	@RequestMapping(value="task-groups", method=RequestMethod.GET)
    @GetMapping(params = {"fromDate", "toDate"})
	@Secured({AuthoritiesConstants.MANAGER})
	@Timed
	public List<TaskGroup> getTaskGroupByDates(
       @RequestParam(value = "fromDate") LocalDate fromDate,
       @RequestParam(value = "toDate") LocalDate toDate,
       Pageable pageable) {

       Page<TaskGroup> page = extTaskService.findTaskGroupByDates(
           fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
           toDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant(),
           pageable);
//       HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/ext-task-group-list");
//       return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
       return page.getContent();
	}
	
//	@GetMapping(value="tasks-of-task-group", params = {"taskGroupId", "fromDate", "toDate"})
	@GetMapping(value="tasks")
	public List<Task> getTasks(
			@RequestParam(value = "source") String source,
			@RequestParam(value = "taskGroupId") Long taskGroupId,
			@RequestParam(value = "fromDate") LocalDate fromDate,
			@RequestParam(value = "toDate") LocalDate toDate,
			Pageable pageable) throws Exception {

//		log.debug("taskGroupId = " + taskGroupId);
//		log.debug("fromDate = " + fromDate);
//		log.debug("toDate = " + toDate);
		
		Page<Task> page = extTaskService.findTasks(
				source,
				taskGroupId,
				fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
				toDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant(),
				pageable);
		return page.getContent();
		//		       HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/ext-task-group-list");
		//		       return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//		return new ArrayList<Task>();
	}
	
	
	@GetMapping(value="snfiles-of-task/{taskId}")
	public List<SnFile> getSnFilesOfTask(
			@PathVariable(value = "taskId") Long taskId) {

		return snFileService.findSnFilesOfTask(taskId);

	}
	
	@GetMapping(value="history-of-task/{taskId}")
	public List<TaskHistory> getHistoryOfTask(
			@PathVariable(value = "taskId") Long taskId) {

		return extTaskService.findHistoryOfTask(taskId);

	}
	
	/**
	 * fn collects user, company, hospital and doctor info
	 * 
	 * @return all data is placed in map
	 */
	@GetMapping(value="bundle")
	public Map<ExtTaskService.BUNDLE_FIELD, Object> fetchBundle() {
		return extTaskService.fetchBundle();

	}
	
	
	  // upload all the files
	  // source can be task or taskGroup
	  // id can be zero or any id
	  // if taskGroup and id is zero, a new taskGroup will be created
	  // if task and id is zero, error is thrown
		  
	
//	@RequestMapping(value= "/uploadFiles/{source}/{id}", method = {RequestMethod.POST, RequestMethod.GET})
	@PostMapping("/upload-files/{source}/{id}")
	public Boolean handleFileUpload(
			@PathVariable String source, 
			@PathVariable Long id, 
			@RequestParam("file[]") List<MultipartFile> mpFileList) throws Exception {
		
		try {
			extUploaderService.handleFileUpload(source, id, mpFileList);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		return true;
	}
	

	@GetMapping(value="download-files/{source}/{id}/{selectedIds}", produces="application/zip")
	@ResponseBody
	public ResponseEntity<byte[]> downloadFiles(
			@PathVariable String source, @PathVariable(value = "id") Long id,
			@PathVariable(value = "selectedIds") String selectedIds) throws Exception {

	    byte[] bytes = extDownloaderService.downloadFiles(source, id, selectedIds);
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//	    headers.add("Content-Type", "application/octet-stream");
	    headers.add("Content-Type", "application/zip");
	    headers.add("Content-Disposition", "attachment; filename=\"files.zip\"");
	    return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
	   }
	
	
	@PutMapping("/update-snfiles")
	@Secured({AuthoritiesConstants.MANAGER})
    @Timed
    public String updateSnFiles(@RequestBody List<SnFile> snFiles) throws Exception {
        log.debug("Put request to update snFile list : {}");
        
        if(snFiles == null)
        {
        	throw new Exception("Error! null snFiles");
        }
        
        return extTaskService.updateSnFiles(snFiles);
//        return extTaskService.updateTasks(paramObj);
        
    }
	
	@PutMapping("/update-tasks")
    @Timed
    public String updateTasks(@RequestBody ParamObj paramObj) throws Exception {
        log.debug("Put request to update Task list : {}");
        
        if(paramObj == null)
        {
        	throw new Exception("Error! null ParamObj");
        }
        
//        if(paramObj.getMap() == null)
//        {
//        	throw new Exception("Error! null ParamObj.map");
//        }
        
        return extTaskService.updateTasks(paramObj.getTasks(), paramObj.getHistoryObe(), paramObj.getFieldNames());
//        return extTaskService.updateTasks(paramObj);
        
    }
}


class ParamObj
{
	private List<Task> tasks;
    private TaskHistory historyObe;
    private String fieldNames;
    
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public TaskHistory getHistoryObe() {
		return historyObe;
	}
	public void setHistoryObe(TaskHistory historyObe) {
		this.historyObe = historyObe;
	}
	public String getFieldNames() {
		return fieldNames;
	}
	public void setFieldNames(String fieldNames) {
		this.fieldNames = fieldNames;
	}
	
}