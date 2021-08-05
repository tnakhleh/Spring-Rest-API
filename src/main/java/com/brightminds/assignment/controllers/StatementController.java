package com.brightminds.assignment.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.brightminds.assignment.dto.StatementDTO;
import com.brightminds.assignment.models.Statement;
import com.brightminds.assignment.services.IStatementsService;

@RestController
@RequestMapping("/v1/account")
public class StatementController extends BaseController{
	
	@Autowired
	private IStatementsService statementsService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void createCourse(@RequestBody String course) {
		
	}
	
	@PreAuthorize("@securityService.checkValidAccess(#statementParams)")
	@PostMapping("/{accountId}/statements")
	public List<Statement> getAccountStatements( @PathVariable Integer accountId, @RequestBody(required=false) StatementDTO statementParams){
		List<Statement> result = statementsService.getAccountStatements(accountId, statementParams);
		return result;
	}
}

