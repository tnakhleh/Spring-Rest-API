package com.brightminds.assignment.repositories.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.brightminds.assignment.dto.StatementDTO;
import com.brightminds.assignment.models.Statement;
import com.brightminds.assignment.repositories.dbaccess.DBAccessAdapter;

@Repository
public class StatementRepository {
	
	@Autowired
	private DBAccessAdapter DBAccess;
	
	public List<Statement> getAccountStatementsByAccountIdAndDateAndAmount(Integer accountId, StatementDTO statementParams) {
		List<Statement> result = DBAccess.getAccountStatements(accountId, statementParams);
		return result;
	}

}
