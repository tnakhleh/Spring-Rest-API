package com.brightminds.assignment.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightminds.assignment.dto.StatementDTO;
import com.brightminds.assignment.exceptions.RequestValidationException;
import com.brightminds.assignment.models.Statement;
import com.brightminds.assignment.repositories.impl.StatementRepository;
import com.brightminds.assignment.services.IStatementsService;
import com.brightminds.assignment.util.LoggerUtil;

@Service
public class StatementsService implements IStatementsService {
	
	@Autowired
	private LoggerUtil logger;
	
	@Autowired
	private StatementRepository statementRepository;
	
	@Override
	public List<Statement> getAccountStatements(Integer accountId, StatementDTO statementParams) {
		//Validate parameters
		if(statementParams != null) {
			//validate dateFrom and dateTo parameters if the both are passed
			if(statementParams.getDateFrom() != null && statementParams.getDateTo() != null 
					&& statementParams.getDateFrom().after(statementParams.getDateTo())) {
				
				throw new RequestValidationException("Invalid date's parameters, dateFrom parameter should be less than or equla dateTo");
			}
			
			//validate amountForm and amountTo parameters if the both are passed
			if(statementParams.getAmountFrom() != null && statementParams.getAmountTo() != null 
					&& statementParams.getAmountFrom().doubleValue() > statementParams.getAmountTo().doubleValue()) {
				throw new RequestValidationException("Invalid amount's parameters, amountFrom parameter should be less than or equla amountTo");
			}
			
		}else {
			logger.debug("No Any Parameter passed with Request");
		}
		
		return statementRepository.getAccountStatementsByAccountIdAndDateAndAmount(accountId, statementParams);
	}
}
