package com.neemshade.sniper.service;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.domain.TaskGroup;
import com.neemshade.sniper.domain.TaskHistory;
import com.neemshade.sniper.domain.User;
import com.neemshade.sniper.domain.UserInfo;
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
	private TaskHistoryRepository taskHistoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;

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


}
