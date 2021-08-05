package com.brightminds.assignment.services;

import java.util.List;

import com.brightminds.assignment.dto.StatementDTO;
import com.brightminds.assignment.models.Statement;

public interface IStatementsService {
	List<Statement> getAccountStatements(Integer accountId, StatementDTO statementParams);
}
