package com.neemshade.sniper.web.rest;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.domain.TaskGroup;
import com.neemshade.sniper.security.AuthoritiesConstants;
import com.neemshade.sniper.service.ExtTaskService;
import com.neemshade.sniper.service.ExtUploaderService;
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
	private ExtUploaderService extUploaderService;

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
	@GetMapping(value="tasks-of-task-group")
	public List<Task> getTasksOfTaskGroup(
			@RequestParam(value = "taskGroupId") Long taskGroupId,
			@RequestParam(value = "fromDate") LocalDate fromDate,
			@RequestParam(value = "toDate") LocalDate toDate,
			Pageable pageable) {

//		log.debug("taskGroupId = " + taskGroupId);
//		log.debug("fromDate = " + fromDate);
//		log.debug("toDate = " + toDate);
		
		Page<Task> page = taskService.findTasksOfTaskGroup(
				taskGroupId,
				fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
				toDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant(),
				pageable);
		return page.getContent();
		//		       HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/ext-task-group-list");
		//		       return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//		return new ArrayList<Task>();
	}
	
	  // upload all the files
	  // source can be task or taskGroup
	  // id can be zero or any id
	  // if taskGroup and id is zero, a new taskGroup will be created
	  // if task and id is zero, error is thrown
		  
	
//	@RequestMapping(value= "/uploadFiles/{source}/{id}", method = {RequestMethod.POST, RequestMethod.GET})
	@PostMapping("/uploadFiles/{source}/{id}")
	public Boolean handleFileUpload(@PathVariable String source, @PathVariable Long id, @RequestParam("file[]") List<MultipartFile> mpFileList) throws Exception {
		
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
}
