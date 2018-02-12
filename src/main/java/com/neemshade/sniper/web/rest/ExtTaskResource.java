package com.neemshade.sniper.web.rest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.neemshade.sniper.domain.TaskGroup;
import com.neemshade.sniper.security.AuthoritiesConstants;
import com.neemshade.sniper.service.ExtTaskService;

@RestController
@RequestMapping("/ext")
public class ExtTaskResource {

	private final ExtTaskService extTaskService;

    public ExtTaskResource(ExtTaskService extTaskService) {
        this.extTaskService = extTaskService;
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
}
